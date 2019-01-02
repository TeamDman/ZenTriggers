package zentriggers.zentriggers.actions;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.TriggerDirector;
import zentriggers.zentriggers.TriggerType;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.zentriggers.actions.DimensionChangedAction")
public class DimensionChangedAction implements IAction<PlayerEvent.PlayerChangedDimensionEvent> {
	public String command;
	public int[]  dimFrom;
	public int[]  dimTo;

	public DimensionChangedAction(int[] dimFrom, int[] dimTo, String command) {
		this.dimFrom = dimFrom;
		this.dimTo = dimTo;
		this.command = command;
	}

	@ZenMethod
	public static void create(int[] dimFrom, int[] dimTo, String command) {
		TriggerDirector.addAction(new DimensionChangedAction(dimFrom, dimTo, command));
	}

	@Override
	public TriggerType getType() {
		return TriggerType.DIMENSION_CHANGE;
	}

	@Override
	public void accept(PlayerEvent.PlayerChangedDimensionEvent arg) {
		if (Arrays.stream(dimFrom).noneMatch(dim -> dim == arg.fromDim) || Arrays.stream(dimTo).noneMatch(dim -> dim == arg.toDim))
			return;

		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		server.commandManager.executeCommand(server, command);
		System.out.println("Triggered command \"" + command + "\" on player " + arg.player + " moving from dimension " + arg.fromDim + " to " + arg.toDim + ".");
	}
}
