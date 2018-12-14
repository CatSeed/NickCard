package cc.baka9.nickcard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ItemUtil {

	public static String toData(ItemStack item) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			BukkitObjectOutputStream output = new BukkitObjectOutputStream(b);
			output.writeObject(item);
			output.close();
			byte[] by = b.toByteArray();
			return ByteToString(by);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ItemStack toItem(String str) {
		try {
			byte[] data = StringToByte(str);
			BukkitObjectInputStream bois = new BukkitObjectInputStream(new ByteArrayInputStream(data));
			ItemStack item = (ItemStack) bois.readObject();
			bois.close();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String ByteToString(byte[] Data) {
		try {
			int iLen = Data.length;
			StringBuffer sb = new StringBuffer(iLen * 2);
			for (int i = 0; i < iLen; i++) {
				int intTmp = Data[i];
				while (intTmp < 0) {
					intTmp += 256;
				}
				if (intTmp < 16) {
					sb.append("0");
				}
				sb.append(Integer.toString(intTmp, 16));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] StringToByte(String Data) {
		try {
			byte[] arrB = Data.getBytes();
			int iLen = arrB.length;
			byte[] arrOut = new byte[iLen / 2];
			for (int i = 0; i < iLen; i += 2) {
				String strTmp = new String(arrB, i, 2);
				arrOut[(i / 2)] = ((byte) Integer.parseInt(strTmp, 16));
			}
			return arrOut;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addItemLore(ItemStack item, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lores = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		for (String line : lines)
			lores.add(line);
		meta.setLore(lores);
		item.setItemMeta(meta);
	}

	public static void setItemLore(ItemStack item, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lores = new ArrayList<String>();
		for (String line : lines)
			lores.add(line);
		meta.setLore(lores);
		item.setItemMeta(meta);
	}

	public static void setItemLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static void removeItemLore(ItemStack item, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lores = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		for (String line : lines)
			lores.remove(line);
		meta.setLore(lores);
		item.setItemMeta(meta);
	}

	public static List<String> getLore(ItemStack item) {
		return item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new ArrayList<String>();

	}

	public static void setItemDisPlayName(ItemStack item, String disPlayName) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(disPlayName);
		item.setItemMeta(meta);
	}

	public static void dropItem(Location loc, ItemStack item) {
		if (item == null) return;
		loc.getWorld().dropItem(loc, item);
	}

	public static int getInventoryItemCount(Inventory inv, ItemStack targetItem) {
		int invTargetItemCount = 0;
		for (ItemStack invItem : inv.getContents()) {
			if (invItem != null && invItem.getType() != Material.AIR && targetItem.isSimilar(invItem))
				invTargetItemCount += invItem.getAmount();
		}
		return invTargetItemCount;
	}

	public static int getPlayerItemCount(Player player, ItemStack targetItem) {
		return getInventoryItemCount(player.getInventory(), targetItem);
	}

	public static boolean consumeInventoryItem(Inventory inv, ItemStack targetItem, int count) {
		if (!inv.containsAtLeast(targetItem, count)) return false;
		for (int i = 0; i < inv.getSize(); i++) {
			if (count == 0) return true;
			ItemStack invItem = inv.getItem(i);
			if (ItemIsEmpty(invItem) || !invItem.isSimilar(targetItem)) continue;
			int surplus = invItem.getAmount() - count;
			count = Math.abs(Math.min(0, surplus));
			invItem.setAmount(Math.max(0, surplus));
			inv.setItem(i, invItem);
		}
		return true;

	}

	@SuppressWarnings("deprecation")
	public static boolean consumePlayerItem(Player player, ItemStack targetItem, int count) {
		boolean result = consumeInventoryItem(player.getInventory(), targetItem, count);
		player.updateInventory();
		return result;
	}

	public static boolean ItemIsEmpty(ItemStack item) {
		return (item == null || item.getType() == Material.AIR);
	}

	public static boolean maxInventory(Inventory inv) {
		for (int a = 0; a < inv.getSize(); a++) {
			ItemStack item = inv.getItem(a);
			if (item == null) { return false; }
		}
		return true;
	}

	public static int getInventoryAirCount(Inventory inv) {
		int count = 0;
		for (int a = 0; a < inv.getSize(); a++) {

			if (inv.getItem(a) == null) count++;
		}
		return count;
	}

	public static int getPlayerAirCount(Player player) {
		return getInventoryAirCount(player.getInventory());
	}

	public static boolean maxInventory(Player p) {
		return maxInventory(p.getInventory());
	}

	public static List<ItemStack> getInventoryItems(Inventory inv) {
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack invItem = inv.getItem(i);
			if (!ItemUtil.ItemIsEmpty(invItem)) {
				itemList.add(invItem);
			}
		}
		return itemList;

	}

}
