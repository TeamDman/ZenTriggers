package zentriggers.zentriggers;

import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public enum TriggerType {
	ITEM_PICKUP(PlayerEvent.ItemPickupEvent.class),
	DIMENSION_CHANGE(PlayerEvent.PlayerChangedDimensionEvent.class);

	Class argType;

	TriggerType(Class argType) {
		this.argType = argType;
	}
}
