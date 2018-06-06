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
	
	/**
	 * An integer constructor for the statistic object
	 * @param name The name of the statistic
	 * @param statisticInt The integer value to add to the integer statistics
	 */
	public Statistic(String name, int statisticInt) {
		this.name = name;
		intStatistics.add(statisticInt);
	}
	
	/**
	 * An boolean constructor for the statistic object
	 * @param name The name of the statistic
	 * @param statisticBool The boolean value to add to the boolean statistics
	 */
	public Statistic(String name, boolean statisticBool) {
		this.name = name;
		boolStatistics.add(statisticBool);
	}
	
	/**
	 * A string constructor for the statistic object
	 * @param name The name of the statistic
	 * @param statisticStr The string value to add to the string statistics
	 */
	public Statistic(String name, String statisticStr) {
		this.name = name;
		strStatistics.add(statisticStr);
	}
	
	/**
	 * Adds two statistics together
	 * @param stat The statistic to add to this statistic
	 */
	public void addStat(Statistic stat) {
		try {
			intStatistics.addAll(stat.intStatistics);
		} catch (Exception e) {
			try {
				boolStatistics.addAll(stat.boolStatistics);
			} catch (Exception ex) {
				strStatistics.addAll(stat.strStatistics);
			}
		}
	}
	
	/**
	 * Gets the average of this statistic
	 * @return The double average of this statistic
	 * @throws Exception
	 */
	public double getAverage() throws Exception {
		if (intStatistics.size() == 0) throw new Exception();
		
		int sum = 0;
		for (int d : intStatistics) sum += d;
		return 1.0d * sum / intStatistics.size();
	}
	
	/**
	 * Gets the average of this statistic
	 * @return The boolean average of this statistic
	 * @throws Exception
	 */
	public boolean getModeBoolean() throws Exception {
		if (boolStatistics.size() == 0) throw new Exception();
		
		if (Collections.frequency(boolStatistics, true) > Collections.frequency(boolStatistics, false)) {
			return true;
		} else {return false;}
	}
	
	/**
	 * Gets the mode string of this statistic
	 * @return The string average of this statistic
	 * @throws Exception
	 */
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
