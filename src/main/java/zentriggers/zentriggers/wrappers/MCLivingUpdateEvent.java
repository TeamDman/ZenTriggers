package zentriggers.zentriggers.wrappers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.event.ILivingEvent;
import crafttweaker.api.event.IPlayerEvent;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import zentriggers.zentriggers.ZenTriggers;

@ZenRegister
@ZenClass(ZenTriggers.ZEN_PACKAGE + ".events.EntityLivingUpdateEvent")
public class MCLivingUpdateEvent implements ILivingEvent, IPlayerEvent {

	private final LivingEvent.LivingUpdateEvent event;

	public MCLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		this.event = event;
	}

	@Override
	public IEntityLivingBase getEntityLivingBase() {
		return CraftTweakerMC.getIEntityLivingBase(event.getEntityLiving());
	}

	@ZenGetter("isPlayer")
	public boolean isPlayer() {
		return getEntity() instanceof EntityPlayer;
	}

	@ZenGetter("player")
	@Nullable
	@Override
	public IPlayer getPlayer() {
		return isPlayer() ? CraftTweakerMC.getIPlayer(((EntityPlayer) getEntity())) : null;
	}
}
