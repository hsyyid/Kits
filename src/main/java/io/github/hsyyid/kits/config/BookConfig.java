package io.github.hsyyid.kits.config;

import io.github.hsyyid.kits.Kits;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the book.conf file
 */
public class BookConfig implements Configurable
{
	private static BookConfig config = new BookConfig();

	private BookConfig()
	{
		;
	}

	public static BookConfig getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(Kits.getKits().getConfigDir().resolve("data") + "/book.conf");
	private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	private CommentedConfigurationNode configNode;

	@Override
	public void setup()
	{
		if (!Files.exists(configFile))
		{
			try
			{
				Files.createFile(configFile);
				load();
				populate();
				save();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			load();
		}
	}

	@Override
	public void load()
	{
		try
		{
			configNode = configLoader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save()
	{
		try
		{
			configLoader.save(configNode);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void populate()
	{
		get().getNode("book").setComment("Contains data of book given to players on first join.");
		get().getNode("book", "title").setValue("&aKits");
		get().getNode("book", "author").setValue("HassanS6000");
		get().getNode("book", "pages").setComment("Contains pages of book.");
		get().getNode("book", "pages", "1").setValue("&bThis is an example\n&cOf a book\n&dEnjoy!").setComment("Page 1 of the book.");
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
