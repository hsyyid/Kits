package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.Kits;
import io.github.hsyyid.kits.utils.PaginatedList;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;

public class KitListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Optional<Integer> pageNo = args.<Integer>getOne("page number");
		
		int pgNo = 1;
		
		if(pageNo.isPresent())
			pgNo = pageNo.get();
		
		PaginatedList pList = new PaginatedList("/kits");
		
		if (pgNo > pList.getTotalPages())
		{
			pgNo = 1;
		}
		for (String name : Kits.allKits)
		{
			Text item = Texts.builder(name)
				.onClick(TextActions.runCommand("/kit " + name))
				.onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Spawn kit ", TextColors.GOLD, name)))
				.color(TextColors.DARK_AQUA)
				.style(TextStyles.UNDERLINE)
				.build();

			pList.add(item);
		}
		pList.setItemsPerPage(10);

		TextBuilder header = Texts.builder();
		header.append(Texts.of(TextColors.GREEN, "------------"));
		header.append(Texts.of(TextColors.GREEN, " Showing Kits page " + pgNo + " of " + pList.getTotalPages() + " "));
		header.append(Texts.of(TextColors.GREEN, "------------"));

		pList.setHeader(header.build());

		src.sendMessage(pList.getPage(pgNo));
		
		return CommandResult.success();
	}
}
