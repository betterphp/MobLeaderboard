package uk.co.jacekk.bukkit.mobleaderboard;

import java.io.File;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;

public class MobLeaderboard extends BasePlugin {
	
	protected PluginConfig config;
	protected Leaderboard leaderboard;
	
	protected long nextReset;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), ConfigKey.values(), this.log);
		this.leaderboard = new Leaderboard(new File(this.baseDirPath + File.separator + "leaderboard.bin"));
		
		this.nextReset = System.currentTimeMillis();
		
		try{
			this.leaderboard.load();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if (this.config.getBoolean(ConfigKey.RESET_ON_DEATH)){
			this.pluginManager.registerEvents(new PlayerDeathListener(this), this);
		}
		
		if (this.config.getStringList(ConfigKey.MOB_TYPES).size() > 0){
			this.pluginManager.registerEvents(new MobDeathListener(this), this);
		}
		
		for (Permission permission : Permission.values()){
			this.pluginManager.addPermission(new org.bukkit.permissions.Permission(permission.getNode(), permission.getDescription(), permission.getDefault()));
		}
		
		this.getCommand("topten").setExecutor(new TopTenExecutor(this));
		this.getCommand("leaderboard").setExecutor(new LeaderboardExecutor(this));
		
		this.scheduler.scheduleSyncRepeatingTask(this, new ResetTask(this), 0L, 36000L); // ~ half an hour
	}
	
	public void onDisable(){
		try{
			this.leaderboard.save();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
