package Tavi007.ElementalCombat;

import Tavi007.ElementalCombat.particle.AbsorbParticle;
import Tavi007.ElementalCombat.particle.ImmunityParticle;
import Tavi007.ElementalCombat.particle.ResistanceParticle;
import Tavi007.ElementalCombat.particle.WeaknessParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class StartupClientOnly {
	
	@SubscribeEvent
	public static void onClientSetupEvent(final FMLClientSetupEvent event)
	{
		ElementalCombat.LOGGER.info("clientRegistries method registered.");	
	}
	
	@SubscribeEvent
	public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
		// beware - there are two registerFactory methods with different signatures.
		// If you use the wrong one it will put Minecraft into an infinite loading loop with no console errors
		Minecraft.getInstance().particles.registerFactory(StartupCommon.weaknessParticleType, sprite -> new WeaknessParticle.Factory(sprite));
		Minecraft.getInstance().particles.registerFactory(StartupCommon.resistanceParticleType, sprite -> new ResistanceParticle.Factory(sprite));
		Minecraft.getInstance().particles.registerFactory(StartupCommon.immunityParticleType, sprite -> new ImmunityParticle.Factory(sprite));
		Minecraft.getInstance().particles.registerFactory(StartupCommon.absorbParticleType, sprite -> new AbsorbParticle.Factory(sprite));
		//  This lambda may not be obvious: its purpose is:
		//  the registerFactory method creates an IAnimatedSprite, then passes it to the constructor of FlameParticleFactory

		//  General rule of thumb:
		// If you are creating a TextureParticle with a corresponding json to specify textures which will be stitched into the
		//    particle texture sheet, then use the 1-parameter constructor method
		// If you're supplying the render yourself, or using a texture from the block sheet, use the 0-parameter constructor method
		//   (examples are MobAppearanceParticle, DiggingParticle).  See ParticleManager::registerFactories for more.
		
	    ElementalCombat.LOGGER.info("ElementalCombat particles factory registered.");
	}
}
