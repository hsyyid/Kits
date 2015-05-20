package io.github.hsyyid;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class KitAddExecutor implements CommandExecutor {
	
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException {
		String name = args.<String>getOne("kit name").get();
		String item = args.<String>getOne("item").get();
		String noOfItems = "";
		int numberOfItem = args.<Integer>getOne("number of items").get();
		noOfItems = Integer.toString(numberOfItem);
		int subtype = 0;
		boolean isType = false;
		try{
			subtype = args.<Integer>getOne("item subtype").get();
			isType = true;
		}catch(IllegalStateException e){
			//do notin
		}

		
		//String Items is Purely to Check if the Kit Exists or Not
		String items = Main.getItems(name);
		
		//Adds the Command with noOfItems.
		String fullCmd = ""; 
		if(isType){
		  	fullCmd = (item + " " + noOfItems + " " + subtype);
		}
		else{
			fullCmd = (item + " " + noOfItems);
		}
		if(items != null){
			Utils.addItem(name, fullCmd);
			src.sendMessage(Texts.of(TextColors.GOLD,"Success! ", TextColors.YELLOW, "The item was added to kit " + name));
		}
		else{
			Utils.addKit(name, fullCmd);
			src.sendMessage(Texts.of(TextColors.GOLD,"Success! ", TextColors.YELLOW, "The kit was added! ", TextColors.DARK_GRAY, "Don't forget to reboot the server for the kit to show up!"));
		}
		return CommandResult.success();
	}

}
