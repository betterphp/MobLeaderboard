package uk.co.jacekk.bukkit.mobleaderboard;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class TopTenExecutor extends BaseCommandExecutor<MobLeaderboard> {

	public TopTenExecutor(MobLeaderboard plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!Permission.LOOKUP_TOPTEN.hasPermission(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
		Set<Entry<String, Integer>> entries = plugin.leaderboard.getTop(10).entrySet();
		
		if (entries.size() == 0){
			sender.sendMessage(ChatColor.RED + "There are no players on the list yet.");
			return true;
		}
		
		List<String> mobList = plugin.config.getStringList(ConfigKey.MOB_TYPES);
		int totalMobs = mobList.size();
		
		StringBuilder line = new StringBuilder();
		line.append("Top ");
		
		String mobName;
		String lastMob = mobList.remove(totalMobs - 1);
		
		if (totalMobs > 1){
			for (int i = 1; i < totalMobs; ++i){
				mobName = mobList.get(i);
				
				line.append(Character.toUpperCase(mobName.charAt(0)) + mobName.substring(1).toLowerCase());
				line.append(", ");
			}
			
			line.append(" and ");
		}
		
		line.append(Character.toUpperCase(lastMob.charAt(0)) + lastMob.substring(1).toLowerCase());
		line.append(" killers");
		
		sender.sendMessage(plugin.formatMessage(ChatColor.GREEN + line.toString()));
		
		DecimalFormat formater = new DecimalFormat("#,###,###,##0");
		
		int rank = 0;
		
		for (Entry<String, Integer> entry : entries){
			line.setLength(0);
			
			line.append(ChatColor.GREEN);
			line.append("  ");
			line.append(++rank);
			line.append(" - ");
			line.append(entry.getKey());
			line.append(" ");
			line.append(formater.format(entry.getValue()));
			
			sender.sendMessage(line.toString());
		}
		
		return true;
	}
	
}
