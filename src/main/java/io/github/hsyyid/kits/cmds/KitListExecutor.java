package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import io.github.hsyyid.kits.utils.PaginatedList;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;

public class KitListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Optional<Integer> pageNo = args.<Integer> getOne("page number");

		int pgNo = 1;

		if (pageNo.isPresent())
			pgNo = pageNo.get();

		PaginatedList pList = new PaginatedList("/kits");

		for (String name : Kits.allKits)
		{
			if (src.hasPermission("kits.use." + name))
			{
				Text item = 
					Text.builder(name)
						.onClick(TextActions.runCommand("/kit " + name))
						.onHover(TextActions.showText(Text.of(TextColors.WHITE, "Spawn kit ", TextColors.GOLD, name)))
						.color(TextColors.DARK_AQUA)
						.style(TextStyles.UNDERLINE)
						.build();

				pList.add(item);
			}
		}

		pList.setItemsPerPage(10);

		if (pgNo > pList.getTotalPages())
			pgNo = 1;

		Text.Builder header = Text.builder();
		header.append(Text.of(TextColors.GREEN, "------------"));
		header.append(Text.of(TextColors.GREEN, " Showing Kits page " + pgNo + " of " + pList.getTotalPages() + " "));
		header.append(Text.of(TextColors.GREEN, "------------"));

		pList.setHeader(header.build());

		src.sendMessage(pList.getPage(pgNo));

		return CommandResult.success();
	}
}
