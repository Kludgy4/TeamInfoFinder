package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistic {
	
	String name;
	
	public ArrayList<Integer> intStatistics = new ArrayList<Integer>();
	public ArrayList<Boolean> boolStatistics = new ArrayList<Boolean>();
	public ArrayList<String> strStatistics = new ArrayList<String>();
	
	public Statistic(String name) {
		this.name = name;
	}
	
	public Statistic(String name, int statisticInt) {
		this.name = name;
		intStatistics.add(statisticInt);
	}
	
	public Statistic(String name, boolean statisticBool) {
		this.name = name;
		boolStatistics.add(statisticBool);
	}
	
	public Statistic(String name, String statisticStr) {
		this.name = name;
		strStatistics.add(statisticStr);
	}
	
	public void addStat(Statistic stat) {
		try {
			intStatistics.add(stat.intStatistics.get(0));
		} catch (Exception e) {
			try {
				boolStatistics.add(stat.boolStatistics.get(0));
			} catch (Exception ex) {
				strStatistics.add(stat.strStatistics.get(0));
			}
		}
	}
	
	public double getAverage() throws Exception {
		if (intStatistics.size() == 0) throw new Exception();
		
		int sum = 0;
		for (int d : intStatistics) sum += d;
		return 1.0d * sum / intStatistics.size();
	}
	
	public boolean getModeBoolean() throws Exception {
		if (boolStatistics.size() == 0) throw new Exception();
		
		if (Collections.frequency(boolStatistics, true) > Collections.frequency(boolStatistics, false)) {
			return true;
		} else {return false;}
	}
	
	public ArrayList<String> getModeString() throws Exception {
		if (strStatistics.size() == 0) throw new Exception();
		
		Map<String, Long> strOcurrences = 
				  strStatistics.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
		
		ArrayList<String> modeEntries = new ArrayList<>();
		long highestFreq = 0;
		modeEntries.add(null);

		for (Map.Entry<String, Long> entry : strOcurrences.entrySet()) {
		    if (modeEntries.size() == 0 || entry.getValue().compareTo(highestFreq) > 0)  {
		    	modeEntries.clear();
		    	modeEntries.add(entry.getKey());
		    	highestFreq = entry.getValue();
		    } else if (entry.getValue().compareTo(highestFreq) == 0) {
		    	modeEntries.add(entry.getKey());
		    }
		}
		return modeEntries;
	}

}
