package zentriggers.zentriggers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import scala.util.matching.Regex;

public class Transformer {
	enum Transformable {
		PLAYER("$player"),
		ITEMSTACK("$itemstack");
		String regex;
		Transformable(String regex) {
			this.regex = Regex.quote(regex);
		}
	}
	public static String transformPlayer(String command, EntityPlayer player) {
		return command.replaceAll(Transformable.PLAYER.regex, player.getName());
	}
	public static String transformItemStack(String command, ItemStack stack) {
		if (stack.getItem().getRegistryName() == null)
			return command;
		return command.replaceAll(Transformable.ITEMSTACK.regex, stack.getItem().getRegistryName().toString());
	}
}
