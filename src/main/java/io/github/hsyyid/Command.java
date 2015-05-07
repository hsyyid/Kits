package io.github.hsyyid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class Command implements CommandCallable {
    private final Server server;
    //Remember, to use TEXT you MUST cast it.
    private final Optional<Text> desc = Optional.of((Text) Texts.of("Kits enables users to use kits, defined in the configuration files!"));
    private final Optional<Text> help = Optional.of((Text) Texts.of("To use Kits do /kit."));
    List<String> suggestions = new ArrayList<String>();
    Game game = Main.game;
    public Command(Server server) {
        this.server = server;
    }

	public Optional<Text> getHelp(CommandSource arg0) {
		return help;
	}
	
	public Optional<Text> getShortDescription(CommandSource arg0) {
		return desc;
	}

	public List<String> getSuggestions(CommandSource arg0, String arg1)
			throws CommandException {
		return suggestions;
	}

	public Text getUsage(CommandSource arg0) {
		return ((Text) Texts.of("Use /kit <name> to spawn a kit!"));
	}
	
	public Optional<CommandResult> process(CommandSource src, String arguments)
			throws CommandException {
		
		if(arguments != null){
		//Get Items
		String items = Main.getItems(arguments);
		
		boolean finished = false;
		
		//Array List to Keep all the Items in
		ArrayList<String> itemList = new ArrayList<String>();
		
		if(finished != true){
			int endIndex = items.indexOf(",");
			if(endIndex != -1){
				String substring = items.substring(0, endIndex);
				itemList.add(substring);
				
				//If they Have More than 1
				while(finished != true){
					int startIndex = endIndex;
					endIndex = items.indexOf(",", startIndex + 1);
					if(endIndex != -1){
						String substrings = items.substring(startIndex+1, endIndex);
						itemList.add(substrings);
					}
					else{
						finished = true;
					}
				}
			}
			else{
				itemList.add(items);
				finished = true;
			}
		}
		
		if(src instanceof Player) {
			Player player = (Player) src;
			//Give Player their Kit
			for (String i: itemList)
			{
				game.getCommandDispatcher().process(game.getServer().getConsole(), "give" + " " + player.getName() + " " + i);
			}
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
		}
		else if(src instanceof CommandBlockSource) {
		    src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
		}
		
	}
	else{
		src.sendMessage(Texts.of("PLEASE SPECIFY A KIT NAME! /kit <name>"));
	}
		
		return Optional.of(CommandResult.success());	
	}
	
	public boolean testPermission(CommandSource source) {
		return source.hasPermission("kits.use");
	}
}