package io.github.hsyyid;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class IntervalTask extends TimerTask {
	String playerName = "";
	String kitName = "";
	public IntervalTask(String userName, String kit){
		playerName = userName;
		kitName = kit;
	}
	@Override
	public void run() {
			ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
				Main.config.getNode("players", playerName, kitName, "usable").setValue("true");
				try {
					configManager.save(Main.config);
					configManager.load();
				} catch(IOException e) {
				    System.out.println("[KITS]: Failed to save config!");
				}
	}
	public static boolean canUse(String name, String k){
		return Utils.canUse(name, k);
	}
}
