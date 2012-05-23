package uk.co.jacekk.bukkit.mobleaderboard;

import uk.co.jacekk.bukkit.baseplugin.BaseTask;

public class ResetTask extends BaseTask<MobLeaderboard> {
	
	public ResetTask(MobLeaderboard plugin){
		super(plugin);
	}
	
	public void run(){
		long timeNow = System.currentTimeMillis();
		
		if (timeNow > plugin.nextReset){
			plugin.leaderboard.reset();
			plugin.nextReset = timeNow + (plugin.config.getInt(ConfigKey.RESET_TIME) * 86400000);
			
			plugin.log.info("Leaderboard reset");
		}
	}
	
}
