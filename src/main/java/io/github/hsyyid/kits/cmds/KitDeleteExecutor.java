package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class KitDeleteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String> getOne("kit name").get();
		ConfigManager.deleteKit(name);
		src.sendMessage(Text.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "The kit was deleted!"));
		return CommandResult.success();
	}
}
