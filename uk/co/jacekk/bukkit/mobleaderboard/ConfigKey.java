package uk.co.jacekk.bukkit.mobleaderboard;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfigKey;

public enum ConfigKey implements PluginConfigKey {
	
	MOB_TYPES(				"mob-types",				Arrays.asList("CHICKEN")),
	RESET_TIME(				"reset-time",				7),
	RESET_ON_DEATH(			"reset-on-death",			false);
	
	private String key;
	private Object defaultValue;
	
	private ConfigKey(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public Object getDefault(){
		return this.defaultValue;
	}
	
	public String getKey(){
		return this.key;
	}
	
}
