package main;

import java.util.ArrayList;

public class Statistic {
	
	String name;
	
	private ArrayList<Integer> intStatistics;
	private ArrayList<Boolean> boolStatistics;
	private ArrayList<String> strStatistics;
	
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
	
	public void addInt(int statisticInt) {
		intStatistics.add(statisticInt);
	}
	
	public void addBool(boolean statisticBool) {
		boolStatistics.add(statisticBool);
	}
	
	public void addStr(String statisticStr) {
		strStatistics.add(statisticStr);
	}
	
	public int getInt() {
		return intStatistics.get(0);
	}
	
	public boolean getBool() {
		return boolStatistics.get(0);
	}
	
	public String getStr() {
		return strStatistics.get(0);
	}

}
