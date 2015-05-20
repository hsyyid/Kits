package io.github.hsyyid;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import org.spongepowered.api.service.config.DefaultConfig;

import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
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
	public static void addConfig(String name, String kitName){
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		Timer t = new Timer();
		if(inConfig(name, kitName)){
			t.schedule(new IntervalTask(name, kitName), getInterval(kitName));
		}
		else{
			Main.config.getNode("players", name, kitName, "usable").setValue("false");
			try {
				configManager.save(Main.config);
				configManager.load();
			} catch(IOException e) {
			    System.out.println("[KITS]: Failed to add " + name + " to config!");
			}
			t.schedule(new IntervalTask(name, kitName), getInterval(kitName));
		}
	}
	//Check if Player is In Config
	public static boolean inConfig(String userName, String kitName){
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
	public static boolean canUse(String userName, String kitName){
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("players." + userName + "." + kitName + ".usable").split("\\."));
		boolean b = valueNode.getBoolean();
		return b;
	}
	public static void setFalse(String name, String kitName){
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
	public static int getInterval(String kitName){
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		int interval = valueNode.getInt();
		return interval;
	}
	//Set Kit Interval
	public static void setInterval(int interval, String kitName) {
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

