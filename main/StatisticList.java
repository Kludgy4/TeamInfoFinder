package main;

import java.util.ArrayList;

public class StatisticList {
	private ArrayList<Statistic> statistics = new ArrayList<>();
	
	public StatisticList() {}
	
	public boolean addStatistic(Statistic stat) {
		for(Statistic s : statistics) {
			if (s.name == stat.name) {
				try {
					s.addInt(stat.getInt());
				} catch (Exception e) {
					try {
						s.addBool(stat.getBool());
					} catch (Exception ex) {
						s.addStr(stat.getStr());
					}
				}
				return true;
			}
		}
		statistics.add(stat);
		return true;
	}
	
	public void printStatistics() {
		for (Statistic s : statistics) {
			s.l
		}
	}
}
