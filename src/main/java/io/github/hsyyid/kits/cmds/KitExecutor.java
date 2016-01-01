package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KitExecutor implements CommandExecutor
{
	private String kit;

	public KitExecutor(String kit)
	{
		this.kit = kit;
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Game game = Kits.game;
		Scheduler scheduler = Sponge.getGame().getScheduler();

		if (src instanceof Player)
		{
			final Player player = (Player) src;

			if (!ConfigManager.isPlayerInConfig(player.getUniqueId(), kit))
				ConfigManager.addPlayerToConfig(player.getUniqueId(), kit);

			List<String> items = ConfigManager.getItems(kit);

			if (items.size() == 0)
			{
				player.sendMessage(Text.of(TextColors.RED, "Error: ", TextColors.DARK_RED, "The specified kit was not found, or there was an error retrieving data from it."));
				return CommandResult.success();
			}

			if (ConfigManager.canUseKit(player.getUniqueId(), kit))
			{
				// Give Player their Kit
				for (String i : items)
				{
					game.getCommandManager().process(game.getServer().getConsole(), "minecraft:give" + " " + player.getName() + " " + i);
				}

				ConfigManager.setFalse(player.getUniqueId(), kit);

				if (ConfigManager.getInterval(kit) instanceof Integer)
				{
					long val = (Integer) ConfigManager.getInterval(kit);
					ConfigManager.setTimeRemaining(player, kit, val);

					final Task updateTask = scheduler.createTaskBuilder().execute(new Runnable()
					{
						public void run()
						{
							if (ConfigManager.getTimeRemaining(player, kit) > 0)
								ConfigManager.setTimeRemaining(player, kit, ConfigManager.getTimeRemaining(player, kit) - 1);
						}
					}).interval(1, TimeUnit.SECONDS).name("Kits - Counts remaining time").submit(game.getPluginManager().getPlugin("Kits").get().getInstance().get());

					scheduler.createTaskBuilder().execute(new Runnable()
					{
						public void run()
						{
							ConfigManager.setTrue(player, kit);
							Sponge.getScheduler().getScheduledTasks().remove(updateTask);
						}
					}).delay(val, TimeUnit.SECONDS).name("Kits - Sets Value Back to True").submit(game.getPluginManager().getPlugin("Kits").get().getInstance().get());
				}
			}
			else
			{
				if (ConfigManager.getInterval(kit) instanceof Integer)
				{
					if (ConfigManager.getTimeRemaining(player, kit) > 0)
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait " + ConfigManager.getTimeRemaining(player, kit) + " seconds before using this Kit again!"));
					else
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait before using this Kit again!"));
				}
				else if (ConfigManager.getInterval(kit) instanceof Boolean)
				{
					src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "This Kit is one-time use only!"));
				}
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Text.of("Must be an in-game player to use /kit!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of("Must be an in-game player to use /kit!"));
		}

		return CommandResult.success();
	}
}
