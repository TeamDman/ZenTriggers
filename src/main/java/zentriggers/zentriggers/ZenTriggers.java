package zentriggers.zentriggers;

import net.minecraftforge.fml.common.Mod;

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
