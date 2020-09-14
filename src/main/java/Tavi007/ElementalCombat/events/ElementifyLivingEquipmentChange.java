package Tavi007.ElementalCombat.events;

import Tavi007.ElementalCombat.ElementalCombat;
import Tavi007.ElementalCombat.ElementalCombatAPI;
import Tavi007.ElementalCombat.capabilities.defense.DefenseData;
import Tavi007.ElementalCombat.curios.HandleCuriosInventory;
import Tavi007.ElementalCombat.loading.EntityCombatProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ElementalCombat.MOD_ID, bus = Bus.FORGE)
public class ElementifyLivingEquipmentChange 
{
	@SubscribeEvent
	public static void elementifyLivingEquipmentChange(LivingEquipmentChangeEvent event)
	{
		//change defense properties
		LivingEntity entity = event.getEntityLiving();
		if(event.getSlot().getSlotType() == EquipmentSlotType.Group.ARMOR)
		{

			// get default values
			ResourceLocation rlEntity = entity.getType().getRegistryName();
			ResourceLocation rlData = new ResourceLocation(ElementalCombat.MOD_ID, "entities/" + rlEntity.getNamespace() + "/" + rlEntity.getPath());
			EntityCombatProperties entityData = ElementalCombat.COMBAT_PROPERTIES_MANGER.getEntityDataFromLocation(rlData);
			
			DefenseData defData = new DefenseData(entityData.getDefenseStyle(), entityData.getDefenseElement());
			// get values from armor
			entity.getArmorInventoryList().forEach(item -> {
				if (!item.isEmpty()) {
					DefenseData defCapItem = ElementalCombatAPI.getDefenseDataWithEnchantment(item);
					defData.sum(defCapItem);
				}
			});
			// cross-mod interaction with curios
			if (ModList.get().isLoaded("curios")) {
				DefenseData defCapItem = HandleCuriosInventory.getDefenseData(entity);
				defData.sum(defCapItem);
			}

			// set values
			DefenseData defCapEntity = ElementalCombatAPI.getDefenseData(entity);
			defCapEntity.setStyleFactor(defData.getStyleFactor());
			defCapEntity.setElementFactor(defData.getElementFactor());
		}
	}
}
