package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;

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
	
	public double getAverage() {
		int sum = 0;
		for (int d : intStatistics) sum += d;
		return 1.0d * sum / intStatistics.size();
	}

}
