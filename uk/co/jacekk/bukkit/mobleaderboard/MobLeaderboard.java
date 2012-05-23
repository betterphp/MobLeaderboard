package uk.co.jacekk.bukkit.mobleaderboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

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
		
		File updateTimeFile = new File(this.baseDirPath + File.separator + "nextupdate.dat");
		
		if (!updateTimeFile.exists()){
			this.nextReset = System.currentTimeMillis() + (this.config.getInt(ConfigKey.RESET_TIME) * 86400000);
		}else{
			try{
				FileInputStream input = new FileInputStream(updateTimeFile);
				DataInputStream stream = new DataInputStream(input);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				
				this.nextReset = Long.parseLong(reader.readLine().trim());
				
				reader.close();
				stream.close();
				input.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
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
			
			FileWriter writer = new FileWriter(new File(this.baseDirPath + File.separator + "nextupdate.dat"));
			BufferedWriter out = new BufferedWriter(writer);
			
			out.write(new StringBuilder().append(this.nextReset).toString());
			
			out.close();
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
