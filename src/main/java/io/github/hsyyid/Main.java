package io.github.hsyyid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.spongepowered.api.util.command.args.ChildCommandElementExecutor;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.dispatcher.SimpleDispatcher;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.google.inject.Inject;

import org.slf4j.Logger;

@Plugin(id = "Kits", name = "Kits", version = "0.1")
public class Main {
	static List<String> allKits = new ArrayList<String>();
	//Setting up Plugin Logger.
	static ConfigurationNode config = null;
	static ConfigurationLoader<CommentedConfigurationNode> configurationManager;

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
		        config.getNode("kits", "kits").setValue("default,");
		        config.getNode("kits", "default", "item").setValue("diamond_axe,");
		        configManager.save(config);
		    }
		    configurationManager = configManager;
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
		
		//Add All Kits to Config if Not Added Already.
			for (String k: kitList)
			{
				if(getItems(k) != null){
					//DO NOTHIN
				}
				else{
					config.getNode("kits", k, "item").setValue("diamond_axe");
					try {
						configManager.save(config);
					} catch (IOException e) {
						getLogger().error("The default configuration could not be loaded or created!");
					}
				}
			}
			
		//Sub Commands
		HashMap<List<String>, CommandSpec> subcommands = new HashMap();
        
		for(String k : kitList){
			subcommands.put(Arrays.asList(k), CommandSpec.builder()
			.setPermission("kits.use." + k)
			.setDescription(Texts.of("Kit " + k))
			.setExecutor(new KitExecutor(k))
			.build());
		}
		// /kit add
		subcommands.put(Arrays.asList("add"), CommandSpec.builder()
		        .setPermission("kits.add")
		        .setDescription(Texts.of("Add a Kit or Item to a Kit"))
		        .setArguments(GenericArguments.seq(
		        		GenericArguments.onlyOne(GenericArguments.string(Texts.of("kit name"))),
		                GenericArguments.onlyOne(GenericArguments.string(Texts.of("item")))))
		        .setExecutor(new KitAddExecutor())
		        .setExtendedDescription(Texts.of("To use /kit add please do /kit add <kit name> <item id>"))
		        .build());
		
		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
		        .setPermission("kits.reload")
		        .setDescription(Texts.of("Reload the Kits Config"))
		        .setExecutor(new KitReloadExecutor())
		        .setExtendedDescription(Texts.of("To reload the config, simply do /kit reload"))
		        .build());
		
		//Register /kit Command
		CommandSpec myCommandSpec = CommandSpec.builder()
			    .setDescription(Texts.of("Kits Command"))
			    .setPermission("kits.use")
			    // NOT YET IMPLEMENTED IN SPONGE API .setExecutor(new KitExecutor())
			    .setArguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("kit name"))))
			    .setChildren(subcommands)
			    .build();

			game.getCommandDispatcher().register(this, myCommandSpec, "kit");
			
        CommandService cmdService = event.getGame().getCommandDispatcher();
        cmdService.register(this, new ListCommand(server), "kits");
	}
	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager(){
		return configurationManager;
	}
	public static String getItems(String kitName){
		ConfigurationNode valueNode = config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		if(items != null){
			return items;
		}
		else{
			System.out.println("[KITS]: " + kitName + " does not exist!");
			return null;
		}
	}
}
