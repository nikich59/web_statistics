package stats.controller;

import org.json.simple.JSONObject;
import stats.Statistics;

import java.io.IOException;
import java.util.List;

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

	public abstract List < StatsController > listStatistics( String directory );

	public abstract StatsController createStatsController(
			String directory, Statistics.StatisticsHeader statisticsHeader )
		throws IOException;
}









