package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Main;

import java.io.IOException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitDeleteExecutor implements CommandExecutor {

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException {
		//Get the name of the Kit to Delete
		String name = args.<String>getOne("kit name").get();
		//Get Config Manager
		ConfigurationLoader<CommentedConfigurationNode> configManager = Main.getConfigManager();
		//Get Kit Node
		ConfigurationNode kitNode = Main.config.getNode((Object[]) ("kits.kits").split("\\."));
		//Get Value of Kits Node
		String kits = kitNode.getString();
		//Remove Kit from Kits Node
		String newVal = kits.replace(name + ",","");
		kitNode.setValue(newVal);
		//Save CONFIG
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
		    System.out.println("[KITS]: Failed to delete kit " + name);
		    src.sendMessage(Texts.of(TextColors.DARK_RED,"Error! ", TextColors.RED, "The kit was not deleted successfully!"));
		}
		//Get Item Node
		ConfigurationNode itemNode = Main.config.getNode((Object[]) ("kits.").split("\\."));
		//Remove Kit, child of itemNode
		itemNode.removeChild(name);
		//save config
		try {
			configManager.save(Main.config);
			configManager.load();
		} catch(IOException e) {
			System.out.println("[KITS]: Failed to remove kit " + name);
			src.sendMessage(Texts.of(TextColors.DARK_RED,"Error! ", TextColors.RED, "The kit was not deleted successfully!"));
		}
		src.sendMessage(Texts.of(TextColors.GREEN,"Success: ", TextColors.YELLOW, "The kit was deleted!"));
		return CommandResult.success();
	}

}
