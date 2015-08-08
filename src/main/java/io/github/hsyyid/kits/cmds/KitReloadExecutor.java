package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Main;

import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitReloadExecutor implements CommandExecutor
{
	ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();

	public CommandResult execute(CommandSource src, CommandContext arg1)
		throws CommandException
	{
		try
		{
			configManager.load();
		}
		catch (IOException e)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The config was not reloaded!"));
		}

		src.sendMessage(Texts.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "The config was reloaded."));
		return CommandResult.success();
	}

}
