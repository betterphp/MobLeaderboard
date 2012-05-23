package uk.co.jacekk.bukkit.mobleaderboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;

public class PlayerDeathListener extends BaseListener<MobLeaderboard> {
	
	public PlayerDeathListener(MobLeaderboard plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event){
		((MobLeaderboard) plugin).leaderboard.removePlayer(event.getEntity().getName());
	}
	
}
