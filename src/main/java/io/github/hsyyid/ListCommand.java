package io.github.hsyyid;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import com.google.common.base.Optional;

public class ListCommand implements CommandCallable{
	
	private final Server server;
	Game game = Main.game;
    
    public ListCommand(Server server) {
        this.server = server;
    }
    List<String> suggestions = new ArrayList<String>();
    private final Optional<Text> desc = Optional.of((Text) Texts.of("This command enables users to view all the kits avaliable!"));
    private final Optional<Text> help = Optional.of((Text) Texts.of("To list the Kits do /kits."));
	public Optional<CommandResult> process(CommandSource src,
			String arguments) throws CommandException {
				int pgNo = 1;
				if(arguments.length() == 0){
					pgNo = 1;
				}
				else{
					try{
						pgNo = Integer.parseInt(arguments);
					}catch(NumberFormatException e){
						src.sendMessage(Texts.of(TextColors.DARK_RED, "Error: ", TextColors.RED, "Format: /kits <page number>"));
					}
				}
				//Add List
				PaginatedList pList = new PaginatedList("/kits");
				for (String name: Main.allKits) {
				     Text item = Texts.builder(name)
				        .onClick(TextActions.runCommand("/kit " + name))
				        .onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Spawn kit ", TextColors.GOLD, name)))
				        .color(TextColors.DARK_AQUA)
				        .style(TextStyles.UNDERLINE)
				        .build();

				    pList.add(item);
				}
				pList.setItemsPerPage(10);
				//Header
				TextBuilder header = Texts.builder();
				header.append(Texts.of(TextColors.GREEN, "------------"));
				header.append(Texts.of(TextColors.GREEN, " Showing Kits page " + pgNo + " of " + pList.getTotalPages() + " "));
				header.append(Texts.of(TextColors.GREEN, "------------"));

				pList.setHeader(header.build());
				//Send List
				src.sendMessage(pList.getPage(pgNo));		
				return Optional.of(CommandResult.success());
	}

	public List<String> getSuggestions(CommandSource source, String arguments)
			throws CommandException {
		return suggestions;
	}

	public boolean testPermission(CommandSource source) {
		return source.hasPermission("kits.list");
	}

	public Optional<Text> getShortDescription(CommandSource source) {
		return desc;
	}

	public Optional<Text> getHelp(CommandSource source) {
		return help;
	}

	public Text getUsage(CommandSource source) {
		return ((Text) Texts.of("Use /kits <pgNo> to list the kits!"));
	}

}
