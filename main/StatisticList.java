package main;

import java.util.ArrayList;

public class StatisticList {
	public ArrayList<Statistic> statistics = new ArrayList<>();
	
	/**
	 * An unused constructor for the statisticlist object
	 */
	public StatisticList() {}

	/**
	 * Adds a statistic to this list of statistics, combines it with another if it has the same name
	 * @param stat The statistic to add to this list
	 * @return A boolean that represents whether the statistic was not a duplicate
	 */
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
}
