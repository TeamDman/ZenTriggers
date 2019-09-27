package zentriggers.zentriggers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

@ZenClass(ZenTriggers.ZEN_PACKAGE + ".EntityUpdatePredicateBuilder")
@ZenRegister
public class EntityUpdatePredicateBuilder {
	private static final Random rand = new Random();

	static {
		rand.setSeed(1);
	}

	private final ArrayDeque<Predicate<Entity>> predicates = new ArrayDeque<>();

	@ZenMethod
	public static EntityUpdatePredicateBuilder create() {
		return new EntityUpdatePredicateBuilder();
	}

	public boolean test(Entity entity) {
		return predicates.stream().allMatch(p -> p.test(entity));
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder negateLatest() {
		predicates.push(predicates.pop().negate());
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInDimension(int id) {
		predicates.push((entity) -> entity.dimension == id);
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isNthTick(int x) {
		predicates.push((entity) -> entity.world.getTotalWorldTime() % x == 0);
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInMaterial(IMaterial material) {
		final Material mat = (Material) material.getInternal();
		predicates.push((entity) -> entity.isInsideOfMaterial(mat));
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlock(IBlock block) {
		predicates.push((entity) -> CraftTweakerMC.getBlock(block).equals(entity.world.getBlockState(entity.getPosition()).getBlock()));
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlockArea(IBlock block, int expandX, int expandY) {
		return isInBlockArea(block, expandX, expandX, expandY, expandY);
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlockArea(IBlock block, int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = getOffsetPositions(expandXPos, expandXNeg, expandYPos, expandYNeg);
		predicates.push((entity) -> offsets.stream().anyMatch(offset ->
				CraftTweakerMC.getBlock(block).equals(
						entity.world.getBlockState(
								entity.getPosition().add(offset)).getBlock())));
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isRandom(double chance) {
		predicates.push((event) -> rand.nextDouble() < chance);
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isRemote() {
		predicates.push((entity) -> entity.getEntityWorld().isRemote);
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlockState(IBlockState state) {
		predicates.push((entity) -> state.matches(CraftTweakerMC.getBlockState(entity.world.getBlockState(entity.getPosition()))));
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlockStateArea(IBlockState state, int expandX, int expandY) {
		return isInBlockStateArea(state, expandX, expandX, expandY, expandY);
	}


	@ZenMethod
	public EntityUpdatePredicateBuilder isInBlockStateArea(IBlockState state, int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = getOffsetPositions(expandXPos, expandXNeg, expandYPos, expandYNeg);
		predicates.push((entity) -> offsets.stream().anyMatch(offset ->
				state.matches(CraftTweakerMC.getBlockState(
						entity.world.getBlockState(
								entity.getPosition().add(offset))))));
		return this;
	}

	private static ArrayList<BlockPos> getOffsetPositions(int expandXPos, int expandXNeg, int expandYPos, int expandYNeg) {
		ArrayList<BlockPos> offsets = new ArrayList<>();
		for (int x = -expandYNeg; x <= expandXPos; x++)
			for (int dx = -expandXNeg; dx <= expandXPos; dx++)
				for (int y = -expandYNeg; y <= expandYPos; y++)
					offsets.add(new BlockPos(x, y, dx));
		return offsets;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isNotNull() {
		predicates.push((entity) -> entity != null && entity.world != null);
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInstanceOf(IEntityDefinition definition) {
		if (definition == null || definition.getName() == null) {
			System.out.println("Provided entity is null or has no name, can't add isInstanceOf to the chain!");
			predicates.push((__) -> false);
			return this;
		}

		//noinspection ConstantConditions
		predicates.push((entity) -> EntityList.getKey(entity) != null && definition.getId().equalsIgnoreCase(EntityList.getKey(entity).toString()));
		return this;
	}

	@ZenMethod
	public EntityUpdatePredicateBuilder isInstanceOf(String name) {
		//noinspection ConstantConditions
		predicates.push((entity) -> EntityList.getKey(entity) != null && EntityList.getKey(entity).toString().equalsIgnoreCase(name));
		return this;
	}
}
