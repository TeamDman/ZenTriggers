package zentriggers.zentriggers;

import crafttweaker.CraftTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = ZenTriggers.MOD_ID,
		name = ZenTriggers.MOD_NAME,
		version = ZenTriggers.VERSION
)
public class ZenTriggers {

	public static final String MOD_ID   = "zentriggers";
	public static final String MOD_NAME = "Zen Triggers";
	public static final String VERSION  = "@VERSION@";
	public static final String ZEN_PACKAGE = "mods."+MOD_ID;


	@Mod.Instance(MOD_ID)
	public static ZenTriggers INSTANCE;


}
