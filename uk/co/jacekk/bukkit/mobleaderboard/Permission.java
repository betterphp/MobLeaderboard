package uk.co.jacekk.bukkit.mobleaderboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

public enum Permission implements PluginPermission {
	
	LOOKUP_TOPTEN(	"mobleaderboard.lookup.topten",		PermissionDefault.TRUE,		"Allows the player to view the top ten players"),
	LOOKUP_SELF(	"mobleaderboard.lookup.self",		PermissionDefault.TRUE,		"Allows the player to view their position on the leaderboard"),
	LOOKUP_OTHERS(	"mobleaderboard.lookup.others",		PermissionDefault.TRUE,		"Allows the player to view other players position on the leaderboard");
	
	protected String node;
	protected PermissionDefault defaultValue;
	protected String description;
	
	private Permission(String node, PermissionDefault defaultValue, String description){
		this.node = node;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	public List<Player> getPlayersWith(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()){
			if (this.hasPermission(player)){
				players.add(player);
			}
		}
		
		return players;
	}
	
	public Boolean hasPermission(CommandSender sender){
		return sender.hasPermission(this.node);
	}
	
	public String getNode(){
		return this.node;
	}
	
	public PermissionDefault getDefault(){
		return this.defaultValue;
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
