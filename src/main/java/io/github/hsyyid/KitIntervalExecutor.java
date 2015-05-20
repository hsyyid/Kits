package io.github.hsyyid;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitIntervalExecutor implements CommandExecutor {

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException {
		String kitName = args.<String>getOne("kit name").get();
		int interval = args.<Integer>getOne("kit interval").get();
		// Sets the Interval
		Utils.setInterval(interval, kitName);
		src.sendMessage(Texts.of(TextColors.GOLD,"Success! ", TextColors.YELLOW, "The interval was changed!"));
		return CommandResult.success();
	}

}
