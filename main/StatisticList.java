package main;

import java.util.ArrayList;

public class StatisticList {
	private ArrayList<Statistic> statistics = new ArrayList<>();
	
	public StatisticList() {}

	public boolean addStatistic(Statistic stat) {
		for(Statistic s : statistics) {
			if (s.name == stat.name) {
				s.addStat(stat);
				return false;
			}
		}
		statistics.add(stat);
		return true;
	}
	
	public ArrayList<Statistic> getStatistics() {
		return statistics;
	}
}
