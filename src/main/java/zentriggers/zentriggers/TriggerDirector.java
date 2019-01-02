package zentriggers.zentriggers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zentriggers.zentriggers.actions.IAction;

public class TriggerDirector {
	public static final Multimap<TriggerType, IAction> actions = ArrayListMultimap.create();

	public static void addAction(IAction action) {
		actions.put(action.getType(), action);
	}

	public static void handleTrigger(TriggerType type, Object arg) {
		if (type.argType.isInstance(arg)) {
			System.out.println("Trigger " + type + " was invoked with " + arg +" (an invalid argument!)");
			return;
		}
		//noinspection unchecked
		actions.get(type).forEach(action -> action.accept(arg));
	}
}
