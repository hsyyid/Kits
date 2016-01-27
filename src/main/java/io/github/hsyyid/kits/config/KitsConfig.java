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
 * Handles the kits.conf file
 */
public class KitsConfig implements Configurable
{
	private static KitsConfig config = new KitsConfig();

	private KitsConfig()
	{
		;
	}

	public static KitsConfig getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(Kits.getKits().getConfigDir().resolve("data") + "/kits.conf");
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
		get().getNode("kits").setComment("Contains all Kits data.");
		get().getNode("kits", "kits").setValue("default,");
		get().getNode("kits", "default", "item").setValue("diamond_axe,");
		get().getNode("kits", "default", "interval").setValue(60);
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
