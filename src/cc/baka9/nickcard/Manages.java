package cc.baka9.nickcard;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class Manages {

	public static void msg(CommandSender sender, String msg) {
		sender.sendMessage("§a[§e称号卡§a]§6" + msg.replace("&", "§"));
	}

	public static ItemStack getNickCardItem() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemUtil.setItemLore(item, "§b自定义称号卡", "<未命名>", "§3设置称号指令: /nickcard set 称号", "§3颜色代码:",
				"§1&1§2&2§3&3§4&4§5&5§6&6§7&7§8&8§9&9§0&0§a&a§b&b§c&c§d&d§e&e§f&f");
		return item;
	}

	public static boolean itemIsNickCardItem(ItemStack item) {
		if (ItemUtil.ItemIsEmpty(item)) return false;
		List<String> lore = ItemUtil.getLore(item);
		if (lore.size() >= 2 && lore.get(0).equals("§b自定义称号卡")) return true;
		return false;
	}

	public static String getNickCardItemNick(ItemStack item) {
		return ItemUtil.getLore(item).get(1);
	}

	public static boolean containsBlack(String str) {
		for (String black : Config.blacks) {
			if (str.toLowerCase().contains(black.toLowerCase())) return true;
		}
		return false;
	}

	public static boolean nickAntiLong(String str) {
		return (str.length() > Config.maxLeng * 3) || (getNickLength(str) > Config.maxLeng);
	}

	private static int getNickLength(String str) {
		return str.replace("&", "§").replaceAll("§[0-9abcdefroklnmABCDEFROKLNM]", "").length();
	}

	public static void setNickCardNick(ItemStack item, String nick) {
		List<String> lore = ItemUtil.getLore(item);
		nick = Config.prefix + nick + Config.suffix;
		lore.set(1, nick.replace("&", "§"));
		ItemUtil.setItemLore(item, lore);
	}

}
