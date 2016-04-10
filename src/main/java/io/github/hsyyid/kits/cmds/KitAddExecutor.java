package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class KitAddExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String> getOne("kit name").get();
		String item = args.<String> getOne("item").get();
		int numberOfItem = args.<Integer> getOne("number of items").get();
		Optional<Integer> subtype = args.<Integer> getOne("item subtype");

		List<String> items = ConfigManager.getItems(name);
		String fullCmd;

		if (subtype.isPresent())
		{
			fullCmd = (item + " " + numberOfItem + " " + subtype.get());
		}
		else
		{
			fullCmd = (item + " " + numberOfItem);
		}

		if (items.size() > 0)
		{
			ConfigManager.addItemToKit(name, fullCmd);
			src.sendMessage(Text.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The item was added to kit " + name));
		}
		else
		{
			ConfigManager.addKit(name, fullCmd);
			src.sendMessage(Text.of(TextColors.GOLD, "Success! ", TextColors.YELLOW, "The kit was added! ", TextColors.DARK_GRAY, "Don't forget to reboot the server for the kit to show up!"));
		}

		return CommandResult.success();
	}

}
