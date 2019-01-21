package zentriggers.zentriggers.wrappers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.ILivingEvent;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.event.entity.living.LivingEvent;
import stanhebben.zenscript.annotations.ZenClass;
import zentriggers.zentriggers.ZenTriggers;

@ZenRegister
@ZenClass(ZenTriggers.ZEN_PACKAGE+".events.EntityLivingUpdateEvent")
public class MCLivingUpdateEvent implements ILivingEvent {
	private final LivingEvent.LivingUpdateEvent event;

	public MCLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		this.event = event;
	}

	@Override
	public IEntityLivingBase getEntityLivingBase() {
		return CraftTweakerMC.getIEntityLivingBase(event.getEntityLiving());
	}
}
