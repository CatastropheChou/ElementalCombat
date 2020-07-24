package Tavi007.ElementalCombat;

import Tavi007.ElementalCombat.capabilities.attack.ElementalAttack;
import Tavi007.ElementalCombat.capabilities.attack.ElementalAttackCapability;
import Tavi007.ElementalCombat.capabilities.defense.ElementalDefense;
import Tavi007.ElementalCombat.capabilities.defense.ElementalDefenseCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;

public class ElementalCombatAPI 
{
	//LivingEntity
	public static ElementalAttack getElementalAttackData(LivingEntity entity){
		return (ElementalAttack) entity.getCapability(ElementalAttackCapability.ELEMENTAL_ATTACK_CAPABILITY, null).orElse(new ElementalAttack());
	}
	
	public static ElementalDefense getElementalDefenseData(LivingEntity entity){
		return (ElementalDefense) entity.getCapability(ElementalDefenseCapability.ELEMENTAL_DEFENSE_CAPABILITY, null).orElse(new ElementalDefense());
	}
	
	//ItemStack
	public static ElementalAttack getElementalAttackData(ItemStack item){
		return (ElementalAttack) item.getCapability(ElementalAttackCapability.ELEMENTAL_ATTACK_CAPABILITY, null).orElse(new ElementalAttack());
	}
	
	public static ElementalDefense getElementalDefenseData(ItemStack item){
		return (ElementalDefense) item.getCapability(ElementalDefenseCapability.ELEMENTAL_DEFENSE_CAPABILITY, null).orElse(new ElementalDefense());
	}
	
	//Projectiles
//	public static ElementalAttack getElementalAttackData(DamagingProjectileEntity entity){
//		return entity.getCapability(ElementalAttackCapability.ELEMENTAL_ATTACK_CAPABILITY, null).orElse(new ElementalAttack());
//	}
}
