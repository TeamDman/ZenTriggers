package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.entity.living.LivingEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.function.Predicate;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".PredicateBuilder")
@ZenRegister
public class PredicateBuilder {
	private static final Random                                               rand       = new Random();

	static {
		rand.setSeed(1);
	}

	private final        ArrayDeque<Predicate<LivingEvent.LivingUpdateEvent>> predicates = new ArrayDeque<>();

	@ZenMethod
	public static PredicateBuilder create() {
		return new PredicateBuilder();
	}

	public boolean test(LivingEvent.LivingUpdateEvent event) {
		return predicates.stream().allMatch(p -> p.test(event));
	}

	@ZenMethod
	public PredicateBuilder negateLatest() {
		predicates.push(predicates.pop().negate());
		return this;
	}

	@ZenMethod
	public PredicateBuilder isInDimension(int id) {
		predicates.push((event) -> event.getEntityLiving().dimension == id);
		return this;
	}

	@ZenMethod
	public PredicateBuilder isNthTick(int x) {
		predicates.push((event) -> event.getEntity().world.getTotalWorldTime() % x == 0);
		return this;
	}

	@ZenMethod
	public PredicateBuilder isInMaterial(IMaterial material) {
		final Material mat = (Material) material.getInternal();
		predicates.push((event) -> event.getEntityLiving().isInsideOfMaterial(mat));
		return this;
	}

	@ZenMethod
	public PredicateBuilder isInBlock(IBlock block) {
		predicates.push((event) -> CraftTweakerMC.getBlock(block).equals(event.getEntityLiving().world.getBlockState(event.getEntityLiving().getPosition()).getBlock()));
		return this;
	}

	@ZenMethod
	public PredicateBuilder isRandom(double chance) {
		predicates.push((event) -> rand.nextDouble() < chance);
		return this;
	}

	@ZenMethod
	public PredicateBuilder isInBlockState(IBlockState state) {
		predicates.push((event) -> state.matches(CraftTweakerMC.getBlockState(event.getEntityLiving().world.getBlockState(event.getEntityLiving().getPosition()))));
		return this;
	}
}
