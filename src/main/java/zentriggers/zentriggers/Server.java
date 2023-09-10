package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.server.ServerPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".server.Server")
@ZenRegister
public class Server {
	@ZenMethod
	public static void executeCommand(String command) {
		FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(
			ServerPlayer.INSTANCE, command
		);
	}
}
