package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class KitResetExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String kit = ctx.<String> getOne("kit").get();

		Kits.remainingTime.forEach((k, v) -> {
			if (v.containsKey(kit))
			{
				v.remove(kit);
			}
		});

		src.sendMessage(Text.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "Reset the player times for specified Kit."));
		return CommandResult.success();
	}
}
