package io.github.hsyyid.kits.listeners;

import io.github.hsyyid.kits.utils.ConfigManager;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class SignChangeListener
{
	@Listener
	public void onPlayerChangeSign(ChangeSignEvent event, @First Player player)
	{
		SignData signData = event.getText();
		List<Text> signLines = signData.getListValue().get();

		if (signLines.get(0).toPlain().equals("[Kit]"))
		{
			if (player.hasPermission("kits.sign.create"))
			{
				if (ConfigManager.getKits().contains(signLines.get(1).toPlain()))
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_BLUE, "[Kit]")));
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Created Kit sign."));
				}
				else
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_RED, "[Kit]")));
					player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Kit does not exist."));
				}
			}
			else
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to create Kit signs."));
				event.setCancelled(true);
			}
		}
	}
}
