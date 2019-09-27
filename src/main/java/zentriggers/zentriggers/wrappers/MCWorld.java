package zentriggers.zentriggers.wrappers;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;
import zentriggers.zentriggers.ZenTriggers;

@ZenRegister
@ZenClass(ZenTriggers.ZEN_PACKAGE + ".world.World")
public class MCWorld extends crafttweaker.mc1120.world.MCWorld {
	private World world;

	public MCWorld(World world) {
		super(world);
		this.world = world;
	}

	@ZenGetter("worldTime")
	public long getWorldTime() {
		return world.getWorldTime();
	}

	@ZenSetter("worldTime")
	public void setWorldTime(long time) {
		world.setWorldTime(time);
	}

	@ZenGetter("totalWorldTime")
	public long getTotalWorldTime() {
		return world.getTotalWorldTime();
	}

	@ZenSetter("totalWorldTime")
	public void setTotalWorldTime(long time) {
		world.setTotalWorldTime(time);
	}
}
