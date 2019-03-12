package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".Profiler")
@ZenRegister
public class Profiler {
	private static final HashMap<String, Long> timestamps = new HashMap<>();

	@ZenMethod
	public static void start(String identifier) {
		timestamps.put(identifier, System.nanoTime());
	}

	@ZenMethod
	public static String finish(String identifier) {
		if (!timestamps.containsKey(identifier))
			return "unknown duration, no timestamp found for the given identifier";
		return Duration.of(System.nanoTime() - timestamps.get(identifier), ChronoUnit.NANOS).toString();
	}
}
