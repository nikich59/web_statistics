package stats.controller;

import org.apache.commons.io.FilenameUtils;
import stats.Statistics;
import stats.StatsFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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
		StatsFile statsFile = StatsFile.loadCaptionFromFile( statisticsDirectory );

		this.statistics = statsFile.getStatistics( );

		return statsFile.getStatistics( ).getHeader( );
	}

	@Override
	public void storeStatistics( Statistics statistics )
			throws IOException
	{
		StatsFile statsFile = new StatsFile( );
		statsFile.setStatistics( statistics );

		String filePath = statisticsDirectory;

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

		String filePath = statisticsDirectory; // + getFileNameForStatistics( statisticsHeader ) + "." + FILE_EXTENSION;

		File file = new File( filePath );
		if ( ! file.exists( ) )
		{
//			file.mkdirs( );

			file.createNewFile( );

			statsFile.writeToFile( filePath );
		}
	}

	private static String getFileNameForStatistics( Statistics.StatisticsHeader statisticsHeader )
	{
		return statisticsHeader.getUrl( ).
				replace( " ", "" ).replace( "?", "" ).
				replace( ".", "" ).replace( "\\", "" ).
				replace( "/", "" ).replace( ":", "" ) +
				"." + FILE_EXTENSION;
	}

	@Override
	public String getId( )
	{
		return statistics.getHeader( ).getStatisticsId( ) + "." + FILE_EXTENSION;
	}

	@Override
	public StatsController createStatsController(
			String statisticsDirectory, Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{
		StatsFileController newController = new StatsFileController( );
		newController.setStatisticsDirectory( statisticsDirectory + getFileNameForStatistics( statisticsHeader ) );

		newController.initStatistics( statisticsHeader );

		return newController;
	}


	@Override
	public void finish( )
			throws IOException
	{
		File statsFile = new File( statisticsDirectory );

		File finishedStatsFile = new File( finishedStatisticsDirectoryPath + statsFile.getName( ) );
		Files.move( statsFile.getAbsoluteFile( ).toPath( ),
				finishedStatsFile.getAbsoluteFile( ).toPath( ),
				StandardCopyOption.REPLACE_EXISTING );
	}

	@Override
	public void appendData( Statistics.DataPoint dataPoint )
			throws IOException
	{
		StatsFile.appendData( statisticsDirectory, dataPoint );
	}

	@Override
	public void loadStatistics( )
			throws IOException
	{
		statistics = StatsFile.loadFromFile( statisticsDirectory ).getStatistics( );
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
	public List < StatsController > listStatistics( String statisticsDirectory )
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
				statsFileController.setStatisticsDirectory( statsFile.getAbsolutePath( ) );

				statisticsArrayList.add( statsFileController );

				try
				{
					statsFileController.loadStatisticsCaption( );
				}
				catch ( Exception e )
				{
					// TODO: Implement error handling.
					// Ignoring exception. If exception occurred then file is not valid so just ignoring it.
				}
			}
		}

		return statisticsArrayList;
	}
}
