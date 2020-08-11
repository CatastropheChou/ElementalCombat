package Tavi007.ElementalCombat.capabilities.attack;

import java.util.HashMap;

import Tavi007.ElementalCombat.ElementalCombat;
import Tavi007.ElementalCombat.ElementalCombatAPI;
import Tavi007.ElementalCombat.capabilities.NBTHelper;
import Tavi007.ElementalCombat.capabilities.SerializableCapabilityProvider;
import Tavi007.ElementalCombat.loading.EntityData;
import Tavi007.ElementalCombat.loading.GeneralData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ElementalAttackCapability {

	@CapabilityInject(IElementalAttack.class)
	public static final Capability<IElementalAttack> ELEMENTAL_ATTACK_CAPABILITY = null;

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(ElementalCombat.MOD_ID, "elemental_attack");

	public static void register() {
		CapabilityManager.INSTANCE.register(IElementalAttack.class, new Capability.IStorage<IElementalAttack>() {

			@Override
			public INBT writeNBT(final Capability<IElementalAttack> capability, final IElementalAttack instance, final Direction side) {

				HashMap<String, Integer> atckMap = instance.getElementalAttack();

				//fill nbt with data
				CompoundNBT nbt = new CompoundNBT();
				nbt.put("elem_atck", NBTHelper.fromMapToNBT(atckMap));
				return nbt;
			}

			@Override
			public void readNBT(final Capability<IElementalAttack> capability, final IElementalAttack instance, final Direction side, final INBT nbt) {

				CompoundNBT nbtCompound = ((CompoundNBT)nbt).getCompound("elem_atck");
				//fill list with data
				instance.setElementalAttack(NBTHelper.fromNBTToMap(nbtCompound));
			}
		}, () -> new ElementalAttack());
	}

	public static ICapabilityProvider createProvider(final IElementalAttack atck) {
		return new SerializableCapabilityProvider<>(ELEMENTAL_ATTACK_CAPABILITY, DEFAULT_FACING, atck);
	}


	/**
	 * Event handler for the {@link IElementalAttack} capability.
	 */
	@Mod.EventBusSubscriber(modid = ElementalCombat.MOD_ID)
	private static class EventHandler {

		/**
		 * Attach the {@link IElementalAttack} capability to all living entities.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
			if(!event.getObject().getEntityWorld().isRemote()) {
				if (event.getObject() instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) event.getObject();

					ResourceLocation rl = new ResourceLocation(entity.getType().getRegistryName().getNamespace(), "elementalproperties/entities/" + entity.getType().getRegistryName().getPath());
					EntityData entityData = ElementalCombat.DATAMANAGER.getEntityDataFromLocation(rl);
					final ElementalAttack elemAtck = new ElementalAttack(entityData.getAttackMap());
					event.addCapability(ID, createProvider(elemAtck));
				}
				else if(event.getObject() instanceof ProjectileEntity) {
					ProjectileEntity entity = (ProjectileEntity) event.getObject();

					//currently source is always null, cause it hasn't been set yet...
					//maybe do it differently?
					Entity source = entity.func_234616_v_();
					if(source != null && source instanceof LivingEntity) { 
						LivingEntity sourceEntity = (LivingEntity) source;
						ElementalAttack sourceData;
						if(sourceEntity.hasItemInSlot(EquipmentSlotType.MAINHAND)) {
							sourceData = ElementalCombatAPI.getElementalAttackData(sourceEntity.getActiveItemStack());
						}
						else {
							sourceData = ElementalCombatAPI.getElementalAttackData(sourceEntity);
						}
						event.addCapability(ID, createProvider(sourceData));
					}
				}
			}
		}

		@SubscribeEvent
		public static void attachCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event) {
			ItemStack item = event.getObject();
			ResourceLocation rl = new ResourceLocation(item.getItem().getRegistryName().getNamespace(), "elementalproperties/items/" + item.getItem().getRegistryName().getPath());
			GeneralData itemData = ElementalCombat.DATAMANAGER.getItemDataFromLocation(rl);

			final ElementalAttack elemAtck = new ElementalAttack(itemData.getAttackMap());
			event.addCapability(ID, createProvider(elemAtck));
		}
	}
}