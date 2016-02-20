package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.ConfigManager;
import io.github.hsyyid.kits.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class KitExecutor implements CommandExecutor
{
	private String kit;

	public KitExecutor(String kit)
	{
		this.kit = kit;
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Optional<Player> target = args.<Player> getOne("target");

		if (!target.isPresent())
		{
			if (src instanceof Player)
			{
				final Player player = (Player) src;

				if (!ConfigManager.isPlayerInConfig(player.getUniqueId(), kit))
					ConfigManager.addPlayerToConfig(player.getUniqueId(), kit);

				List<String> items = ConfigManager.getItems(kit);

				if (items.size() == 0)
				{
					player.sendMessage(Text.of(TextColors.RED, "Error! ", TextColors.DARK_RED, "The specified kit was not found, or there was an error retrieving data from it."));
					return CommandResult.success();
				}

				if (ConfigManager.canUseKit(player.getUniqueId(), kit))
				{
					Utils.givePlayerKit(player, items);
					ConfigManager.setFalse(player.getUniqueId(), kit);

					if (ConfigManager.getInterval(kit) instanceof Integer)
					{
						long val = (Integer) ConfigManager.getInterval(kit);
						ConfigManager.setTimeRemaining(player.getUniqueId(), kit, val);
						Utils.scheduleUpdateTask(player.getUniqueId(), kit);
						Utils.scheduleValueChangeTask(player.getUniqueId(), kit, val);
					}
				}
				else
				{
					if (ConfigManager.getInterval(kit) instanceof Integer)
					{
						if (ConfigManager.getTimeRemaining(player.getUniqueId(), kit) > 0)
							src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait " + ConfigManager.getTimeRemaining(player.getUniqueId(), kit) + " " + ConfigManager.getTimeUnit().name().toLowerCase() + " before using this Kit again!"));
						else
							src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait before using this Kit again!"));
					}
					else if (ConfigManager.getInterval(kit) instanceof Boolean)
					{
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "This Kit is one-time use only!"));
					}
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /kit!"));
			}
		}
		else if (src.hasPermission("kits.use.others"))
		{
			Player player = target.get();
			List<String> items = ConfigManager.getItems(kit);
			Utils.givePlayerKit(player, items);
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to give other players kits!"));
		}

		return CommandResult.success();
	}
}
