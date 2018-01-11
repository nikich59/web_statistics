package ru.nikich59.webstatistics.statister;

import ru.nikich59.webstatistics.statister.dataprocessor.DataProcessor;
import ru.nikich59.webstatistics.statister.dataprocessor.DataProcessorFactory;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirer;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirerFactory;
import stats.Statistics;
import stats.controller.StatsController;

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
	/*
	public static class SiteDescriptor implements Cloneable
	{
		public static class ValueDescriptor
		{
			String name;
			String query;
		}

		private String url = "";
		private String headline = "";
		private List < ValueDescriptor > valueDescriptors = new ArrayList <>( );
		private DataType dataType;
		private int initialAcquirePeriodInMillis = 60000;
		private String dataProcessingMethod = "";
		private boolean checkForTooSmallData = true;
		private long targetColumnSum = 0;
		private long targetValuePeriodInMinutes = 60;
		private long expirationPeriodInMinutes = 60 * 24 * 30; // 30 days

		public SiteDescriptor( String url )
		{
			this.url = url;
		}

		public SiteDescriptor( SiteDescriptor other )
		{
			url = other.url;
			headline = other.headline;
			valueDescriptors.addAll( other.valueDescriptors );
			url = other.url;
			dataType = other.dataType;
			initialAcquirePeriodInMillis = other.initialAcquirePeriodInMillis;
			dataProcessingMethod = other.dataProcessingMethod;
			checkForTooSmallData = other.checkForTooSmallData;
			targetColumnSum = other.targetColumnSum;
			targetValuePeriodInMinutes = other.targetValuePeriodInMinutes;
			expirationPeriodInMinutes = other.expirationPeriodInMinutes;
		}

		public void setUrl( String url )
		{
			this.url = url;
		}

		public void setHeadline( String headline )
		{
			this.headline = headline;
		}

		public String getHeadline( )
		{
			return headline;
		}

		public void setInitialAcquirePeriodInMillis( int initialAcquirePeriodInMillis )
		{
			this.initialAcquirePeriodInMillis = initialAcquirePeriodInMillis;
		}

		public int getInitialAcquirePeriodInMillis( )
		{
			return initialAcquirePeriodInMillis;
		}

		public void setCheckForTooSmallData( boolean checkForTooSmallData )
		{
			this.checkForTooSmallData = checkForTooSmallData;
		}

		public boolean checkForTooSmallData( )
		{
			return checkForTooSmallData;
		}

		public void setTargetColumnSum( long targetColumnSum )
		{
			this.targetColumnSum = targetColumnSum;
		}

		public long getTargetColumnSum( )
		{
			return targetColumnSum;
		}

		public void setTargetValuePeriodInMinutes( long targetValuePeriodInMinutes )
		{
			this.targetValuePeriodInMinutes = targetValuePeriodInMinutes;
		}

		public long getTargetValuePeriodInMinutes( )
		{
			return targetValuePeriodInMinutes;
		}

		public void setExpirationPeriodInMinutes( long expirationPeriodInMinutes )
		{
			this.expirationPeriodInMinutes = expirationPeriodInMinutes;
		}

		public long getExpirationPeriodInMinutes( )
		{
			return expirationPeriodInMinutes;
		}

		public SiteDescriptor( JSONObject configJson )
		{
			url = ( String ) configJson.get( "url" );
			dataType = DataType.fromString( ( String ) configJson.get( "data_type" ) );
			initialAcquirePeriodInMillis = ( int ) ( long ) configJson.get( "period" );
			if ( configJson.get( "data_processing_method" ) != null )
			{
				dataProcessingMethod = ( String ) configJson.get( "data_processing_method" );
			}
			else
			{
				dataProcessingMethod = "";
			}

			if ( configJson.get( "target_value_sum" ) != null )
			{
				targetColumnSum = ( long ) configJson.get( "target_value_sum" );

				if ( configJson.get( "target_value_period_in_minutes" ) != null )
				{
					targetValuePeriodInMinutes = ( long ) configJson.get( "target_value_period_in_minutes" );

					checkForTooSmallData = true;
				}
			}

			if ( configJson.get( "expiration_period_in_minutes" ) != null )
			{
				expirationPeriodInMinutes = ( long ) configJson.get( "expiration_period_in_minutes" );
			}

			valueDescriptors = new ArrayList <>( );
			JSONArray valueDescriptorsArray = ( JSONArray ) configJson.get( "value_description" );
			for ( Object valueDescriptorObject : valueDescriptorsArray )
			{
				JSONObject valueDescriptorJson = ( JSONObject ) valueDescriptorObject;

				ValueDescriptor valueDescriptor = new ValueDescriptor( );
				valueDescriptor.query = ( String ) valueDescriptorJson.get( "query" );
				valueDescriptor.name = ( String ) valueDescriptorJson.get( "name" );

				valueDescriptors.add( valueDescriptor );
			}
		}

		public void setDataProcessingMethod( String dataProcessingMethod )
		{
			this.dataProcessingMethod = dataProcessingMethod;
		}

		public String getDataProcessingMethod( )
		{
			return dataProcessingMethod;
		}

		public String getUrl( )
		{
			return url;
		}

		public void setDataType( DataType dataType )
		{
			this.dataType = dataType;
		}

		public DataType getDataType( )
		{
			return dataType;
		}

		public List < ValueDescriptor > getValueDescriptors( )
		{
			return valueDescriptors;
		}

		public void addValueDescriptor( ValueDescriptor valueDescriptor )
		{
			valueDescriptors.add( valueDescriptor );
		}

		public void setValueDescriptors( List < ValueDescriptor > valueDescriptors )
		{
			this.valueDescriptors = valueDescriptors;
		}

		public Statistics.StatisticsHeader getStatisticsHeader( )
		{
			Statistics.StatisticsHeader statisticsHeader = new Statistics.StatisticsHeader( );
			List < String > columnNames = new ArrayList <>( );
			List < String > columnQueries = new ArrayList <>( );
			for ( ValueDescriptor valueDescriptor : getValueDescriptors( ) )
			{
				columnNames.add( valueDescriptor.name );
				columnQueries.add( valueDescriptor.query );
			}

			statisticsHeader.setColumnNames( columnNames );
			statisticsHeader.setColumnQueries( columnQueries );

			statisticsHeader.setDataProcessingMethod( getDataProcessingMethod( ) );
		}
	}*/

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
//	private Sleuth.DataType dataType = Sleuth.DataType.XML;

//	private String configFilePath;

	public int getAcquiringPeriod( )
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

		for ( String query : statisticsHeader.getColumnQueries( ) )
		{
			data.add( statisticsAcquirer.getValue( query ) );
		}

		lastData = data;

		dataPoint.data = new String[ data.size( ) ];
		for ( int i = 0; i < data.size( ); i += 1 )
		{
			dataPoint.data[ i ] = getProcessedData( data.get( i ) );
		}

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

			for ( String columnValue : dataPoint.data )
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

	private String getProcessedData( String data )
	{
		DataProcessor dataProcessor =
				DataProcessorFactory.getDataProcessor( statisticsHeader.getDataProcessingMethod( ) );

		return dataProcessor.getProcessedData( data );
	}
}
