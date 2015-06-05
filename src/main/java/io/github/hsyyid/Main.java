package io.github.hsyyid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.google.inject.Inject;

import org.slf4j.Logger;

@Plugin(id = "Kits", name = "Kits", version = "0.4")
public class Main {
	static List<String> allKits = new ArrayList<String>();
	//Setting up Plugin Logger.
	static ItemStackBuilder ItemBuilder = null;
	static ConfigurationNode config = null;
	static ConfigurationNode intervalConfig = null;
	static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	static Game game = null;
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
		getLogger().info("Kits loading...");
		
		game = event.getGame();
		
		GameRegistry registry = game.getRegistry();
		ItemStackBuilder builder = registry.getItemBuilder();
		ItemBuilder = builder;
		
		//Get Server
		Server server = event.getGame().getServer();
		
		//Config File
		try {
		     if (!defaultConfig.exists()) {
		        defaultConfig.createNewFile();
		        config = configManager.load();
		        config.getNode("kits", "kits").setValue("default,");
		        config.getNode("kits", "default", "item").setValue("diamond_axe,");
		        config.getNode("kits", "default", "interval").setValue(30000);
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
			.permission("kits.use." + k)
			.description(Texts.of("Kit " + k))
			.executor(new KitExecutor(k))
			.build());
		}
		// /kit add
		subcommands.put(Arrays.asList("add"), CommandSpec.builder()
		        .permission("kits.add")
		        .description(Texts.of("Add a Kit or Item to a Kit"))
		        .arguments(GenericArguments.seq(
		        		GenericArguments.onlyOne(GenericArguments.string(Texts.of("kit name"))),
		                GenericArguments.onlyOne(GenericArguments.string(Texts.of("item")))),
		                GenericArguments.onlyOne(GenericArguments.integer(Texts.of("number of items"))),
		                GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.integer(Texts.of("item subtype")))))
		        .executor(new KitAddExecutor())
		        .extendedDescription(Texts.of("To use /kit add please do /kit add <kit name> <item id>"))
		        .build());
		
		subcommands.put(Arrays.asList("interval"), CommandSpec.builder()
		        .permission("kits.interval")
		        .description(Texts.of("Change a Kit's Interval"))
		        .arguments(GenericArguments.seq(
		        		GenericArguments.onlyOne(GenericArguments.string(Texts.of("kit name"))),
		        		GenericArguments.onlyOne(GenericArguments.integer(Texts.of("kit interval")))))
		        .executor(new KitIntervalExecutor())
		        .extendedDescription(Texts.of("To use /kit interval simply do /kit interval <kit name> <interval>"))
		        .build());
		
		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
		        .permission("kits.delete")
		        .description(Texts.of("Delete a Kit from the Config"))
		        .arguments(GenericArguments.seq(
		        		GenericArguments.onlyOne(GenericArguments.string(Texts.of("kit name")))))
		        .executor(new KitDeleteExecutor())
		        .extendedDescription(Texts.of("To use /kit delete simply do /kit delete <kit name>"))
		        .build());
		
		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
		        .permission("kits.reload")
		        .description(Texts.of("Reload the Kits Config"))
		        .executor(new KitReloadExecutor())
		        .extendedDescription(Texts.of("To reload the config, simply do /kit reload"))
		        .build());
		
		//Register /kit Command
		CommandSpec myCommandSpec = CommandSpec.builder()
			    .extendedDescription(Texts.of("Kits Command"))
			    .permission("kits.use")
			    // NOT YET IMPLEMENTED IN SPONGE API .setExecutor(new KitExecutor())
			    //.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("help"))))
			    .children(subcommands)
			    .build();

			game.getCommandDispatcher().register(this, myCommandSpec, "kit");
			
        CommandService cmdService = event.getGame().getCommandDispatcher();
        cmdService.register(this, new ListCommand(server), "kits");
        getLogger().info("-----------------------------");
        getLogger().info("Kits was made by HassanS6000!");
        getLogger().info("Please post all errors with Kits on the Sponge Thread or on GitHub!");
        getLogger().info("Have fun, and enjoy your Kits! :D");
        getLogger().info("-----------------------------");
        getLogger().info("Kits Loaded!");
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
