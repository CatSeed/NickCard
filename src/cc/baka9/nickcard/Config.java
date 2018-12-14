package cc.baka9.nickcard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static List<String> blacks;
	public static String prefix;
	public static String suffix;
	public static int maxLeng;

	public static void load() {
		NickCard.instance.saveDefaultConfig();
		FileConfiguration config = NickCard.instance.getConfig();
		blacks = config.getStringList("blacks");
		blacks = blacks == null ? new ArrayList<String>() : blacks;
		prefix = config.getString("prefix");
		suffix = config.getString("suffix");
		maxLeng = config.getInt("maxLeng");
	}

	public static void reload() {
		NickCard.instance.reloadConfig();
		load();
	}

}
