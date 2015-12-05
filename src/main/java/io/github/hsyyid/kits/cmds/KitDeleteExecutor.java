package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import ninja.leaping.configurate.ConfigurationNode;
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

public class KitDeleteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String> getOne("kit name").get();

		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode kitNode = Kits.config.getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String newVal = kits.replace(name + ",", "");
		kitNode.setValue(newVal);

		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[KITS]: Failed to delete kit " + name);
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The kit was not deleted successfully!"));
		}

		ConfigurationNode itemNode = Kits.config.getNode((Object[]) ("kits.").split("\\."));
		itemNode.removeChild(name);

		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[KITS]: Failed to remove kit " + name);
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The kit was not deleted successfully!"));
		}
		
		src.sendMessage(Texts.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "The kit was deleted!"));
		return CommandResult.success();
	}
}
