package zentriggers.zentriggers.wrappers;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import zentriggers.zentriggers.ZenTriggers;

@ZenRegister
@ZenClass(ZenTriggers.ZEN_PACKAGE + ".events.WorldTickEvent")
public class MCWorldTickEvent {

	private final MCWorld world;

	public MCWorldTickEvent(MCWorld world) {
		this.world = world;
	}

	@ZenGetter("world")
	public MCWorld getWorld() {
		return world;
	}
}
