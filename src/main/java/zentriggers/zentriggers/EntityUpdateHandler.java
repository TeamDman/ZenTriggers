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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ZenClass(ZenTriggers.ZEN_PACKAGE + ".EntityUpdateHandler")
@ZenRegister
@Mod.EventBusSubscriber
public class EntityUpdateHandler {
	private static Map<EntityUpdatePredicateBuilder, IEventHandler<MCLivingUpdateEvent>> handlers = new HashMap<>();
	private static Map<EntityUpdatePredicateBuilder, IEventHandler<MCLivingUpdateEvent>> rawHandlers = new HashMap<>();
	private static int rawInterval = 10;

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
		List<Entity> entities;
		synchronized (e.world.loadedEntityList) {
			entities = new ArrayList<>(e.world.loadedEntityList);
		}
		entities.forEach(entity -> {
			rawHandlers.forEach((predicates, handler) -> {
				if (!(entity instanceof EntityLivingBase))
					return;
				if (predicates.test(entity)) {
					try {
						handler.handle(new MCLivingUpdateEvent(new LivingEvent.LivingUpdateEvent((EntityLivingBase) entity)));
					} catch (Throwable t) {
						System.out.println("Error occurred invoking raw handler for onEntityUpdateRaw");
						t.printStackTrace();
					}
				}
			});
		});
	}

	@SubscribeEvent
	public static void onEntityUpdateRaw(LivingEvent.LivingUpdateEvent event) {
		handlers.forEach((predicates, handler) -> {
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
	public static void onEntityUpdate(EntityUpdatePredicateBuilder builder, IEventHandler<MCLivingUpdateEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers onEntityUpdate event, skipping. [Builder: " + builder + ", Handler: " +handler + "]");
		handlers.put(builder, handler);
	}

	@ZenMethod
	public static void onEntityUpdateRaw(EntityUpdatePredicateBuilder builder, IEventHandler<MCLivingUpdateEvent> handler) {
		if (builder == null || handler == null)
			System.out.println("Builder or handler null trying to register ZenTriggers onEntityUpdateRaw event, skipping. [Builder: " + builder + ", Handler: " +handler + "]");
		rawHandlers.put(builder, handler);
	}

	@ZenMethod
	public static void setRawInterval(int interval) {
		rawInterval = interval;
	}
}