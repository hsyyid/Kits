package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.config.KitsConfig;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class KitReloadExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext arg1) throws CommandException
	{
		KitsConfig.getConfig().load();
		src.sendMessage(Text.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "The config was reloaded."));
		return CommandResult.success();
	}
}
