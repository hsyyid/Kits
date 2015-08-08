package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.Utils;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitIntervalExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args)
		throws CommandException
	{
		String kitName = args.<String> getOne("kit name").get();
		boolean oneTime = false;
		int interval = 0;
		
		if(args.<Integer> getOne("kit interval").isPresent())
		{
			interval = args.<Integer> getOne("kit interval").get();
		}
		else if(args.<Boolean> getOne("one-time").isPresent())
		{
			oneTime = true;
		}
		
		// Sets the Interval
		if(!oneTime)
		{
			Utils.setInterval(interval, kitName);
		}
		else if(oneTime)
		{
			Utils.setInterval(kitName, oneTime);
		}
		
		src.sendMessage(Texts.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The interval was changed!"));
		return CommandResult.success();
	}
}
