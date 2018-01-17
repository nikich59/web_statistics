package ru.nikich59.webstatistics.core.corejpa;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsFileController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

	/*
		private String statisticsDirectory;
		private String finishedStatisticsDirectory;
		*/
	private Mode mode;


	private Map < String, Object > statsControllerTemplateMap;
	/*
		public StatsControllerFactory( String statisticsDirectory )
		{
			this.statisticsDirectory = statisticsDirectory;
		}
	*/
	public StatsControllerFactory( Map < String, Object > configMap )
	{
		statsControllerTemplateMap = configMap;

		mode = Mode.fromString( ( String ) configMap.get( "statistics_mode" ) );

		/*
		this.statisticsDirectory = ( String ) configMap.get( "statistics_directory" );
		this.mode = Mode.fromString( ( String ) configMap.get( "statistics_mode" ) );
		this.finishedStatisticsDirectory = ( String ) configMap.get( "finished_statistics_directory" );
		*/
	}

	public List < StatsController > listStats( )
			throws IOException
	{
		StatsController statsController = createStatsController( );

		if ( statsController == null )
		{
			return null;
		}

//		statsController.loadStatisticsCaption( );

		return statsController.listStatistics( );
	}

	private StatsController createStatsController( )
	{
		switch ( mode )
		{
			case STATS_FILES:
				StatsFileController statsFileController = new StatsFileController( );
				statsFileController.initFromMap( statsControllerTemplateMap );
				return statsFileController;
			case DATA_BASE:
				StatsControllerDB statsControllerDB = new StatsControllerDB( );
				statsControllerDB.initFromMap( statsControllerTemplateMap );
				return statsControllerDB;
			default:
				return null;
		}
	}

	public StatsController createStatisticsController( Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{
		StatsController statsController = createStatsController( );

		if ( statsController == null )
		{
			return null;
		}

		statsController.initStatistics( statisticsHeader );

		return statsController;
	}
}
