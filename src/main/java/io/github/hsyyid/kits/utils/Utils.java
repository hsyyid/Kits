package io.github.hsyyid.kits.utils;

import io.github.hsyyid.kits.Main;

import java.io.IOException;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.service.scheduler.TaskBuilder;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Utils {
	//Must use Main.config, so that whole config saves - rather than overwriting it!
	public static void addItem(String kitName, String item){
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		String formattedItem = (item + ",");
		valueNode.setValue(items + formattedItem);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
	}
	//Adds Kit + an Item to the Config!
	public static void addKit(String kitName, String item){
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode kitNode = Main.config.getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String formattedKitName = (kitName + ",");
		kitNode.setValue(kits + formattedKitName);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to add kit " + kitName);
		}
		ConfigurationNode itemNode = Main.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String formattedItemName = (item + ",");
		itemNode.setValue(formattedItemName);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
		ConfigurationNode intervalNode = Main.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		intervalNode.setValue(30000);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to set the interval on the kit!");
		}
	}

	//Adds Players to Config
	public static void addConfig(UUID playerName, String kitName)
	{
		final UUID userName = playerName;
		final String kit = kitName;
		Game game = Main.game;
		SchedulerService scheduler = game.getScheduler();
		TaskBuilder taskBuilder = scheduler.getTaskBuilder();
		String name = playerName.toString();
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		Timer t = new Timer();
		if(inConfig(playerName, kitName))
		{
			Task task = taskBuilder.execute(new Runnable() {
				public void run() {
					ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
					Main.config.getNode("players", userName.toString(), kit, "usable").setValue("true");
					try
					{
						configManager.save(Main.config);
						configManager.load();
					}
					catch(IOException e)
					{
						System.out.println("[KITS]: Failed to save config!");
					}
				}
			}).delay(Utils.getInterval(kitName), TimeUnit.MILLISECONDS).name("Kits - Sets Value").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());
		}
		else{
			Main.config.getNode("players", name, kitName, "usable").setValue("true");
			try {
				configManager.save(Main.config);
				configManager.load();
			} catch(IOException e) {
				System.out.println("[KITS]: Failed to add " + name + " to config!");
			}
			Task task = taskBuilder.execute(new Runnable() {
				public void run() {
					ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
					Main.config.getNode("players", userName.toString(), kit, "usable").setValue("true");
					try
					{
						configManager.save(Main.config);
						configManager.load();
					}
					catch(IOException e)
					{
						System.out.println("[KITS]: Failed to save config!");
					}
				}
			}).delay(Utils.getInterval(kitName), TimeUnit.MILLISECONDS).name("Kits - Sets Val Back to True").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());
		}
	}
	//Check if Player is In Config
	public static boolean inConfig(UUID playerName, String kitName)
	{
		String userName = playerName.toString();
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("players." + userName + "." + kitName + ".usable").split("\\."));
		Object inConfig = valueNode.getValue();
		if(inConfig != null){
			return true;
		}
		else{
			return false;
		}
	}
	//Checks if Player can Use It
	public static boolean canUse(UUID playerName, String kitName)
	{
		String userName = playerName.toString();
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("players." + userName + "." + kitName + ".usable").split("\\."));
		boolean b = valueNode.getBoolean();
		return b;
	}
	public static void setFalse(UUID playerName, String kitName)
	{
		String name = playerName.toString();
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("players." + name + "." + kitName + ".usable").split("\\."));
		valueNode.setValue(false);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to save config!");
		}
	}
	//Get Kit Interval
	public static int getInterval(String kitName)
	{
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		int interval = valueNode.getInt();
		return interval;
	}
	//Set Kit Interval
	public static void setInterval(int interval, String kitName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode intervalNode = Main.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		intervalNode.setValue(interval);
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to change the interval for kit " + kitName);
		}

	}
}

