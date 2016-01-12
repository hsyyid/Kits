package io.github.hsyyid.kits.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
		}).interval(1, TimeUnit.SECONDS).name("Kits - Counts remaining time for " + uuid).submit(Sponge.getPluginManager().getPlugin("Kits").get().getInstance().get());
	}

	public static void scheduleValueChangeTask(final UUID uuid, final String kit, long delay)
	{
		Sponge.getScheduler().createTaskBuilder().execute(new Runnable()
		{
			public void run()
			{
				ConfigManager.setTrue(uuid, kit);
				Set<Task> tasksToCancel = Sponge.getScheduler().getTasksByName("Kits - Counts remaining time for " + uuid);
				
				for(Task task : tasksToCancel)
				{
					task.cancel();
				}
			}
		}).delay(delay, TimeUnit.SECONDS).name("Kits - Sets Value Back to True for " + uuid).submit(Sponge.getPluginManager().getPlugin("Kits").get().getInstance().get());
	}
	
	public static void restartTasks()
	{
		for(Object player : ConfigManager.getPlayers())
		{
			if(player instanceof String)
			{
				UUID uuid = UUID.fromString((String) player);
				
				for(Object kit : ConfigManager.getPlayerUsedKits(uuid))
				{
					if(kit instanceof String)
					{
						String kitName = (String) kit;
						
						if(!ConfigManager.canUseKit(uuid, kitName) && ConfigManager.getInterval(kitName) instanceof Integer)
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
}
