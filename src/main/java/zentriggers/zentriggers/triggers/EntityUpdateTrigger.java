package zentriggers.zentriggers.triggers;

import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.event.entity.living.LivingEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.ZenTriggers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".EntityUpdateTrigger")
@ZenRegister
public class EntityUpdateTrigger  {

	public EntityUpdateTrigger() {

	}

}
