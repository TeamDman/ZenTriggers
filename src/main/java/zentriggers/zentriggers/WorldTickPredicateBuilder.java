package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.function.Predicate;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".WorldTickPredicateBuilder")
@ZenRegister
public class WorldTickPredicateBuilder {
	private static final Random rand = new Random();

	static {
		rand.setSeed(1);
	}

	private final ArrayDeque<Predicate<World>> predicates = new ArrayDeque<>();

	@ZenMethod
	public static WorldTickPredicateBuilder create() {
		return new WorldTickPredicateBuilder();
	}

	public boolean test(World world) {
		return predicates.stream().allMatch(p -> p.test(world));
	}

	@ZenMethod
	public WorldTickPredicateBuilder negateLatest() {
		predicates.push(predicates.pop().negate());
		return this;
	}

	@ZenMethod
	public WorldTickPredicateBuilder isNthTick(int x) {
		predicates.push((world) -> world.getTotalWorldTime() % x == 0);
		return this;
	}

	@ZenMethod
	public WorldTickPredicateBuilder isRandom(double chance) {
		predicates.push((world) -> rand.nextDouble() < chance);
		return this;
	}

	@ZenMethod
	public WorldTickPredicateBuilder isRemote() {
		predicates.push((world) -> world.isRemote);
		return this;
	}

	@ZenMethod
	public WorldTickPredicateBuilder isDay() {
		predicates.push(World::isDaytime);
		return this;
	}
}
