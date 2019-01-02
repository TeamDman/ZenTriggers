package zentriggers.zentriggers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class Handler {
	@SubscribeEvent
	public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		TriggerDirector.handleTrigger(TriggerType.DIMENSION_CHANGE, event);
	}
	@SubscribeEvent
	public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
		TriggerDirector.handleTrigger(TriggerType.ITEM_PICKUP, event);
	}
}