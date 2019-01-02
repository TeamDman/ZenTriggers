package zentriggers.zentriggers.actions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.Transformer;
import zentriggers.zentriggers.TriggerDirector;
import zentriggers.zentriggers.TriggerType;
import zentriggers.zentriggers.ZenTriggers;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.zentriggers.actions.ItemPickupAction")
public class ItemPickupAction implements IAction<PlayerEvent.ItemPickupEvent> {
	public final String          command;
	public final RestrictionType restriction;
	public final ItemStack[]     stackList;

	public ItemPickupAction(String command, ItemStack[] stackList, RestrictionType restriction) {
		this.command = command;
		this.stackList = stackList;
		this.restriction = restriction;
	}

	@ZenMethod
	public static void create(String command, IItemStack[] stackList, String restriction) {
		RestrictionType type;
		switch (restriction.toUpperCase()) {
			case "WHITELIST":
				type = RestrictionType.WHITELIST;
				break;
			case "BLACKLIST":
				type = RestrictionType.BLACKLIST;
				break;
			default:
				ZenTriggers.logger.error("ItemPickupAction restriction type {} is invalid.", restriction);
				return;
		}
		TriggerDirector.addAction(new ItemPickupAction(
				command,
				Arrays.stream(stackList).map(s -> (ItemStack) s.getInternal()).toArray(ItemStack[]::new),
				type
		));
	}

	@Override
	public TriggerType getType() {
		return TriggerType.ITEM_PICKUP;
	}

	@Override
	public void accept(PlayerEvent.ItemPickupEvent arg) {
		// beautiful
		if (restriction == RestrictionType.WHITELIST ?
				Arrays.stream(stackList).noneMatch(s ->
						ItemStack.areItemStacksEqual(s, arg.getStack())) :
				Arrays.stream(stackList).anyMatch(s ->
						ItemStack.areItemStacksEqual(s, arg.getStack())))
			return;
		transformAndPostCommand(command, arg);
	}

	@Override
	public String transformCommand(String command, PlayerEvent.ItemPickupEvent arg) {
		return Transformer.transformItemStack(Transformer.transformPlayer(command, arg.player), arg.getStack());
	}

	enum RestrictionType {
		WHITELIST,
		BLACKLIST
	}
}
