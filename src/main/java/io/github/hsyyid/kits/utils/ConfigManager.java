package io.github.hsyyid.kits.utils;

import io.github.hsyyid.kits.Kits;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfigManager
{
	public static void addItemToKit(String kitName, String item)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		String formattedItem = (item + ",");
		valueNode.setValue(items + formattedItem);
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to add " + item + " to kit " + kitName);
		}
	}

	public static void addKit(String kitName, String item)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode kitNode = Kits.config.getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String formattedKitName = (kitName + ",");
		kitNode.setValue(kits + formattedKitName);
		
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to add kit " + kitName);
		}
		ConfigurationNode itemNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String formattedItemName = (item + ",");
		itemNode.setValue(formattedItemName);
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to add " + item + " to kit " + kitName);
		}
		ConfigurationNode intervalNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		intervalNode.setValue(30000);
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to set the interval on the kit!");
		}
	}

	public static void addPlayerToConfig(UUID playerName, String kitName)
	{
		final UUID userName = playerName;
		final String kit = kitName;
		Game game = Kits.game;
		Scheduler scheduler = game.getScheduler();
		Task.Builder taskBuilder = scheduler.createTaskBuilder();
		String name = playerName.toString();
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		if (ConfigManager.getInterval(kitName) instanceof Integer)
		{
			int interval = (Integer) ConfigManager.getInterval(kitName);
			if (isPlayerInConfig(playerName, kitName))
			{
				taskBuilder.execute(new Runnable()
				{
					public void run()
					{
						ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
						Kits.config.getNode("players", userName.toString(), kit, "usable").setValue("true");
						try
						{
							configManager.save(Kits.config);
							configManager.load();
						}
						catch (IOException e)
						{
							System.out.println("[Kits]: Failed to save config!");
						}
					}
				}).delay(interval, TimeUnit.MILLISECONDS).name("Kits - Sets Value").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());
			}
			else
			{
				Kits.config.getNode("players", name, kitName, "usable").setValue("true");
				try
				{
					configManager.save(Kits.config);
					configManager.load();
				}
				catch (IOException e)
				{
					System.out.println("[KITS]: Failed to add " + name + " to config!");
				}
				taskBuilder.execute(new Runnable()
				{
					public void run()
					{
						ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
						Kits.config.getNode("players", userName.toString(), kit, "usable").setValue("true");
						try
						{
							configManager.save(Kits.config);
							configManager.load();
						}
						catch (IOException e)
						{
							System.out.println("[Kits]: Failed to save config!");
						}
					}
				}).delay(interval, TimeUnit.MILLISECONDS).name("Kits - Sets Val Back to True").submit(game.getPluginManager().getPlugin("Kits").get().getInstance());
			}
		}
	}

	public static boolean isPlayerInConfig(UUID playerName, String kitName)
	{
		String userName = playerName.toString();
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("players." + userName + "." + kitName + ".usable").split("\\."));
		Object inConfig = valueNode.getValue();
		if (inConfig != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean canUseKit(UUID playerName, String kitName)
	{
		String userName = playerName.toString();
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("players." + userName + "." + kitName + ".usable").split("\\."));
		if (valueNode.getValue() == null)
		{
			return true;
		}
		boolean b = valueNode.getBoolean();
		return b;
	}

	public static void setFalse(UUID playerName, String kitName)
	{
		String name = playerName.toString();
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("players." + name + "." + kitName + ".usable").split("\\."));
		valueNode.setValue(false);
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to save config!");
		}
	}

	public static Object getInterval(String kitName)
	{
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		Object val = valueNode.getValue();

		if (val instanceof Boolean)
		{
			return valueNode.getBoolean();
		}
		else if (val instanceof Integer)
		{
			return valueNode.getInt();
		}
		return val;
	}

	public static void setInterval(int interval, String kitName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode intervalNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		intervalNode.setValue(interval);
		
		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to change the interval for kit " + kitName);
		}

	}

	public static void setInterval(String kitName, boolean oneTime)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		ConfigurationNode intervalNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
		intervalNode.setValue(oneTime);

		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to change the interval for kit " + kitName);
		}

	}
}
