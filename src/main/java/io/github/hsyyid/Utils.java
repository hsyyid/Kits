package io.github.hsyyid;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.service.config.DefaultConfig;

import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Utils {

	public static void addItem(String kitName, String item){
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode valueNode = Main.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		String formattedItem = (item + ",");
		valueNode.setValue(items + formattedItem);
		try {
			configManager.save(Main.config);
		} catch(IOException e) {
		    System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
	}
	
	public static void addKit(String kitName, String item){
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		ConfigurationNode kitNode = Main.config.getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String formattedKitName = (kitName + ",");
		kitNode.setValue(kits + formattedKitName);
		try {
			configManager.save(Main.config);
		} catch(IOException e) {
		    System.out.println("[KITS]: Failed to add kit " + kitName);
		}
		ConfigurationNode itemNode = Main.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		itemNode.setValue(item);
		try {
			configManager.save(Main.config);
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
	}
}
