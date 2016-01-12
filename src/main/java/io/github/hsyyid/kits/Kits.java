package io.github.hsyyid.kits;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.github.hsyyid.kits.cmds.KitAddExecutor;
import io.github.hsyyid.kits.cmds.KitDeleteExecutor;
import io.github.hsyyid.kits.cmds.KitExecutor;
import io.github.hsyyid.kits.cmds.KitIntervalExecutor;
import io.github.hsyyid.kits.cmds.KitListExecutor;
import io.github.hsyyid.kits.cmds.KitReloadExecutor;
import io.github.hsyyid.kits.utils.ConfigManager;
import io.github.hsyyid.kits.utils.Utils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Plugin(id = "Kits", name = "Kits", version = "1.3")
public class Kits
{
	public static List<String> allKits = Lists.newArrayList();
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

		game = Sponge.getGame();

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
				config.getNode("kits", "default", "interval").setValue(300);
				configManager.save(config);
			}
			configurationManager = configManager;
			config = configManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();
		
		List<String> kitList = ConfigManager.getKits();
		
		for (String k : kitList)
		{
			Kits.allKits.add(k);
		}
		
		for (String k : kitList)
		{
			subcommands.put(Arrays.asList(k), CommandSpec.builder()
				.permission("kits.use." + k)
				.description(Text.of("Kit " + k))
				.executor(new KitExecutor(k))
				.build());
		}

		subcommands.put(Arrays.asList("add"), CommandSpec.builder()
			.permission("kits.add")
			.description(Text.of("Add a Kit or Item to a Kit"))
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Text.of("kit name"))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("item")))),
				GenericArguments.onlyOne(GenericArguments.integer(Text.of("number of items"))),
				GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.integer(Text.of("item subtype")))))
			.executor(new KitAddExecutor())
			.extendedDescription(Text.of("To use /kit add please do /kit add <kit name> <item id>"))
			.build());

		subcommands.put(Arrays.asList("interval"), CommandSpec.builder()
			.permission("kits.interval")
			.description(Text.of("Change a Kit's Interval"))
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Text.of("kit name"))),
				GenericArguments.firstParsing(
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("kit interval"))),
					GenericArguments.onlyOne(GenericArguments.bool(Text.of("one-time"))))))
			.executor(new KitIntervalExecutor())
			.extendedDescription(Text.of("To use /kit interval simply do /kit interval <kit name> <interval|one-time>"))
			.build());

		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
			.permission("kits.delete")
			.description(Text.of("Delete a Kit from the Config"))
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Text.of("kit name")))))
			.executor(new KitDeleteExecutor())
			.extendedDescription(Text.of("To use /kit delete simply do /kit delete <kit name>"))
			.build());

		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
			.permission("kits.reload")
			.description(Text.of("Reload the Kits Config"))
			.executor(new KitReloadExecutor())
			.extendedDescription(Text.of("To reload the config, simply do /kit reload"))
			.build());

		CommandSpec kitCommandSpec = CommandSpec.builder()
			.extendedDescription(Text.of("Kit Command"))
			.permission("kits.use")
			.children(subcommands)
			.build();

		game.getCommandManager().register(this, kitCommandSpec, "kit");

		CommandSpec kitsCommandSpec = CommandSpec.builder()
			.extendedDescription(Text.of("Kits List Command"))
			.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.integer(Text.of("page number")))))
			.executor(new KitListExecutor())
			.permission("kits.list")
			.build();

		game.getCommandManager().register(this, kitsCommandSpec, "kits");
		
		Utils.restartTasks();

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
}
