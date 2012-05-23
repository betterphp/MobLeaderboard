package uk.co.jacekk.bukkit.mobleaderboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Leaderboard {
	
	private File dataFile;
	
	private LinkedHashMap<String, Integer> data;
	private LinkedHashMap<String, Integer> topten;
	
	private Comparator<Entry<String, Integer>> comp;
	
	private long lastSave;
	private boolean recalulationNeeded;
	
	public Leaderboard(File dataFile){
		this.dataFile = dataFile;
		
		this.data = new LinkedHashMap<String, Integer>();
		this.topten = new LinkedHashMap<String, Integer>();
		
		this.comp = new Comparator<Entry<String, Integer>>(){
			
			public int compare(Entry<String, Integer> a, Entry<String, Integer> b){
				return -1 * a.getValue().compareTo(b.getValue());
			}
			
		};
		
		this.lastSave = System.currentTimeMillis();
		this.recalulationNeeded = false;
		
		if (this.dataFile.exists() == false){
			try{
				this.dataFile.createNewFile();
				this.save();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws IOException, ClassNotFoundException {
		try{
			this.data = (LinkedHashMap<String, Integer>) new ObjectInputStream(new FileInputStream(this.dataFile)).readObject();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void save(boolean checkTime) throws FileNotFoundException, IOException {
		long timeNow = System.currentTimeMillis();
		
		if (checkTime && timeNow - this.lastSave < 10000){
			return;
		}
		
		this.lastSave = timeNow;
		
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.dataFile));
		
		stream.writeObject(this.data);
		stream.flush();
		stream.close();
	}
	
	public void save() throws FileNotFoundException, IOException {
		this.save(false);
	}
	
	public void recalculateOrder(){
		LinkedList<Entry<String, Integer>> values = new LinkedList<Entry<String, Integer>>(this.data.entrySet());
		
		Collections.sort(values, this.comp);
		
		this.data.clear();
		
		for (Entry<String, Integer> value : values){
			this.data.put(value.getKey(), value.getValue());
		}
		
		this.recalulationNeeded = false;
	}
	
	public LinkedHashMap<String, Integer> getTop(int total){
		if (this.recalulationNeeded){
			this.recalculateOrder();
			
			this.topten.clear();
			
			int i = 0;
			
			for (Entry<String, Integer> entry : this.data.entrySet()){
				if (++i > total){
					break;
				}
				
				this.topten.put(entry.getKey(), entry.getValue());
			}
		}
		
		return this.topten;
	}
	
	public void reset(){
		this.data.clear();
	}
	
	public void removePlayer(String playerName){
		this.data.remove(playerName);
	}
	
	public void incrementPlayer(String playerName){
		this.data.put(playerName, (this.data.containsKey(playerName)) ? this.data.get(playerName) + 1 : 1);
		this.recalulationNeeded = true;
	}
	
	public int[] getPlayerEntry(String playerName){
		int rank = this.data.size() + 1;
		int killed = 0;
		
		int i = 1;
		
		for (Entry<String, Integer> entry : this.data.entrySet()){
			if (entry.getKey().equalsIgnoreCase(playerName)){
				rank = i;
				killed = entry.getValue();
				
				break;
			}
			
			++i;
		}
		
		return new int[]{rank, killed};
	}
	
}