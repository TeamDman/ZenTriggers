package zentriggers.zentriggers.actions;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import zentriggers.zentriggers.TriggerType;

import java.util.List;

public interface IAction<T> {
	public TriggerType getType();
	public void accept(T arg);
	public String transformCommand(String command, T arg);
	default public void transformAndPostCommand(String command, T arg) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		server.commandManager.executeCommand(server, transformCommand(command, arg));
	};
}
