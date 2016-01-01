package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class KitIntervalExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
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
			ConfigManager.setInterval(interval, kitName);
		}
		else if(oneTime)
		{
			ConfigManager.setInterval(oneTime, kitName);
		}
		
		src.sendMessage(Text.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The interval was changed!"));
		return CommandResult.success();
	}
}
