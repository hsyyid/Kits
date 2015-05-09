package io.github.hsyyid;

import java.util.ArrayList;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitExecutor implements CommandExecutor {

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    	Game game = Main.game;
    	String arguments = args.<String>getOne("kit name").get();
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
        return CommandResult.success();
    }
}
