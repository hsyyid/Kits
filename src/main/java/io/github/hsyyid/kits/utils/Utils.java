package io.github.hsyyid.kits.utils;

import io.github.hsyyid.kits.PluginInfo;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Utils
{
	public static void scheduleUpdateTask(final UUID uuid, final String kit)
	{
		Sponge.getScheduler().createTaskBuilder().execute(new Runnable()
		{
			public void run()
			{
				if (ConfigManager.getTimeRemaining(uuid, kit) > 0)
					ConfigManager.setTimeRemaining(uuid, kit, ConfigManager.getTimeRemaining(uuid, kit) - 1);
			}
		}).interval(1, ConfigManager.getTimeUnit()).name("Kits - Counts remaining time for " + uuid).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
	}

	public static void scheduleValueChangeTask(final UUID uuid, final String kit, long delay)
	{
		Sponge.getScheduler().createTaskBuilder().execute(new Runnable()
		{
			public void run()
			{
				ConfigManager.setTrue(uuid, kit);
				Set<Task> tasksToCancel = Sponge.getScheduler().getTasksByName("Kits - Counts remaining time for " + uuid);

				for (Task task : tasksToCancel)
				{
					task.cancel();
				}
			}
		}).delay(delay, ConfigManager.getTimeUnit()).name("Kits - Sets Value Back to True for " + uuid).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
	}

	public static void restartTasks()
	{
		for (Object player : ConfigManager.getPlayers())
		{
			if (player instanceof String)
			{
				UUID uuid = UUID.fromString((String) player);

				for (Object kit : ConfigManager.getPlayerUsedKits(uuid))
				{
					if (kit instanceof String)
					{
						String kitName = (String) kit;

						if (!ConfigManager.canUseKit(uuid, kitName) && ConfigManager.getInterval(kitName) instanceof Integer)
						{
							long timeRemaining = (long) ConfigManager.getTimeRemaining(uuid, kitName);
							Utils.scheduleUpdateTask(uuid, kitName);
							Utils.scheduleValueChangeTask(uuid, kitName, timeRemaining);
						}
					}
				}
			}
		}
	}

	public static void givePlayerKit(Player player, List<String> items)
	{
		// Give Player their Kit
		for (String i : items)
		{
			String id = i;
			int quantity = 1;
			int meta = -1;
			
			if (id.startsWith("execute_cmd:")){
				String cmd = id.split(":")[1];
				if (!cmd.startsWith("/")){
					cmd = "/"+cmd;
				}
				Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), cmd.replace("@p", Player.getName()));
				continue;
			}

			if (id.contains(" "))
			{
				String substring = id.substring(id.indexOf(" ") + 1, id.length());
				id = id.substring(0, id.indexOf(" "));

				if (substring.contains(" "))
				{
					String quant = substring.substring(0, substring.indexOf(" "));

					try
					{
						quantity = Integer.parseInt(quant);
					}
					catch (NumberFormatException e)
					{
						quantity = 1;
					}

					String met = substring.substring(substring.indexOf(" ") + 1, substring.length());

					try
					{
						meta = Integer.parseInt(met);
					}
					catch (NumberFormatException e)
					{
						meta = -1;
					}
				}
				else
				{
					try
					{
						quantity = Integer.parseInt(substring);
					}
					catch (NumberFormatException e)
					{
						quantity = 1;
					}
				}
			}

			Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, id);

			if (optionalItemType.isPresent())
			{
				int c = 1;

				if (quantity > optionalItemType.get().getMaxStackQuantity())
				{
					c = quantity;
					quantity = 1;
				}

				for (int z = 0; z < c; z++)
				{
					ItemStack stack = Sponge.getRegistry()
						.createBuilder(ItemStack.Builder.class)
						.itemType(optionalItemType.get())
						.quantity(quantity)
						.build();

					if (meta == -1)
					{
						player.getInventory().offer(stack);
					}
					else
					{
						DataContainer container = stack.toContainer().set(DataQuery.of("UnsafeDamage"), meta);
						stack = Sponge.getRegistry()
							.createBuilder(ItemStack.Builder.class)
							.fromContainer(container)
							.build();
						player.getInventory().offer(stack);
					}
				}
			}
		}
	}

	public static void givePlayerBook(Player player)
	{
		ItemStack stack = ItemStack.builder().itemType(ItemTypes.WRITTEN_BOOK).quantity(1).build();
		stack.offer(Keys.BOOK_AUTHOR, ConfigManager.getBookAuthor());
		stack.offer(Keys.BOOK_PAGES, ConfigManager.getBookPages());
		stack.offer(Keys.DISPLAY_NAME, ConfigManager.getBookTitle());
		player.getInventory().offer(stack);
	}
}
