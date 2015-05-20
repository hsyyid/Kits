package io.github.hsyyid;

import java.util.ArrayList;
import java.util.Timer;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitExecutor implements CommandExecutor {
	String kit;
	public KitExecutor(String kitName){
		kit = kitName;
	}
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    	Game game = Main.game;
    		if(src instanceof Player) {
    			Player player = (Player) src;
    			Utils.addConfig(player.getName(), kit);
    		}
    		//Get Items
    		String items = "";
    		if(Main.getItems(kit) != null){
    			items = Main.getItems(kit);
    		}
    		else{
    			src.sendMessage(Texts.of(TextColors.RED,"Error: ", TextColors.DARK_RED, "The specified kit was not found, or there was an error retrieving data from it."));
    			return CommandResult.success();
    		}
    		
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
    		Timer t = new Timer();
    		if(src instanceof Player) {
    			Player player = (Player) src;
    			if(IntervalTask.canUse(player.getName(), kit)){
    				//Give Player their Kit
    				for (String i: itemList)
    				{
    					game.getCommandDispatcher().process(game.getServer().getConsole(), "give" + " " + player.getName() + " " + i);
    				}
    				Utils.setFalse(player.getName() , kit);
    				t.schedule(new IntervalTask(player.getName(), kit), Utils.getInterval(kit));
    			}
    			else{
    				src.sendMessage(Texts.of(TextColors.DARK_RED,"Error! ", TextColors.RED, "You must wait before using this kit again!"));
    			}
    		}
    		else if(src instanceof ConsoleSource) {
    		    src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
    		}
    		else if(src instanceof CommandBlockSource) {
    		    src.sendMessage(Texts.of("Must be an in-game player to use /kit!"));
    		}
        return CommandResult.success();
    }
}
