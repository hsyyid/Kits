package io.github.hsyyid.kits.cmds;

import io.github.hsyyid.kits.utils.ConfigManager;
import io.github.hsyyid.kits.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class KitExecutor implements CommandExecutor
{
	private String kit;

	public KitExecutor(String kit)
	{
		this.kit = kit;
	}

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		Optional<Player> target = args.<Player> getOne("target");

		if (!target.isPresent())
		{
			if (src instanceof Player)
			{
				final Player player = (Player) src;

				if (!ConfigManager.isPlayerInConfig(player.getUniqueId(), kit))
					ConfigManager.addPlayerToConfig(player.getUniqueId(), kit);

				List<String> items = ConfigManager.getItems(kit);

				if (items.size() == 0)
				{
					player.sendMessage(Text.of(TextColors.RED, "Error: ", TextColors.DARK_RED, "The specified kit was not found, or there was an error retrieving data from it."));
					return CommandResult.success();
				}

				if (ConfigManager.canUseKit(player.getUniqueId(), kit))
				{
					// Give Player their Kit
					for (String i : items)
					{
						String id = i;
						int quantity = 1;
						int meta = -1;

						if (id.contains(" "))
						{
							id = id.substring(0, id.indexOf(" "));
							String substring = id.substring(id.indexOf(" ") + 1, id.length());

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
							
							if (optionalItemType.get().getMaxStackQuantity() < quantity)
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

					ConfigManager.setFalse(player.getUniqueId(), kit);

					if (ConfigManager.getInterval(kit) instanceof Integer)
					{
						long val = (Integer) ConfigManager.getInterval(kit);
						ConfigManager.setTimeRemaining(player.getUniqueId(), kit, val);
						Utils.scheduleUpdateTask(player.getUniqueId(), kit);
						Utils.scheduleValueChangeTask(player.getUniqueId(), kit, val);
					}
				}
				else
				{
					if (ConfigManager.getInterval(kit) instanceof Integer)
					{
						if (ConfigManager.getTimeRemaining(player.getUniqueId(), kit) > 0)
							src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait " + ConfigManager.getTimeRemaining(player.getUniqueId(), kit) + " seconds before using this Kit again!"));
						else
							src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must wait before using this Kit again!"));
					}
					else if (ConfigManager.getInterval(kit) instanceof Boolean)
					{
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "This Kit is one-time use only!"));
					}
				}
			}
			else if (src instanceof ConsoleSource)
			{
				src.sendMessage(Text.of("Must be an in-game player to use /kit!"));
			}
			else if (src instanceof CommandBlockSource)
			{
				src.sendMessage(Text.of("Must be an in-game player to use /kit!"));
			}
		}
		else if (src.hasPermission("kits.use.others"))
		{
			Player player = target.get();
			List<String> items = ConfigManager.getItems(kit);

			for (String i : items)
			{
				String id = i;
				int quantity = 1;
				int meta = -1;

				if (id.contains(" "))
				{
					id = id.substring(0, id.indexOf(" "));
					String substring = id.substring(id.indexOf(" ") + 1, id.length());

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

		return CommandResult.success();
	}
}
