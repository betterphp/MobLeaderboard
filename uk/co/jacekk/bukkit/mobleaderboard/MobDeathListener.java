package uk.co.jacekk.bukkit.mobleaderboard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;

public class MobDeathListener extends BaseListener<MobLeaderboard> {
	
	public MobDeathListener(MobLeaderboard plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event){
		Event damageEvent = event.getEntity().getLastDamageCause();
		String configName = event.getEntityType().getName().toUpperCase();
		
		if (damageEvent instanceof EntityDamageByEntityEvent && plugin.config.getStringList(ConfigKey.MOB_TYPES).contains(configName)){
			EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) damageEvent;
			Entity damager = entityDamageEvent.getDamager();
			
			if (damager instanceof Player){
				plugin.leaderboard.incrementPlayer(((Player) damager).getName());
				
				try{
					plugin.leaderboard.save(true);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
}
