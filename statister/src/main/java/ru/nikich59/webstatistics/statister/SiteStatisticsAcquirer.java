package ru.nikich59.webstatistics.statister;

import com.sun.javafx.UnmodifiableArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirer;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirerFactory;
import stats.DataType;
import stats.Statistics;
import stats.controller.StatsController;
import stats.controller.StatsFileController;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 24.12.2017.
 */
public class SiteStatisticsAcquirer implements Comparable < SiteStatisticsAcquirer >
{
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
	}

	private SiteDescriptor siteDescriptor;
	//	private int initialAcquirePeriodInMillis = 60000;
//	private String statisticsFilePath = "";
	private StatsController statsController = new StatsFileController( );
	private WebDataAcquirer statisticsAcquirer;
	private Timer timer;
	private ZonedDateTime initialDateTime = ZonedDateTime.now( );
	private String finishedStatisticsDirectoryPath = "";

	private Exception lastException;
//	private Sleuth.DataType dataType = Sleuth.DataType.XML;

//	private String configFilePath;

	public Exception getLastException( )
	{
		return lastException;
	}


	public SiteStatisticsAcquirer( String finishedStatisticsDirectoryPath )
	{
		this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;
	}

	@Override
	public int compareTo( SiteStatisticsAcquirer other )
	{
		return other.statsController.getStatisticsId( ).compareTo( getStatisticsId( ) );
	}

	public SiteStatisticsAcquirer( StatsController statsController )
			throws IOException
	{
//		this.statisticsFilePath = configFilePath;
		this.statsController = statsController;

//		this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;

//		StatsFile statsFile = StatsFile.loadCaptionFromFile( configFilePath );
		statsController.loadStatisticsCaption( );
		Statistics statistics = statsController.getStatistics( );

		initialDateTime = statistics.getInitialDateTime( );

		UnmodifiableArrayList < String > columnQueries = statistics.getColumnQueries( );
		List < SiteDescriptor.ValueDescriptor > valueDescriptors = new ArrayList <>( );

		for ( String columnQuery : columnQueries )
		{
			SiteDescriptor.ValueDescriptor valueDescriptor = new SiteDescriptor.ValueDescriptor( );
			valueDescriptor.query = columnQuery;

			valueDescriptors.add( valueDescriptor );
		}

		siteDescriptor = new SiteDescriptor( statistics.getLink( ) );
		siteDescriptor.setValueDescriptors( valueDescriptors );
		siteDescriptor.setInitialAcquirePeriodInMillis( statistics.getPeriodInMillis( ) );
		siteDescriptor.setDataType( statistics.getDataType( ) );
		siteDescriptor.setDataProcessingMethod( statistics.getDataProcessingMethod( ) );

		siteDescriptor.setTargetColumnSum( statistics.getTargetColumnSum( ) );
		siteDescriptor.setTargetValuePeriodInMinutes( statistics.getTargetValuePeriodInMinutes( ) );

		siteDescriptor.setExpirationPeriodInMinutes( statistics.getExpirationPeriodInMinutes( ) );
	}

	public String getStatisticsId( )
	{
		return statsController.getStatisticsId( );
	}

	public void setSiteDescriptor( SiteDescriptor siteDescriptor )
	{
		this.siteDescriptor = siteDescriptor;

//		setDataType( siteDescriptor.getDataType( ) );
	}

	public void setStatisticsController( StatsController statsController )
	{
		this.statsController = statsController;
	}

	public String getUrl( )
	{
		return siteDescriptor.getUrl( );
	}

	public void start( )
			throws IOException
	{
		statisticsAcquirer = WebDataAcquirerFactory.getDataAcquirer( siteDescriptor.dataType, siteDescriptor.url );

//		StatsFile.DataPoint dataPoint = new StatsFile.DataPoint( );

		Statistics statistics = new Statistics( );
		statistics.setHeadline( siteDescriptor.getHeadline( ) );
		statistics.setLink( siteDescriptor.getUrl( ) );
		statistics.setInitialDateTime( ZonedDateTime.now( ) );

		List < String > columnNames = new ArrayList <>( );
		List < String > columnQueries = new ArrayList <>( );

		for ( SiteDescriptor.ValueDescriptor valueDescriptor : siteDescriptor.valueDescriptors )
		{
			columnNames.add( valueDescriptor.name );
			columnQueries.add( valueDescriptor.query );
		}

		statistics.setColumnNames( columnNames );
		statistics.setColumnQueries( columnQueries );
		statistics.setPeriodInMillis( siteDescriptor.initialAcquirePeriodInMillis );
		statistics.setDataType( siteDescriptor.dataType );
		statistics.setDataProcessingMethod( siteDescriptor.dataProcessingMethod );

		statistics.setTargetColumnSum( siteDescriptor.targetColumnSum );
		statistics.setTargetValuePeriodInMinutes( siteDescriptor.targetValuePeriodInMinutes );
		statistics.setExpirationPeriodInMinutes( siteDescriptor.expirationPeriodInMinutes );

		statsController.initStatistics( statistics );

//		statsFile.setSiteDescriptor( siteDescriptor );
		/*
		File file = new File( statisticsFilePath );

		if ( ! file.exists( ) )
		{
			try
			{
				file.createNewFile( );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}

			try ( FileOutputStream fileOutputStream = new FileOutputStream( file ) )
			{
				fileOutputStream.write( statsFile.getCaption( ).getBytes( "UTF-8" ) );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
		}*/

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
				siteDescriptor.getInitialAcquirePeriodInMillis( )
		);
	}

	public void stop( )
	{
		timer.cancel( );
	}

	public void finish( )
			throws IOException
	{
		stop( );

		statsController.finish( );
	}

	private void appendData( )
	{
		lastException = null;

		try
		{
			statisticsAcquirer.acquireData( );
		}
		catch ( Exception e )
		{
//			System.err.println( e.getMessage( ) );
			lastException = e;

			return;
			//e.printStackTrace( );
		}

		Statistics.DataPoint dataPoint = new Statistics.DataPoint( );

		List < String > data = new ArrayList <>( );

		for ( SiteDescriptor.ValueDescriptor valueDescriptor : siteDescriptor.valueDescriptors )
		{
			data.add( statisticsAcquirer.getValue( valueDescriptor.query ) );
		}

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

/*

//		dataPoint.setSiteDescriptor( siteDescriptor );

		File file = new File( statisticsFilePath );

		try ( FileOutputStream fileOutputStream = new FileOutputStream( file, true ) )
		{
			String line = dataPoint.toLine( );
			fileOutputStream.write( line.getBytes( ) );
		}
		catch ( Exception e )
		{
			lastException = e;

//			e.printStackTrace( );

			return;
		}
		*/

		long minutesSinceAcquiringStart = Duration.between( initialDateTime, ZonedDateTime.now( ) ).toMinutes( );


		if ( minutesSinceAcquiringStart > siteDescriptor.expirationPeriodInMinutes )
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

//					e.printStackTrace( );
				}
			}
		}

		if ( siteDescriptor.checkForTooSmallData &&
				minutesSinceAcquiringStart >= siteDescriptor.targetValuePeriodInMinutes )
		{
			siteDescriptor.checkForTooSmallData = false;

			long columnSum = 0L;

			for ( String columnValue : dataPoint.data )
			{
				columnSum += Long.parseLong( columnValue );
			}

			if ( columnSum < siteDescriptor.targetColumnSum )
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

//						e.printStackTrace( );
					}
				}
			}
		}
	}

	private String getProcessedData( String data )
	{
		if ( siteDescriptor.dataProcessingMethod.isEmpty( ) )
		{
			return data;
		}

		if ( "int".equals( siteDescriptor.dataProcessingMethod ) )
		{
			String processedData = "";

			String digits = "0123456789";

			for ( int i = 0; i < data.length( ); i += 1 )
			{
				if ( digits.contains( new String( new char[]{ data.charAt( i ) } ) ) )
				{
					processedData += data.charAt( i );
				}
			}

			if ( processedData.isEmpty( ) )
			{
				processedData = "0";
			}

			return processedData;
		}

		throw new UnsupportedOperationException( );
	}
}
