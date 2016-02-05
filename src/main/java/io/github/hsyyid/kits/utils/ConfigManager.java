package io.github.hsyyid.kits.utils;

import com.google.common.collect.Lists;
import io.github.hsyyid.kits.config.Config;
import io.github.hsyyid.kits.config.Configs;
import io.github.hsyyid.kits.config.Configurable;
import io.github.hsyyid.kits.config.KitsConfig;
import io.github.hsyyid.kits.config.PlayerDataConfig;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfigManager
{
	private static Configurable config = Config.getConfig();
	private static Configurable playerConfig = PlayerDataConfig.getConfig();
	private static Configurable kitsConfig = KitsConfig.getConfig();

	public static List<String> getKits()
	{
		ConfigurationNode kits = Configs.getConfig(kitsConfig).getNode("kits", "kits");

		if (kits.getValue() == null)
			return Lists.newArrayList();

		String kit = kits.getString("default");
		boolean finished = false;
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

	public static void deleteKit(String name)
	{
		ConfigurationNode kitNode = Configs.getConfig(kitsConfig).getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String newVal = kits.replace(name + ",", "");
		Configs.setValue(kitsConfig, kitNode.getPath(), newVal);
		Configs.removeChild(kitsConfig, new Object[] { "kits" }, name);
	}

	public static Set<Object> getPlayers()
	{
		return Configs.getConfig(playerConfig).getNode("players").getChildrenMap().keySet();
	}

	public static Set<Object> getPlayerUsedKits(UUID uuid)
	{
		return Configs.getConfig(playerConfig).getNode("players", uuid.toString()).getChildrenMap().keySet();
	}

	public static void setTrue(UUID uuid, String kit)
	{
		Configs.setValue(playerConfig, new Object[] { "players", uuid.toString(), kit, "usable" }, true);
	}

	public static List<String> getItems(String kitName)
	{
		ConfigurationNode valueNode = Configs.getConfig(kitsConfig).getNode((Object[]) ("kits." + kitName + ".item").split("\\."));

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
		ConfigurationNode node = Configs.getConfig(playerConfig).getNode("players", uuid.toString(), kit, "time");

		if (node.getValue() == null)
			return 0;
		else
			return node.getDouble();
	}

	public static void setTimeRemaining(UUID uuid, String kit, double timeRemaining)
	{
		Configs.setValue(playerConfig, new Object[] { "players", uuid.toString(), kit, "time" }, timeRemaining);
	}

	public static void addItemToKit(String kitName, String item)
	{
		ConfigurationNode valueNode = Configs.getConfig(playerConfig).getNode((Object[]) ("kits." + kitName + ".item").split("\\."));
		String items = valueNode.getString();
		String formattedItem = (item + ",");
		Configs.setValue(kitsConfig, valueNode.getPath(), (items + formattedItem));
	}

	public static void addKit(String kitName, String item)
	{
		ConfigurationNode kitNode = Configs.getConfig(kitsConfig).getNode((Object[]) ("kits.kits").split("\\."));
		String kits = kitNode.getString();
		String formattedKitName = (kitName + ",");
		Configs.setValue(kitsConfig, kitNode.getPath(), (kits + formattedKitName));
		Configs.setValue(kitsConfig, new Object[] { "kits", kitName, "item" }, (item + ","));
		Configs.setValue(kitsConfig, new Object[] { "kits", kitName, "interval" }, 30);
	}

	public static boolean isPlayerInConfig(UUID uuid, String kitName)
	{
		ConfigurationNode valueNode = Configs.getConfig(playerConfig).getNode((Object[]) ("players." + uuid.toString() + "." + kitName + ".usable").split("\\."));
		Object value = valueNode.getValue();

		if (value != null)
			return true;
		else
			return false;
	}

	public static boolean canUseKit(UUID uuid, String kitName)
	{
		ConfigurationNode valueNode = Configs.getConfig(playerConfig).getNode(new Object[] { "players", uuid.toString(), kitName, "usable" });

		if (valueNode.getValue() == null)
			return true;
		else
			return valueNode.getBoolean();
	}

	public static void setFalse(UUID uuid, String kitName)
	{
		Configs.setValue(playerConfig, new Object[] { "players", uuid.toString(), kitName, "usable" }, false);
	}

	public static Object getInterval(String kitName)
	{
		ConfigurationNode valueNode = Configs.getConfig(kitsConfig).getNode((Object[]) ("kits." + kitName + ".interval").split("\\."));
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

	public static TimeUnit getTimeUnit()
	{
		ConfigurationNode valueNode = Configs.getConfig(config).getNode((Object[]) ("kits.interval.units").split("\\."));
		String timeUnit = valueNode.getString();
		return TimeUnit.valueOf(timeUnit);
	}

	public static void setInterval(Object interval, String kitName)
	{
		Configs.setValue(kitsConfig, new Object[] { "kits", kitName, "interval" }, interval);
	}

	public static void addPlayerToConfig(UUID uuid, String kit)
	{
		Configs.setValue(playerConfig, new Object[] { "players", uuid.toString(), kit, "usable" }, true);
	}
}
