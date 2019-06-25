package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.util.IEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
	private static Map<Class<? extends LivingEvent.LivingUpdateEvent>, Map<PredicateBuilder, IEventHandler<MCLivingUpdateEvent>>> rawHandlers = new HashMap<>();

	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent e) {
		if (e.world == null)
			return;
		if (rawHandlers.isEmpty())
			return;
		HashMap<IEventHandler<MCLivingUpdateEvent>, Entity> toTick = new HashMap<>();
		e.world.loadedEntityList.forEach(entity -> {
			rawHandlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).forEach((predicates, handler) -> {
				if (!(entity instanceof EntityLivingBase))
					return;
				if (predicates.test(entity)) {
					toTick.put(handler, entity);
				}
			});
		});
		toTick.forEach((handler, entity) -> {
			try {
				handler.handle(new MCLivingUpdateEvent(new LivingEvent.LivingUpdateEvent((EntityLivingBase) entity)));
			} catch (Throwable t) {
				System.out.println("Error occurred invoking raw handler for onEntityUpdateRaw");
				t.printStackTrace();
			}
		});
	}

	@SubscribeEvent
	public static void onEntityUpdateRaw(LivingEvent.LivingUpdateEvent event) {
		handlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).forEach((predicates, handler) -> {
			if (predicates.test(event.getEntity())) {
				try {
					handler.handle(new MCLivingUpdateEvent(event));
				} catch (Throwable t) {
					System.out.println("Error occurred invoking handler for onEntityUpdate");
					t.printStackTrace();
				}
			}
		});
	}



	@ZenMethod
	public static void onEntityUpdate(PredicateBuilder builder, IEventHandler<MCLivingUpdateEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers event, skipping. [Builder: " + builder + ", Handler: " +handler + "]");
		handlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).put(builder, handler);
	}

	@ZenMethod
	public static void onEntityUpdateRaw(PredicateBuilder builder, IEventHandler<MCLivingUpdateEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers event, skipping. [Builder: " + builder + ", Handler: " +handler + "]");
		rawHandlers.computeIfAbsent(LivingEvent.LivingUpdateEvent.class, (k) -> new HashMap<>()).put(builder, handler);
	}
}