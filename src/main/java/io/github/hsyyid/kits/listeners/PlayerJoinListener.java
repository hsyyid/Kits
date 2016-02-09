package io.github.hsyyid.kits.listeners;

import io.github.hsyyid.kits.utils.ConfigManager;
import io.github.hsyyid.kits.utils.Utils;
import org.spongepowered.api.data.manipulator.mutable.entity.JoinData;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

public class PlayerJoinListener
{
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event)
	{
		Optional<JoinData> optionalJoinData = event.getTargetEntity().getOrCreate(JoinData.class);

		if(optionalJoinData.isPresent())
		{
			JoinData joinData = optionalJoinData.get();

			if(joinData.lastPlayed().equals(joinData.firstPlayed()))
			{
				if(ConfigManager.getDefaultKit().isPresent())
					Utils.givePlayerKit(event.getTargetEntity(), ConfigManager.getItems(ConfigManager.getDefaultKit().get()));
			}
		}
	}
}
