package ru.nikich59.webstatistics.statister.model;

import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import stats.controller.StatsFileController;
import ru.nikich59.webstatistics.statister.sleuth.Sleuth;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Nikita on 27.12.2017.
 */
public class Model
{
	private Set < Sleuth > sleuthList = new HashSet <>( );

	private ConcurrentSkipListSet < SiteStatisticsAcquirer > statisticsAcquirers = new ConcurrentSkipListSet <>( );
	private String statistersDirectoryPath;
	private String finishedStatisticsDirectoryPath;

	private Timer reportTimer;

	public Model( int reportTimerPeriodInMillis )
	{
		reportTimer = new Timer( );

		reportTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						System.out.println( );
						System.out.println( "As for " + LocalDateTime.now( ).toString( ) + ": " );

						for ( SiteStatisticsAcquirer siteStatisticsAcquirer : statisticsAcquirers )
						{
							String message = "";
							if ( siteStatisticsAcquirer.getLastException( ) == null )
							{
								message = "OK";
							}
							else
							{
								message = siteStatisticsAcquirer.getLastException( ).toString( );
							}

							System.out.println( "	Statistics acquirer for " +
									siteStatisticsAcquirer.getUrl( ) + " :\n		" + message );
						}

						for ( Sleuth sleuth : sleuthList )
						{
							String message = "";
							if ( sleuth.getLastException( ) == null )
							{
								message = "OK";
							}
							else
							{
								message = sleuth.getLastException( ).toString( );
							}

							System.out.println( "	Sleuth for " +
									sleuth.getUrl( ) + " :\n		" + message );
						}

						System.out.println( "---" );
						System.out.println( );
						System.out.println( );
					}
				},
				2000,
				reportTimerPeriodInMillis
		);
	}

	public void addSleuth( Sleuth sleuth )
	{
		System.out.println( "New sleuth: \"" + sleuth.getUrl( ) + "\"" );

		sleuthList.add( sleuth );

		sleuth.setSiteDescriptorConsumer( ( siteDescriptor ) ->
				{
/*					String filePath =  statistersDirectoryPath +
							DigestUtils.sha1Hex( siteDescriptor.getUrl( ) ) + ".stats";*/

					String filePath = statistersDirectoryPath + siteDescriptor.getUrl( ).
							replace( " ", "" ).replace( "?", "" ).
							replace( ".", "" ).replace( "\\", "" ).
							replace( "/", "" ).replace( ":", "" ) + ".stats";

					File file = new File( filePath );

					if ( hasStatister( file.getAbsolutePath( ) ) )
					{
						return;
					}

					SiteStatisticsAcquirer siteStatisticsAcquirer = new SiteStatisticsAcquirer( finishedStatisticsDirectoryPath );
					siteStatisticsAcquirer.setSiteDescriptor( siteDescriptor );

					StatsFileController statsFileController = new StatsFileController( );
					statsFileController.setFinishedStatisticsDirectoryPath( finishedStatisticsDirectoryPath );
					statsFileController.setStatisticsDirectory( statistersDirectoryPath );

					siteStatisticsAcquirer.setStatisticsController( statsFileController );

					addStatisticsAcquirer( siteStatisticsAcquirer );
				}
		);
	}

	public void setFinishedStatisticsDirectoryPath( String finishedStatisticsDirectoryPath )
	{
		this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;
	}

	public void setStatistersDirectoryPath( String statistersDirectoryPath )
	{
		this.statistersDirectoryPath = statistersDirectoryPath;
	}

	public void addStatisticsAcquirer( SiteStatisticsAcquirer siteStatisticsAcquirer )
	{
		System.out.println( "New statister: \"" + siteStatisticsAcquirer.getUrl( ) + "\"" );


		statisticsAcquirers.add( siteStatisticsAcquirer );

		try
		{
			siteStatisticsAcquirer.start( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	public boolean hasSleuth( String configFilePath )
	{
		for ( Sleuth sleuth : sleuthList )
		{
			if ( sleuth.getConfigFilePath( ) != null && sleuth.getConfigFilePath( ).equals( configFilePath ) )
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasStatister( String id )
	{
		for ( SiteStatisticsAcquirer statisticsAcquirer : statisticsAcquirers )
		{
			if ( statisticsAcquirer.getStatisticsId( ) != null &&
					statisticsAcquirer.getStatisticsId( ).equals( id ) )
			{
				return true;
			}
		}

		return false;
	}
}
















