package io.github.hsyyid.kits.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;

public class PlayerInteractBlockListener
{
	@Listener
	public void onPlayerRightClickBlock(InteractBlockEvent.Secondary event, @First Player player)
	{
		Optional<Location<World>> location = event.getTargetBlock().getLocation();

		if (location.isPresent())
		{
			Optional<TileEntity> optTileEntity = location.get().getTileEntity();

			if (optTileEntity.isPresent() && optTileEntity.get() instanceof Sign)
			{
				Sign sign = (Sign) optTileEntity.get();
				List<Text> signLines = sign.getSignData().getListValue().get();

				if (signLines.get(0).toPlain().equals("[Kit]"))
				{
					if (player.hasPermission("kits.sign.use"))
					{
						Sponge.getCommandManager().process(player, "kit " + signLines.get(1).toPlain());
					}
					else
					{
						player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to use Kit signs!"));
					}
				}
			}
		}
	}
}
