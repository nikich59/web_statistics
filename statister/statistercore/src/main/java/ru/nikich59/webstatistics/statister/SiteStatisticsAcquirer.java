package ru.nikich59.webstatistics.statister;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirer;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Nikita on 24.12.2017.
 */
public class SiteStatisticsAcquirer implements Comparable < SiteStatisticsAcquirer >
{

	public Statistics.StatisticsHeader getStatisticsHeader( )
	{
		return statisticsHeader;
	}
	private Statistics.StatisticsHeader statisticsHeader;
	//	private int initialAcquirePeriodInMillis = 60000;
//	private String statisticsFilePath = "";
	private StatsController statsController;
	private WebDataAcquirer statisticsAcquirer;
	private Timer timer;
	private ZonedDateTime initialDateTime = ZonedDateTime.now( );
//	private String finishedStatisticsDirectoryPath = "";

	private Exception lastException;
	private boolean tooSmallDataChecked = false;
	private AtomicBoolean isRunning = new AtomicBoolean( false );

	public List < String > getLastData( )
	{
		return lastData;
	}
	private List < String > lastData = new ArrayList <>( );
//	private Sleuth.WebDataType dataType = Sleuth.WebDataType.XML;

//	private String configFilePath;

	public long getAcquiringPeriod( )
	{
		return statisticsHeader.getPeriodInMillis( );
	}

	public Exception getLastException( )
	{
		return lastException;
	}

	/*
		public SiteStatisticsAcquirer( String finishedStatisticsDirectoryPath )
		{
			this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;
		}
	*/
	@Override
	public int compareTo( SiteStatisticsAcquirer other )
	{
		return other.getStatisticsId( ).compareTo(
				getStatisticsId( ) );
	}

	public SiteStatisticsAcquirer( StatsController statsController )
	{
		this.statsController = statsController;
	}

	public String getStatisticsId( )
	{
		return statsController.getId( );
	}

	public String getUrl( )
	{
		return statisticsHeader.getUrl( );
	}

	public void start( )
			throws IOException
	{
		if ( statisticsHeader == null )
		{
			statisticsHeader = statsController.loadStatisticsCaption( );
		}

		statisticsAcquirer = WebDataAcquirerFactory.getDataAcquirer(
				statisticsHeader.getDataType( ),
				statisticsHeader.getUrl( ) );

		statsController.initStatistics( statisticsHeader );

		initialDateTime = statsController.getStatisticsHeader( ).getInitialDateTime( );

		timer = new Timer( );
		timer.scheduleAtFixedRate( new TimerTask( )
								   {
									   @Override
									   public void run( )
									   {
										   appendData( );
									   }
								   },
				0,
				statisticsHeader.getPeriodInMillis( )
		);

		isRunning.set( true );
	}

	public void stop( )
			throws IOException
	{
		timer.cancel( );
	}

	public void testConnection( )
			throws WebDataAcquirer.AcquiringException, IOException
	{
		statisticsHeader = statsController.loadStatisticsCaption( );

		statisticsAcquirer = WebDataAcquirerFactory.getDataAcquirer(
				statisticsHeader.getDataType( ),
				statisticsHeader.getUrl( ) );

		statisticsAcquirer.acquireData( );
	}

	public void finish( )
			throws IOException
	{
		stop( );

		statsController.finish( );

		isRunning.set( false );
	}

	public boolean isRunning( )
	{
		return isRunning.get( );
	}

	private void appendData( )
	{
		lastException = null;

		for ( int attemptIndex = 0; attemptIndex < 3; attemptIndex += 1 )
		{
			try
			{
				statisticsAcquirer.acquireData( );

				lastException = null;

				break;
			}
			catch ( WebDataAcquirer.AcquiringException e )
			{
				lastException = e;

//				return;
			}
		}

		if ( lastException != null )
		{
			return;
		}

		Statistics.DataPoint dataPoint = new Statistics.DataPoint( );

		List < String > data = new ArrayList <>( );

		for ( Statistics.ValueDescription valueDescription : statisticsHeader.getValueDescriptions( ) )
		{
			data.add( valueDescription.getDataType( )
					.getProcessedData( statisticsAcquirer.getValue( valueDescription.getQuery( ) ) ) );
		}

		lastData = data;

		dataPoint.setData( data );
/*
		dataPoint.data = new String[ data.size( ) ];
		for ( int i = 0; i < data.size( ); i += 1 )
		{
			dataPoint.data[ i ] = getProcessedData( data.get( i ) );
		}
*/
		try
		{
			statsController.appendData( dataPoint );
		}
		catch ( Exception e )
		{
			lastException = e;
		}

		long minutesSinceAcquiringStart =
				Duration.between( initialDateTime, ZonedDateTime.now( ) ).toMinutes( );

		if ( minutesSinceAcquiringStart > statisticsHeader.getExpirationPeriodInMinutes( ) )
		{
			for ( int i = 0; i < 3; i += 1 )
			{
				try
				{
					finish( );

					lastException = null;

					break;
				}
				catch ( Exception e )
				{
					lastException = e;

					// TODO: Implement error handling.
				}
			}
		}

		if ( ! tooSmallDataChecked &&
				minutesSinceAcquiringStart >= statisticsHeader.getTargetValuePeriodInMinutes( ) )
		{
			tooSmallDataChecked = true;

			long columnSum = 0L;

			for ( String columnValue : dataPoint.getData( ) )
			{
				columnSum += Long.parseLong( columnValue );
			}

			if ( columnSum < statisticsHeader.getTargetColumnSum( ) )
			{
				for ( int i = 0; i < 3; i += 1 )
				{
					try
					{
						finish( );

						lastException = null;

						break;
					}
					catch ( Exception e )
					{
						lastException = e;

						// TODO: Implement error handling.
					}
				}
			}
		}
	}
/*
	private String getProcessedData( String data )
	{
		DataProcessor dataProcessor =
				DataProcessorFactory.getDataProcessor( statisticsHeader.getDataProcessingMethod( ) );

		return dataProcessor.getProcessedData( data );
	}*/
}
