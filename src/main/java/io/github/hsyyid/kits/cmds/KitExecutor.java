package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Main;
import io.github.hsyyid.kits.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.service.scheduler.TaskBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitExecutor implements CommandExecutor
{
	String kit;
	ItemStackBuilder builder = Main.ItemBuilder;
	double timeRemaining;

	public KitExecutor(String kitName)
	{
		kit = kitName;
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Game game = Main.game;
		SchedulerService scheduler = game.getScheduler();
		TaskBuilder taskBuilder = scheduler.createTaskBuilder();
		TaskBuilder taskBuilder2 = scheduler.createTaskBuilder();

		if (src instanceof Player)
		{
			final Player player = (Player) src;
			Utils.addConfig(player.getUniqueId(), kit);

			String items = "";
			if (Main.getItems(kit) != null)
			{
				items = Main.getItems(kit);
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.RED, "Error! ", TextColors.DARK_RED, "The specified kit was not found, or there was an error retrieving data from it."));
				return CommandResult.success();
			}

			boolean finished = false;
			ArrayList<String> itemList = new ArrayList<String>();

			if (finished != true)
			{
				int endIndex = items.indexOf(",");
				if (endIndex != -1)
				{
					String substring = items.substring(0, endIndex);
					itemList.add(substring);

					while (finished != true)
					{
						int startIndex = endIndex;
						endIndex = items.indexOf(",", startIndex + 1);
						if (endIndex != -1)
						{
							String substrings = items.substring(startIndex + 1, endIndex);
							itemList.add(substrings);
						}
						else
						{
							finished = true;
						}
					}
				}
				else
				{
					itemList.add(items);
					finished = true;
				}
			}

			if (Utils.canUse(player.getUniqueId(), kit))
			{
				// Give Player their Kit
				for (String i : itemList)
				{
					game.getCommandDispatcher().process(game.getServer().getConsole(), "give" + " " + player.getName() + " " + i);
				}

				Utils.setFalse(player.getUniqueId(), kit);

				if (Utils.getInterval(kit) instanceof Integer)
				{
					long val = (Integer) Utils.getInterval(kit);
					timeRemaining = val * 0.001;

					taskBuilder.execute(new Runnable()
					{
						public void run()
						{
							ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
							Main.config.getNode("players", player.getUniqueId().toString(), kit, "usable").setValue("true");
							try
							{
								configManager.save(Main.config);
								configManager.load();
							}
							catch (IOException e)
							{
								System.out.println("[KITS]: Failed to save config!");
							}
						}
					}).delay(val, TimeUnit.MILLISECONDS).name("Kits - Sets Value Back to True").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());

					taskBuilder2.execute(new Runnable()
					{
						public void run()
						{
							if (timeRemaining > 0)
							{
								timeRemaining = timeRemaining - 1;
							}
						}
					}).interval(1, TimeUnit.SECONDS).name("Kits - Counts remaining time").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());
				}
			}
			else
			{
				if (Utils.getInterval(kit) instanceof Integer)
				{
					if (timeRemaining > 0)
						src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait " + timeRemaining + " seconds before using this Kit again!"));
					else
						src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait before using this Kit again!"));
				}
				else if (Utils.getInterval(kit) instanceof Boolean)
				{
					src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "This Kit is one-time use only!"));
				}
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
		}

		return CommandResult.success();
	}
}
