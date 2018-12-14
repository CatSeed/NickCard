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
			Manages.msg(sender, "������������¼���");
			return true;
		}
		if (args.length > 0 && args[0].equalsIgnoreCase("get") && sender.isOp() && sender instanceof Player) {
			Player player = (Player) sender;
			player.getInventory().addItem(Manages.getNickCardItem());
			player.updateInventory();
			Manages.msg(sender, "�ѻ�ȡ");
			return true;
		}

		if (args.length > 0 && args[0].equalsIgnoreCase("use") && sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = player.getItemInHand();
			if (!Manages.itemIsNickCardItem(item)) {
				Manages.msg(sender, "�����е���Ʒ���ǳƺſ�");
				return true;
			}

			String nick = Manages.getNickCardItemNick(item);
			if (nick.equals("<δ����>")) {
				Manages.msg(sender, "�㻹û�����úóƺ�,���Բ���ʹ��");
				return true;
			}
			ItemUtil.consumePlayerItem(player, item, 1);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " + player.getName() + " prefix " + nick);
			Manages.msg(sender, "ʹ�óɹ�!");
			player.chat("����ɣ~���������³ƺŰ�!");
			return true;
		}
		if (args.length > 1 && args[0].equalsIgnoreCase("set") && sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = player.getItemInHand();
			if (!Manages.itemIsNickCardItem(item)) {
				Manages.msg(sender, "�����е���Ʒ���ǳƺſ�");
				return true;
			}
			String nick = args[1];
			if (Manages.containsBlack(nick)) {
				Manages.msg(sender, "�ƺ��к���Υ���ִ�,����ʧ��");
				return true;
			}
			if (Manages.nickAntiLong(nick)) {
				Manages.msg(sender, "�ƺŹ���,����ʧ��");
				return true;
			}
			Manages.setNickCardNick(item, nick);
			Manages.msg(sender, "�ƺſ��ƺ�����Ϊ:" + nick);
			Manages.msg(sender, "��b��鿴�ƺſ�,�������������/nickcard use ��ʹ��");
			return true;
		}
		if (sender.isOp()) {
			Manages.msg(sender, "/nickcard get ��ȡһ�ųƺ�ֽ");
			Manages.msg(sender, "/nickcard reload ���ز��");
		}
		Manages.msg(sender, "/nickcard set [�ƺ�] ���óƺ�");
		Manages.msg(sender, "/nickcard use ʹ�óƺſ�");
		return super.onCommand(sender, command, label, args);
	}
}
