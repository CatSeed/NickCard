package cc.baka9.nickcard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NickCard extends JavaPlugin {
	public static NickCard instance;

	@Override
	public void onEnable() {
		instance = this;
		Config.load();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.isOp()) {
			Config.reload();
			Manages.msg(sender, "插件配置已重新加载");
			return true;
		}
		if (args.length > 0 && args[0].equalsIgnoreCase("get") && sender.isOp() && sender instanceof Player) {
			Player player = (Player) sender;
			player.getInventory().addItem(Manages.getNickCardItem());
			player.updateInventory();
			Manages.msg(sender, "已获取");
			return true;
		}

		if (args.length > 0 && args[0].equalsIgnoreCase("use") && sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = player.getItemInHand();
			if (!Manages.itemIsNickCardItem(item)) {
				Manages.msg(sender, "你手中的物品不是称号卡");
				return true;
			}

			String nick = Manages.getNickCardItemNick(item);
			if (nick.equals("<未命名>")) {
				Manages.msg(sender, "你还没有设置好称号,所以不能使用");
				return true;
			}
			ItemUtil.consumePlayerItem(player, item, 1);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " + player.getName() + " prefix " + nick);
			Manages.msg(sender, "使用成功!");
			player.chat("米纳桑~看看我哒新称号叭!");
			return true;
		}
		if (args.length > 1 && args[0].equalsIgnoreCase("set") && sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = player.getItemInHand();
			if (!Manages.itemIsNickCardItem(item)) {
				Manages.msg(sender, "你手中的物品不是称号卡");
				return true;
			}
			String nick = args[1];
			if (Manages.containsBlack(nick)) {
				Manages.msg(sender, "称号中含有违禁字词,设置失败");
				return true;
			}
			if (Manages.nickAntiLong(nick)) {
				Manages.msg(sender, "称号过长,设置失败");
				return true;
			}
			Manages.setNickCardNick(item, nick);
			Manages.msg(sender, "称号卡称号设置为:" + nick);
			Manages.msg(sender, "§b请查看称号卡,如果满意请输入/nickcard use 来使用");
			return true;
		}
		if (sender.isOp()) {
			Manages.msg(sender, "/nickcard get 获取一张称号纸");
			Manages.msg(sender, "/nickcard reload 重载插件");
		}
		Manages.msg(sender, "/nickcard set [称号] 设置称号");
		Manages.msg(sender, "/nickcard use 使用称号卡");
		return super.onCommand(sender, command, label, args);
	}
}
