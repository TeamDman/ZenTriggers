package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.util.IEventHandler;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.wrappers.MCLivingUpdateEvent;

import java.util.HashMap;
import java.util.Map;


@ZenClass(ZenTriggers.ZEN_PACKAGE + ".Handler")
@ZenRegister
@Mod.EventBusSubscriber
public class Handler {
	private static Map<Class<? extends LivingEvent.LivingUpdateEvent>, Map<PredicateBuilder, IEventHandler<MCLivingUpdateEvent>>> handlers = new HashMap<>();

	@SubscribeEvent
	public static void onEntityUpdateRaw(LivingEvent.LivingUpdateEvent event) {
		handlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).forEach((predicates, handler) -> {
			if (predicates.test(event)) {
				handler.handle(new MCLivingUpdateEvent(event));
			}
		});
	}

	@ZenMethod
	public static void onEntityUpdate(PredicateBuilder builder, IEventHandler<MCLivingUpdateEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers event, skipping. [Builder: " + builder + ", Handler: " +handler + "]");
		handlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).put(builder, handler);
	}
}