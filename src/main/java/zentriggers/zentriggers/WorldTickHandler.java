package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.util.IEventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.wrappers.MCWorld;
import zentriggers.zentriggers.wrappers.MCWorldTickEvent;

import java.util.HashMap;
import java.util.Map;


@ZenClass(ZenTriggers.ZEN_PACKAGE + ".WorldTickHandler")
@ZenRegister
@Mod.EventBusSubscriber
public class WorldTickHandler {
	private static Map<WorldTickPredicateBuilder, IEventHandler<MCWorldTickEvent>> rawHandlers = new HashMap<>();
	private static int                                                             rawInterval = 10;

	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent e) {
		if (e.phase != TickEvent.Phase.START)
			return;
		if (e.world == null)
			return;
		if (e.world.getTotalWorldTime() % rawInterval != 0)
			return;
		if (rawHandlers.isEmpty())
			return;
		rawHandlers.forEach((predicates, handler) -> {
			if (predicates.test(e.world)) {
				try {
					handler.handle(new MCWorldTickEvent(new MCWorld(e.world)));
				} catch (Throwable t) {
					System.out.println("Error occurred invoking raw handler for onTick");
					t.printStackTrace();
				}
			}
		});
	}


	@ZenMethod
	public static void onWorldTick(WorldTickPredicateBuilder builder, IEventHandler<MCWorldTickEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers event, skipping. [Builder: " + builder + ", Handler: " + handler + "]");
		rawHandlers.put(builder, handler);
	}

	@ZenMethod
	public static void setRawInterval(int interval) {
		rawInterval = interval;
	}
}