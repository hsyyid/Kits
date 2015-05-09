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
	@Inject
	@DefaultConfig(sharedRoot = true)
	private File defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private static ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	public static void addItem(String kitName, String item){
		ConfigurationNode rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());
		ConfigurationNode valueNode = rootNode.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		String formattedItem = (item + ",");
		valueNode.setValue(items + formattedItem);
		try {
			configManager.save(rootNode);
		} catch(IOException e) {
		    System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
	}
	
	public static void addKit(String kitName, String item){
		ConfigurationNode rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());
		ConfigurationNode kitNode = rootNode.getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String formattedKitName = (kitName + ",");
		kitNode.setValue(kits + formattedKitName);
		try {
			configManager.save(rootNode);
		} catch(IOException e) {
		    System.out.println("[KITS]: Failed to add kit " + kitName);
		}
		ConfigurationNode valueNode = configManager.createEmptyNode(ConfigurationOptions.defaults());
		ConfigurationNode itemNode = valueNode.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		itemNode.setValue(item + ",");
		try {
			configManager.save(valueNode);
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to add " + item + " to kit " + kitName);
		}
	}
}
