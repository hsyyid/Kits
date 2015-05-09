package io.github.hsyyid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.dispatcher.SimpleDispatcher;

import com.google.inject.Inject;

import org.slf4j.Logger;

@Plugin(id = "Kits", name = "Kits", version = "0.1")
public class Main {
	int size = 1;
	static List<String> allKits = new ArrayList<String>();
	//Setting up Plugin Logger.
	static ConfigurationNode config = null;
	static Game game;
	@Inject
	private Logger logger;
	
	public Logger getLogger() {
	    return logger;
	}
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private File defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	@Subscribe
    public void onServerStart(ServerStartedEvent event) {
		getLogger().info("Kits loaded!");
		
		game = event.getGame();
		
		//Get Server
		Server server = event.getGame().getServer();
		
		//Config File
		try {
		     if (!defaultConfig.exists()) {
		        defaultConfig.createNewFile();
		        config = configManager.load();
		        
		        config.getNode("kits", "kits").setValue("default");
		        config.getNode("kits", "default", "item").setValue("diamond_axe");
		        configManager.save(config);
		    }
		    config = configManager.load();

		} catch (IOException exception) {
		    getLogger().error("The default configuration could not be loaded or created!");
		}
		
		//Get Kit Names
		ConfigurationNode kits = config.getNode((Object[]) "kits.kits".split("\\."));
		String kit = kits.getString("default");
		
		boolean finished = false;
		//Array List to Keep all the Kits in
		ArrayList<String> kitList = new ArrayList<String>();
		//Add all kits to kitList
		if(finished != true){
			int endIndex = kit.indexOf(",");
			if(endIndex != -1){
				String substring = kit.substring(0, endIndex);
				kitList.add(substring);
				
				//If they Have More than 1
				while(finished != true){
					int startIndex = endIndex;
					endIndex = kit.indexOf(",", startIndex + 1);
					if(endIndex != -1){
						String substrings = kit.substring(startIndex+1, endIndex);
						kitList.add(substrings);
					}
					else{
						finished = true;
					}
				}
			}
			else{
				kitList.add(kit);
				finished = true;
			}
		}
		//Adding it to the other lits for the /kits command.
		for(String k : kitList){
			allKits.add(k);
		}
		//Get how many kits are in it.
		int currentSize = kitList.size();	
		//Add All Kits to Config
		if(size < currentSize){
			for (String k: kitList)
			{
				config.getNode("kits", k, "item").setValue("diamond_axe");
				try {
				configManager.save(config);
				} catch (IOException e) {
					getLogger().error("The default configuration could not be loaded or created!");
				}
			}
		size = currentSize;
		
		}
		
		//Register /kit Command
        CommandService cmdService = event.getGame().getCommandDispatcher();
        cmdService.register(this, new Command(server), "kit");
        cmdService.register(this, new ListCommand(server), "kits");
	}
	public static String getItems(String kitName){
		ConfigurationNode valueNode = config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		if(items != null){
			return items;
		}
		else{
			System.out.println("[KIT]: " + kitName + " does not exist!");
			return null;
		}
	}
}
