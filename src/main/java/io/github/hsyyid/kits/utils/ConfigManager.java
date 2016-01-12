package io.github.hsyyid.kits.utils;

import com.google.common.collect.Lists;
import io.github.hsyyid.kits.Kits;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfigManager
{
	public static List<String> getKits()
	{
		// Get Kit Names
		ConfigurationNode kits = Kits.config.getNode((Object[]) "kits.kits".split("\\."));
		String kit = kits.getString("default");

		boolean finished = false;
		// Array List to Keep all the Kits in
		List<String> kitList = Lists.newArrayList();

		// Add all kits to kitList
		if (finished != true)
		{
			int endIndex = kit.indexOf(",");
			if (endIndex != -1)
			{
				String substring = kit.substring(0, endIndex);
				kitList.add(substring);

				// If they Have More than 1
				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = kit.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = kit.substring(startIndex + 1, endIndex);
						kitList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				kitList.add(kit);
				finished = true;
			}
		}

		return kitList;
	}

	public static Set<Object> getPlayers()
	{
		return Kits.config.getNode("players").getChildrenMap().keySet();
	}
	
	public static Set<Object> getPlayerUsedKits(UUID uuid)
	{
		return Kits.config.getNode("players", uuid.toString()).getChildrenMap().keySet();
	}
	
	public static void setTrue(UUID uuid, String kit)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		Kits.config.getNode("players", uuid.toString(), kit, "usable").setValue(true);
		
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

	public static List<String> getItems(String kitName)
	{
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String items = valueNode.getString();

		boolean finished = false;
		List<String> itemList = Lists.newArrayList();

		if (finished != true)
		{
			int endIndex = items.indexOf(",");

			if (endIndex != -1)
			{
				String substring = items.substring(0, endIndex);
				itemList.add(substring);

				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = items.indexOf(",", startIndex + 1);

					if (endIndex != -1)
					{
						String substrings = items.substring(startIndex + 1, endIndex);
						itemList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				itemList.add(items);
				finished = true;
			}
		}

		return itemList;
	}

	public static double getTimeRemaining(UUID uuid, String kit)
	{
		ConfigurationNode node = Kits.config.getNode("players", uuid.toString(), kit, "time");

		if (node.getValue() == null)
			return 0;
		else
			return node.getDouble();
	}

	public static void setTimeRemaining(UUID uuid, String kit, double timeRemaining)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		Kits.config.getNode("players", uuid.toString(), kit, "time").setValue(timeRemaining);

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
		intervalNode.setValue(30);

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

	public static boolean isPlayerInConfig(UUID uuid, String kitName)
	{
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("players." + uuid.toString() + "." + kitName + ".usable").split("\\."));
		Object value = valueNode.getValue();

		if (value != null)
			return true;
		else
			return false;
	}

	public static boolean canUseKit(UUID uuid, String kitName)
	{
		ConfigurationNode valueNode = Kits.config.getNode((Object[]) ("players." + uuid.toString() + "." + kitName + ".usable").split("\\."));

		if (valueNode.getValue() == null)
			return true;

		return valueNode.getBoolean();
	}

	public static void setFalse(UUID uuid, String kitName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		Kits.config.getNode("players", uuid.toString(), kitName, "usable").setValue(false);

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

	public static void setInterval(Object interval, String kitName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		Kits.config.getNode("kits", kitName, "interval").setValue(interval);

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

	public static void addPlayerToConfig(UUID uuid, String kit)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Kits.getConfigManager();
		Kits.config.getNode("players", uuid.toString(), kit, "usable").setValue(true);

		try
		{
			configManager.save(Kits.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Kits]: Failed to add player to the config.");
		}
	}
}
