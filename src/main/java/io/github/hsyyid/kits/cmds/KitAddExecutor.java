package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Optional;

public class KitAddExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String> getOne("kit name").get();
		String item = args.<String> getOne("item").get();
		int numberOfItem = args.<Integer> getOne("number of items").get();
		Optional<Integer> subtype = args.<Integer> getOne("item subtype");

		String items = Kits.getItems(name);
		String fullCmd = "";
		
		if (subtype.isPresent())
		{
			fullCmd = (item + " " + numberOfItem + " " + subtype.get());
		}
		else
		{
			fullCmd = (item + " " + numberOfItem);
		}
		
		if (items != null)
		{
			ConfigManager.addItemToKit(name, fullCmd);
			src.sendMessage(Texts.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The item was added to kit " + name));
		}
		else
		{
			ConfigManager.addKit(name, fullCmd);
			src.sendMessage(Texts.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The kit was added! ", TextColors.DARK_GRAY, "Don't forget to reboot the server for the kit to show up!"));
		}
		
		return CommandResult.success();
	}

}
