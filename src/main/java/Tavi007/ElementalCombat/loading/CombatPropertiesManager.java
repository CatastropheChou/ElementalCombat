package Tavi007.ElementalCombat.loading;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Queues;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import Tavi007.ElementalCombat.ElementalCombat;
import Tavi007.ElementalCombat.network.BasePropertiesMessage;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElementalCombat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CombatPropertiesManager extends JsonReloadListener 
{
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public static final ResourceLocation EMPTY_RESOURCELOCATION = new ResourceLocation(ElementalCombat.MOD_ID, "empty");

	private Map<ResourceLocation, MobCombatProperties> registeredMobData = ImmutableMap.of();
	private Map<ResourceLocation, ItemCombatProperties> registeredItemData = ImmutableMap.of();
	private Map<ResourceLocation, BiomeCombatProperties> registeredBiomeData = ImmutableMap.of();
	private Map<ResourceLocation, AttackOnlyCombatProperties> registeredDamageSourceData = ImmutableMap.of();
	private Map<ResourceLocation, AttackOnlyCombatProperties> registeredProjectileData = ImmutableMap.of();

	private static ThreadLocal<Deque<CombatPropertiesContext>> dataContext = new ThreadLocal<Deque<CombatPropertiesContext>>();

	public CombatPropertiesManager() {
		super(GSON, "combat_properties");
	}

	public void set(BasePropertiesMessage message) {
		this.registeredMobData = message.getMobData();
		this.registeredItemData = message.getItemData();
		this.registeredBiomeData = message.getBiomeData();
		this.registeredProjectileData = message.getProjectileData();
		this.registeredDamageSourceData = message.getDamageSourceData();
		
		logLoading("client", registeredMobData.size(), "mobs");
		logLoading("client", registeredItemData.size(), "items");
		logLoading("client", registeredBiomeData.size(), "biomes");
		logLoading("client", registeredProjectileData.size(), "projectiles");
		logLoading("client", registeredDamageSourceData.size(), "damage sources");
	}
	
	private void logLoading(String side, int size, String type) {
		ElementalCombat.LOGGER.info(side + " loaded " + size + " combat properties for " + type);
	}

	public BasePropertiesMessage createSyncMessage() {
		return new BasePropertiesMessage(registeredMobData, registeredItemData, registeredBiomeData, registeredProjectileData, registeredDamageSourceData);
	}

	protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		Builder<ResourceLocation, MobCombatProperties> builderMob = ImmutableMap.builder();
		Builder<ResourceLocation, ItemCombatProperties> builderItem = ImmutableMap.builder();
		Builder<ResourceLocation, BiomeCombatProperties> builderBiome = ImmutableMap.builder();
		Builder<ResourceLocation, AttackOnlyCombatProperties> builderDamageSource = ImmutableMap.builder();
		Builder<ResourceLocation, AttackOnlyCombatProperties> builderProjectile = ImmutableMap.builder();

		if (objectIn.remove(EMPTY_RESOURCELOCATION) != null) 
		{
			ElementalCombat.LOGGER.warn("Datapack tried to redefine {} elemental entity data, ignoring", (Object)EMPTY_RESOURCELOCATION);
		}

		Map<String, Map<String, Integer>> counter = new HashMap<String, Map<String, Integer>>();
		objectIn.forEach((rl, json) -> 
		{
			try (net.minecraft.resources.IResource res = resourceManagerIn.getResource(getPreparedPath(rl));)
			{
				String modid = rl.getNamespace();
				String type = "incorrect entries (check path!)";
				//check if entity/item/biome/damageSource gets loaded
				if(rl.getPath().contains("mobs/")){
					MobCombatProperties combatProperties = loadData(GSON, rl, json, res == null || !res.getPackName().equals("main"), MobCombatProperties.class);
					builderMob.put(rl, combatProperties);
					type = "mobs";
				}
				else if(rl.getPath().contains("items/")){
					ItemCombatProperties combatProperties = loadData(GSON, rl, json, res == null || !res.getPackName().equals("main"), ItemCombatProperties.class);
					builderItem.put(rl, combatProperties);
					type = "items";
				}
				else if(rl.getPath().contains("biomes/")){
					BiomeCombatProperties combatProperties = loadData(GSON, rl, json, res == null || !res.getPackName().equals("main"), BiomeCombatProperties.class);
					builderBiome.put(rl, combatProperties);
					type = "biomes";
				}
				else if(rl.getPath().contains("damage_sources/")){
					AttackOnlyCombatProperties combatProperties = loadData(GSON, rl, json, res == null || !res.getPackName().equals("main"), AttackOnlyCombatProperties.class);
					builderDamageSource.put(rl, combatProperties);
					type = "damage_sources";
				}
				else if(rl.getPath().contains("projectiles/")){
					AttackOnlyCombatProperties combatProperties = loadData(GSON, rl, json, res == null || !res.getPackName().equals("main"), AttackOnlyCombatProperties.class);
					builderProjectile.put(rl, combatProperties);
					type = "projectiles";
				}
				
				Map<String, Integer> propertyCounter = counter.get(modid);
				if(propertyCounter == null) {
					propertyCounter = new HashMap<String, Integer>();
					propertyCounter.put(type, 1);
					counter.put(modid, propertyCounter);
				} else {
					Integer count = propertyCounter.get(type);
					if(count == null) {
						propertyCounter.put(type, 1);
					} else {
						propertyCounter.put(type, propertyCounter.get(type) + 1);
					}
				}
			}
			catch (Exception exception)
			{
				ElementalCombat.LOGGER.error("Couldn't parse combat properties {}", rl, exception);
			}
		});

		//not sure if empty resourceLocation is necessary...
		builderMob.put(EMPTY_RESOURCELOCATION, new MobCombatProperties());
		this.registeredMobData = builderMob.build();

		builderItem.put(EMPTY_RESOURCELOCATION, new ItemCombatProperties());
		this.registeredItemData = builderItem.build();

		builderBiome.put(EMPTY_RESOURCELOCATION, new BiomeCombatProperties());
		this.registeredBiomeData = builderBiome.build();

		builderDamageSource.put(EMPTY_RESOURCELOCATION, new AttackOnlyCombatProperties());
		this.registeredDamageSourceData = builderDamageSource.build();

		builderProjectile.put(EMPTY_RESOURCELOCATION, new AttackOnlyCombatProperties());
		this.registeredProjectileData = builderProjectile.build();
		
		
		counter.forEach( (modid, propertyCounter) -> {
			ElementalCombat.LOGGER.info("The mod " + modid + " loaded: ");
			propertyCounter.forEach( (type,amount) -> {
				ElementalCombat.LOGGER.info("" + amount + " " + type);
			});
		});

		logLoading("server", registeredMobData.size(), "mobs");
		logLoading("server", registeredItemData.size(), "items");
		logLoading("server", registeredBiomeData.size(), "biomes");
		logLoading("server", registeredProjectileData.size(), "projectiles");
		logLoading("server", registeredDamageSourceData.size(), "damage sources");
	}

	@Nullable
	private <T> T loadData(Gson gson, ResourceLocation name, JsonElement data, boolean custom, Class<T> classOfT)
	{
		Deque<CombatPropertiesContext> que = dataContext.get();
		if (que == null){
			que = Queues.newArrayDeque();
			dataContext.set(que);
		}

		T ret = null;
		try{
			que.push(new CombatPropertiesContext(name, custom));
			ret = gson.fromJson(data, classOfT);
			que.pop();
		}
		catch (JsonParseException e){
			que.pop();
			throw e;
		}
		return ret;
	}

	public MobCombatProperties getMobDataFromLocation(ResourceLocation rl){
		return new MobCombatProperties(this.registeredMobData.getOrDefault(rl, new MobCombatProperties()));
	}

	public ItemCombatProperties getItemDataFromLocation(ResourceLocation rl){
		return new ItemCombatProperties(this.registeredItemData.getOrDefault(rl, new ItemCombatProperties()));
	}

	public BiomeCombatProperties getBiomeDataFromLocation(ResourceLocation rl){
		return new BiomeCombatProperties(this.registeredBiomeData.getOrDefault(rl, new BiomeCombatProperties()));
	}

	public AttackOnlyCombatProperties getDamageSourceDataFromLocation(ResourceLocation rl){
		return new AttackOnlyCombatProperties(this.registeredDamageSourceData.getOrDefault(rl, new AttackOnlyCombatProperties()));
	}

	public AttackOnlyCombatProperties getProjectileDataFromLocation(ResourceLocation rl){
		return new AttackOnlyCombatProperties(this.registeredProjectileData.getOrDefault(rl, new AttackOnlyCombatProperties()));
	}
}