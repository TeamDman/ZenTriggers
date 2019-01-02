package zentriggers.zentriggers.actions;

import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.Transformer;
import zentriggers.zentriggers.TriggerDirector;
import zentriggers.zentriggers.TriggerType;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.zentriggers.actions.DimensionChangedAction")
public class DimensionChangedAction implements IAction<PlayerEvent.PlayerChangedDimensionEvent> {
	public final String command;
	public final int[]  dimFrom;
	public final int[]  dimTo;

	public DimensionChangedAction(String command, int[] dimFrom, int[] dimTo) {
		this.command = command;
		this.dimFrom = dimFrom;
		this.dimTo = dimTo;
	}

	@ZenMethod
	public static void create(String command, int[] dimFrom, int[] dimTo) {
		TriggerDirector.addAction(new DimensionChangedAction(command, dimFrom, dimTo));
	}

	@Override
	public TriggerType getType() {
		return TriggerType.DIMENSION_CHANGE;
	}

	@Override
	public void accept(PlayerEvent.PlayerChangedDimensionEvent arg) {
		//todo: empty list is wildcard instead of exclude all
		if (Arrays.stream(dimFrom).noneMatch(dim -> dim == arg.fromDim) || Arrays.stream(dimTo).noneMatch(dim -> dim == arg.toDim))
			return;

		transformAndPostCommand(command, arg);
		System.out.println("Triggered command \"" + command + "\" on player " + arg.player + " moving from dimension " + arg.fromDim + " to " + arg.toDim + ".");
	}

	@Override
	public String transformCommand(String command, PlayerEvent.PlayerChangedDimensionEvent arg) {
		return Transformer.transformPlayer(command, arg.player);
	}
}
