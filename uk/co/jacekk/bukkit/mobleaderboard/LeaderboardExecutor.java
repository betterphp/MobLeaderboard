package uk.co.jacekk.bukkit.mobleaderboard;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class LeaderboardExecutor extends BaseCommandExecutor<MobLeaderboard> {
	
	public LeaderboardExecutor(MobLeaderboard plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		String playerName = sender.getName();
		
		if (args.length == 0 || args[0].equalsIgnoreCase(playerName)){
			if (!Permission.LOOKUP_SELF.hasPermission(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to lookup your own position");
				return true;
			}
		}else{
			if (!Permission.LOOKUP_SELF.hasPermission(sender)){
				sender.sendMessage(ChatColor.RED + "You do not have permission to lookup other players");
				return true;
			}
			
			playerName = args[0];
		}
		
		int[] data = plugin.leaderboard.getPlayerEntry(playerName);
		
		StringBuilder line = new StringBuilder();
		
		DecimalFormat formater = new DecimalFormat("#,###,###,##0");
		
		line.append(playerName);
		line.append(" is ranked ");
		
		line.append(data[0]);
		
		if (data[0] >= 11 && data[0] <= 13){
			line.append("th");
		}else{
			switch (data[0] % 10){
				case 1: line.append("st"); break;
				case 2: line.append("nd"); break;
				case 3: line.append("rd"); break;
				default: line.append("st"); break;
			}
		}
		
		line.append(" with ");
		line.append(formater.format(data[1]));
		line.append(" kills");
		
		sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + line.toString()));
		
		return true;
	}
	
}
