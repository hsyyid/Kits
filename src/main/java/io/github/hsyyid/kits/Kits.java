package io.github.hsyyid.kits;

import com.google.inject.Inject;
import io.github.hsyyid.kits.cmds.KitAddExecutor;
import io.github.hsyyid.kits.cmds.KitDeleteExecutor;
import io.github.hsyyid.kits.cmds.KitExecutor;
import io.github.hsyyid.kits.cmds.KitIntervalExecutor;
import io.github.hsyyid.kits.cmds.KitListExecutor;
import io.github.hsyyid.kits.cmds.KitReloadExecutor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Plugin(id = "Kits", name = "Kits", version = "0.9")
public class Kits
{
	public static List<String> allKits = new ArrayList<String>();
	public static ItemStack.Builder itemBuilder;
	public static ConfigurationNode config;
	public static ConfigurationNode intervalConfig;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static Game game;

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File defaultConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	@Listener
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("Kits loading...");

		game = event.getGame();

		GameRegistry registry = game.getRegistry();
		itemBuilder = registry.createBuilder(ItemStack.Builder.class);

		// Config File
		try
		{
			if (!defaultConfig.exists())
			{
				defaultConfig.createNewFile();
				config = configManager.load();
				config.getNode("kits", "kits").setValue("default,");
				config.getNode("kits", "default", "item").setValue("diamond_axe,");
				config.getNode("kits", "default", "interval").setValue(30000);
				configManager.save(config);
			}
			configurationManager = configManager;
			config = configManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		// Get Kit Names
		ConfigurationNode kits = config.getNode((Object[]) "kits.kits".split("\\."));
		String kit = kits.getString("default");

		boolean finished = false;
		// Array List to Keep all the Kits in
		ArrayList<String> kitList = new ArrayList<String>();

		// Add all kits to kitList
		if (finished != true)
		{
			int endIndex = kit.indexOf(",");
			if (endIndex != -1)
			{
				String substring = kit.substring(0, endIndex);
				kitList.add(substring);

				// If they Have More than 1
				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = kit.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = kit.substring(startIndex + 1, endIndex);
						kitList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				kitList.add(kit);
				finished = true;
			}
		}

		for (String k : kitList)
		{
			allKits.add(k);
		}

		for (String k : kitList)
		{
			if (getItems(k) != null)
			{
				;
			}
			else
			{
				config.getNode("kits", k, "item").setValue("diamond_axe");
				try
				{
					configManager.save(config);
				}
				catch (IOException e)
				{
					getLogger().error("The default configuration could not be loaded or created!");
				}
			}
		}

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		for (String k : kitList)
		{
			subcommands.put(Arrays.asList(k), CommandSpec.builder()
				.permission("kits.use." + k)
				.description(Texts.of("Kit " + k))
				.executor(new KitExecutor(k))
				.build());
		}

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
				GenericArguments.firstParsing(
					GenericArguments.onlyOne(GenericArguments.integer(Texts.of("kit interval"))),
					GenericArguments.onlyOne(GenericArguments.bool(Texts.of("one-time"))))))
			.executor(new KitIntervalExecutor())
			.extendedDescription(Texts.of("To use /kit interval simply do /kit interval <kit name> <interval|one-time>"))
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

		CommandSpec kitCommandSpec = CommandSpec.builder()
			.extendedDescription(Texts.of("Kit Command"))
			.permission("kits.use")
			.children(subcommands)
			.build();

		game.getCommandDispatcher().register(this, kitCommandSpec, "kit");

		CommandSpec kitsCommandSpec = CommandSpec.builder()
			.extendedDescription(Texts.of("Kits List Command"))
			.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.integer(Texts.of("page number")))))
			.executor(new KitListExecutor())
			.permission("kits.list")
			.build();

		game.getCommandDispatcher().register(this, kitsCommandSpec, "kits");

		getLogger().info("-----------------------------");
		getLogger().info("Kits was made by HassanS6000!");
		getLogger().info("Please post all errors with Kits on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy your Kits! :D");
		getLogger().info("-----------------------------");
		getLogger().info("Kits Loaded!");
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}

	public static String getItems(String kitName)
	{
		ConfigurationNode valueNode = config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		if (items != null)
		{
			return items;
		}
		else
		{
			System.out.println("[Kits]: " + kitName + " does not exist!");
			return null;
		}
	}
}
