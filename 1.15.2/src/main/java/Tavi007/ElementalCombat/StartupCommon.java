package Tavi007.ElementalCombat;

import Tavi007.ElementalCombat.capabilities.defense.ElementalDefenseCapability;
import Tavi007.ElementalCombat.enchantments.ElementalResistanceEnchantment;
import Tavi007.ElementalCombat.enchantments.ElementalWeaponEnchantment;
import Tavi007.ElementalCombat.capabilities.attack.ElementalAttackCapability;
import Tavi007.ElementalCombat.particle.AbsorbParticleData;
import Tavi007.ElementalCombat.particle.AbsorbParticleType;
import Tavi007.ElementalCombat.particle.ImmunityParticleData;
import Tavi007.ElementalCombat.particle.ImmunityParticleType;
import Tavi007.ElementalCombat.particle.ResistanceParticleData;
import Tavi007.ElementalCombat.particle.ResistanceParticleType;
import Tavi007.ElementalCombat.particle.WeaknessParticleData;
import Tavi007.ElementalCombat.particle.WeaknessParticleType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class StartupCommon {

	public static ParticleType<WeaknessParticleData> weaknessParticleType = new WeaknessParticleType();
	public static ParticleType<ResistanceParticleData> resistanceParticleType = new ResistanceParticleType();
	public static ParticleType<ImmunityParticleData> immunityParticleType = new ImmunityParticleType();
	public static ParticleType<AbsorbParticleData> absorbParticleType = new AbsorbParticleType();
	
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event){
		ElementalAttackCapability.register();
		ElementalDefenseCapability.register();
		ElementalCombat.LOGGER.info("setup method registered.");
    }
	
	@SubscribeEvent
	public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> iParticleTypeRegisterEvent) {
	    iParticleTypeRegisterEvent.getRegistry().register(weaknessParticleType.setRegistryName("elementalcombat:weakness"));
	    iParticleTypeRegisterEvent.getRegistry().register(resistanceParticleType.setRegistryName("elementalcombat:resistance"));
	    iParticleTypeRegisterEvent.getRegistry().register(immunityParticleType.setRegistryName("elementalcombat:immunity"));
	    iParticleTypeRegisterEvent.getRegistry().register(absorbParticleType.setRegistryName("elementalcombat:absorb"));
	    
	    ElementalCombat.LOGGER.info("ElementalCombat particles registered.");
	}
	
	@SubscribeEvent
	public static void registerEnchantments(Register<Enchantment> event) {
		event.getRegistry().register(new ElementalResistanceEnchantment(ElementalResistanceEnchantment.Type.FIRE).setRegistryName(Enchantments.FIRE_PROTECTION.getRegistryName()));
		event.getRegistry().register(new ElementalResistanceEnchantment(ElementalResistanceEnchantment.Type.ICE).setRegistryName("ice_protection"));
		event.getRegistry().register(new ElementalResistanceEnchantment(ElementalResistanceEnchantment.Type.WATER).setRegistryName("water_protection"));
		event.getRegistry().register(new ElementalResistanceEnchantment(ElementalResistanceEnchantment.Type.THUNDER).setRegistryName("thunder_protection"));

		event.getRegistry().register(new ElementalWeaponEnchantment(ElementalWeaponEnchantment.Type.ICE).setRegistryName("ice_aspect"));
		event.getRegistry().register(new ElementalWeaponEnchantment(ElementalWeaponEnchantment.Type.WATER).setRegistryName("water_aspect"));
		event.getRegistry().register(new ElementalWeaponEnchantment(ElementalWeaponEnchantment.Type.THUNDER).setRegistryName("thunder_aspect"));

	    ElementalCombat.LOGGER.info("ElementalCombat enchantments registered.");
	}
}
