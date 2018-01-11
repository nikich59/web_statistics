package stats.controller;

import org.json.simple.JSONObject;
import stats.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 01.01.2018.
 */
public class StatsControllerFactory
{
	private enum Mode
	{
		STATS_FILES,
		DATA_BASE;

		@Override
		public String toString( )
		{
			switch ( this )
			{
				case DATA_BASE:
					return "data_base";
				case STATS_FILES:
					return "stats_files";
				default:
					return super.toString( );
			}
		}
		public static Mode fromString( String s )
		{
			for ( Mode mode : values( ) )
			{
				if ( mode.toString( ).equals( s ) )
				{
					return mode;
				}
			}

			return null;
		}
	}


	private String statisticsDirectory;
	private String finishedStatisticsDirectory;
	private Mode mode;


	public StatsControllerFactory( String statisticsDirectory )
	{
		this.statisticsDirectory = statisticsDirectory;
	}

	public StatsControllerFactory( JSONObject configObject )
	{
		this.statisticsDirectory = ( String ) configObject.get( "statistics_directory" );
		this.mode = Mode.fromString( ( String ) configObject.get( "statistics_mode" ) );
		this.finishedStatisticsDirectory = ( String ) configObject.get( "finished_statistics_directory" );
	}

	public List < StatsController > listStatsInDirectory( )
	{
		List < StatsController > statsControllers = new ArrayList <>( );

		StatsFileController statsFileController = new StatsFileController( );
		List< StatsController > statsFileControllers = statsFileController.listStatistics( statisticsDirectory );
		statsFileControllers.forEach( ( controller ) ->
				( ( StatsFileController ) controller ).
						setFinishedStatisticsDirectoryPath( finishedStatisticsDirectory )
		);
		statsControllers.addAll( statsFileControllers );

		// TODO: Add new types of StatsController here.

		return statsControllers;
	}

	public StatsController createStatisticsController( Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{
		switch ( mode )
		{
			case STATS_FILES:
				StatsController statsFileController =
						new StatsFileController( ).createStatsController( statisticsDirectory, statisticsHeader );
				( ( StatsFileController ) statsFileController ).setFinishedStatisticsDirectoryPath(
						finishedStatisticsDirectory );
				return statsFileController;
			default:
				throw new UnsupportedOperationException( "Mode: \'" + mode.toString( ) + "\' is not supported" );
		}
	}
}
