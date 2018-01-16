package ru.nikich59.webstatistics.core.corebasics.stats.controller;

import org.apache.commons.io.FilenameUtils;
import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.StatsFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikita on 30.12.2017.
 */
public class StatsFileController extends StatsController
{
	public static final String FILE_EXTENSION = "stats";

	private String statisticsDirectory = "";
	private String finishedStatisticsDirectoryPath = "";

	private Statistics statistics = null;

	public void setStatisticsDirectory( String statisticsDirectory )
	{
		this.statisticsDirectory = statisticsDirectory;
	}

	public StatsFileController( )
	{

	}

	public void setFinishedStatisticsDirectoryPath( String finishedStatisticsDirectoryPath )
	{
		this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;
	}

	@Override
	public Statistics.StatisticsHeader loadStatisticsCaption( )
			throws IOException
	{
		StatsFile statsFile = StatsFile.loadCaptionFromFile( getFilePath( ) );

		this.statistics = statsFile.getStatistics( );

		return statsFile.getStatistics( ).getHeader( );
	}

	private Statistics.StatisticsHeader loadStatisticsCaption( String filePath )
			throws IOException
	{
		StatsFile statsFile = StatsFile.loadCaptionFromFile( filePath );

		this.statistics = statsFile.getStatistics( );

		return statistics.getHeader( );
	}

	@Override
	public void initFromMap( Map < String, Object > configMap )
	{
		statisticsDirectory = ( String ) configMap.get( "statistics_directory" );
		finishedStatisticsDirectoryPath = ( String ) configMap.get( "finished_statistics_directory" );
	}

	@Override
	public void storeStatistics( Statistics statistics )
			throws IOException
	{
		StatsFile statsFile = new StatsFile( );
		statsFile.setStatistics( statistics );

		String filePath = getFilePath( );

		statsFile.writeToFile( filePath );
	}

	@Override
	public void initStatistics( Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{
		StatsFile statsFile = new StatsFile( );
		Statistics statistics = new Statistics( );
		statistics.setHeader( statisticsHeader );
		statsFile.setStatistics( statistics );

		this.statistics = statistics;

		String filePath = getFilePath( ); // + getFileNameForStatistics( statisticsHeader ) + "." + FILE_EXTENSION;

		File file = new File( filePath );
		if ( ! file.exists( ) )
		{
//			file.mkdirs( );

			System.out.println( file.getAbsolutePath( ) );

			file.createNewFile( );

			statsFile.writeToFile( filePath );
		}
	}

	private String getFileName( )
	{
		return getStatisticsHeader( ).getUrl( ).
				replace( " ", "" ).replace( "?", "" ).
				replace( ".", "" ).replace( "\\", "" ).
				replace( "/", "" ).replace( ":", "" ) +
				"." + FILE_EXTENSION;
	}

	private String getFilePath( )
	{
		return statisticsDirectory + getFileName( );
	}

	private String getFinishedFilePath( )
	{
		return finishedStatisticsDirectoryPath + getFileName( );
	}

	@Override
	public String getId( )
	{
		return statistics.getHeader( ).getStatisticsId( ) + "." + FILE_EXTENSION;
	}
	/*
		public StatsController createStatsController(
				Map < String, Object > configMap, Statistics.StatisticsHeader statisticsHeader )
				throws IOException
		{
			StatsFileController newController = new StatsFileController( );
	//		newController.setStatisticsDirectory( statisticsDirectory );
			newController.initFromMap( configMap );
	//		newController.setStatisticsDirectory( statisticsDirectory + getFileNameForStatistics( statisticsHeader ) );

			newController.initStatistics( statisticsHeader );

			return newController;
		}

	*/
	@Override
	public void finish( )
			throws IOException
	{
		File statsFile = new File( getFilePath( ) );

		File finishedStatsFile = new File( getFinishedFilePath( ) );
		Files.move( statsFile.getAbsoluteFile( ).toPath( ),
				finishedStatsFile.getAbsoluteFile( ).toPath( ),
				StandardCopyOption.REPLACE_EXISTING );
	}

	@Override
	public void appendData( Statistics.DataPoint dataPoint )
			throws IOException
	{
		StatsFile.appendData( getFilePath( ), dataPoint );
	}

	@Override
	public void loadStatistics( )
			throws IOException
	{
		statistics = StatsFile.loadFromFile( getFilePath( ) ).getStatistics( );
	}

	@Override
	public Statistics getStatistics( )
	{
		return statistics;
	}

	@Override
	public Statistics.StatisticsHeader getStatisticsHeader( )
	{
		if ( statistics == null )
		{
			return null;
		}

		return statistics.getHeader( );
	}

	@Override
	public List < StatsController > listStatistics( )
	{
		ArrayList < StatsController > statisticsArrayList = new ArrayList <>( );

		File statsDir = new File( statisticsDirectory );

		File[] statsFiles = statsDir.listFiles( );

		if ( statsFiles == null )
		{
			return statisticsArrayList;
		}

		for ( File statsFile : statsFiles )
		{
			String extension = FilenameUtils.getExtension( statsFile.getAbsolutePath( ) );
			if ( extension.equals( FILE_EXTENSION ) )
			{
				StatsFileController statsFileController = new StatsFileController( );
				statsFileController.setStatisticsDirectory( statisticsDirectory );

				statisticsArrayList.add( statsFileController );

				try
				{
					Statistics.StatisticsHeader statisticsHeader =
							statsFileController.loadStatisticsCaption( statsFile.getAbsolutePath( ) );

					if ( statisticsHeader == null )
					{
						System.out.println( "NULL" );
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace( );

					continue;
					// TODO: Implement error handling.
					// Ignoring exception. If exception occurred then file is not valid so just ignoring it.
				}

//				statisticsArrayList.add( statsFileController );
			}
		}

		return statisticsArrayList;
	}
}
