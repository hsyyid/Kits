package io.github.hsyyid.kits;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Server;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

public class Command implements CommandCallable {
    private final Server server;
    //Remember, to use TEXT you MUST cast it.
    private final Optional<Text> desc = Optional.of((Text) Texts.of("Kits enables users to use kits, defined in the configuration files!"));
    private final Optional<Text> help = Optional.of((Text) Texts.of("To use Kits do /kit."));
    List<String> suggestions = new ArrayList<String>();

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
		return ((Text) Texts.of("Use /rebooter reload to reload the config."));
	}

	public CommandResult process(CommandSource source, String arguments)
			throws CommandException {
		//Spawn Kit
		return CommandResult.success();
	}
	public boolean testPermission(CommandSource source) {
		return source.hasPermission("kits.use");
	}
}