package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".PredicateBuilder")
@ZenRegister
public class PredicateBuilder {
	private static final Random rand = new Random();

	static {
		rand.setSeed(1);
	}

	private final ArrayDeque<Predicate<LivingEvent.LivingUpdateEvent>> predicates = new ArrayDeque<>();

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
	public PredicateBuilder isInBlockArea(IBlock block, int expandX, int expandY) {
		return isInBlockArea(block, expandX, expandX, expandY, expandY);
	}

	@ZenMethod
	public PredicateBuilder isInBlockArea(IBlock block, int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = getOffsetPositions(expandXPos, expandXNeg, expandYPos, expandYNeg);
		predicates.push((event) -> offsets.stream().anyMatch(offset ->
				CraftTweakerMC.getBlock(block).equals(
						event.getEntityLiving().world.getBlockState(
								event.getEntityLiving().getPosition().add(offset)).getBlock())));
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

	@ZenMethod
	public PredicateBuilder isInBlockStateArea(IBlockState state, int expandX, int expandY) {
		return isInBlockStateArea(state, expandX, expandX, expandY, expandY);
	}


	@ZenMethod
	public PredicateBuilder isInBlockStateArea(IBlockState state, int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = getOffsetPositions(expandXPos, expandXNeg, expandYPos, expandYNeg);
		predicates.push((event) -> offsets.stream().anyMatch(offset ->
				state.matches(CraftTweakerMC.getBlockState(
						event.getEntityLiving().world.getBlockState(
								event.getEntityLiving().getPosition().add(offset))))));
		return this;
	}

	private static ArrayList<BlockPos> getOffsetPositions(int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = new ArrayList<>();
		for (int x=-expandYNeg; x<=expandXPos; x++)
			for (int dx=-expandXNeg; dx<=expandXPos; dx++)
				for (int y=-expandYNeg; y<=expandYPos; y++)
					offsets.add(new BlockPos(x,y,dx));
		return offsets;
	}

	@ZenMethod
	public PredicateBuilder isInstanceOf(IEntityDefinition entity) {
		predicates.push((event) -> event.getEntityLiving().getName().equals(entity.getName()));
		return this;
	}
}
