package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;

public class KitReloadExecutor implements CommandExecutor
{
	ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();

	public CommandResult execute(CommandSource src, CommandContext arg1) throws CommandException
	{
		try
		{
			Kits.config = configManager.load();
		}
		catch (IOException e)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The config was not reloaded!"));
		}

		src.sendMessage(Texts.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "The config was reloaded."));
		return CommandResult.success();
	}
}
