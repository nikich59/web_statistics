package ru.nikich59.webstatistics.core.corebasics.stats.controller;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikita on 30.12.2017.
 */
public abstract class StatsController
{
	public abstract Statistics.StatisticsHeader loadStatisticsCaption( )
			throws IOException;

	public abstract void storeStatistics( Statistics statistics )
			throws IOException;

	public abstract void initStatistics( Statistics.StatisticsHeader statisticsHeader )
			throws IOException;

	public abstract void finish( )
			throws IOException;

	public abstract void appendData( Statistics.DataPoint dataPoint )
			throws IOException;

	public abstract void loadStatistics( )
			throws IOException;

	public abstract String getId( );

	public abstract Statistics getStatistics( );

	public abstract Statistics.StatisticsHeader getStatisticsHeader( );

	public abstract void initFromMap( Map < String, Object > configMap );

	public abstract List < StatsController > listStatistics( );
/*
	public abstract StatsController createStatsController(
			String directory, Statistics.StatisticsHeader statisticsHeader )
		throws IOException;
		*/
}









