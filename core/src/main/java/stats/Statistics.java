package stats;

import com.sun.javafx.UnmodifiableArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nikita on 30.12.2017.
 */
public class Statistics
{
	public static class DataPoint
	{
		public ZonedDateTime dateTime;
		public String[] data;

		public DataPoint( )
		{
			dateTime = ZonedDateTime.now( );
		}
	}

	public static class StatisticsHeader implements Cloneable
	{
		private ZonedDateTime initialDateTime = ZonedDateTime.now( );
		private String url;
		private String headline;
		private List < String > columnNames = new ArrayList <>( );
		private List < String > columnQueries = new ArrayList <>( );
		private int periodInMillis = 60000;
		private DataType dataType;
		private String dataProcessingMethod = "";
		private long targetColumnSum = 1000000000000L;
		private long targetValuePeriodInMinutes = 60;
		private long expirationPeriodInMinutes = 60 * 24 * 30; // 30 days

		public StatisticsHeader( )
		{

		}

		public StatisticsHeader( StatisticsHeader other )
		{
			url = other.url;
			headline = other.headline;
			columnNames = new ArrayList <>( other.columnNames );
			columnQueries = new ArrayList <>( other.columnQueries );
			dataType = other.dataType;
			periodInMillis = other.periodInMillis;
			dataProcessingMethod = other.dataProcessingMethod;
			targetColumnSum = other.targetColumnSum;
			targetValuePeriodInMinutes = other.targetValuePeriodInMinutes;
			expirationPeriodInMinutes = other.expirationPeriodInMinutes;
		}

		public StatisticsHeader( JSONObject configJson )
		{
			url = ( String ) configJson.get( "url" );
			dataType = DataType.fromString( ( String ) configJson.get( "data_type" ) );
			periodInMillis = ( int ) ( long ) configJson.get( "period" );
			if ( configJson.get( "data_processing_method" ) != null )
			{
				dataProcessingMethod = ( String ) configJson.get( "data_processing_method" );
			}
			else
			{
				dataProcessingMethod = "";
			}
			if ( configJson.get( "headline" ) != null )
			{
				headline = ( String ) configJson.get( "headline" );
			}
			else
			{
				headline = "";
			}

			if ( configJson.get( "target_value_sum" ) != null )
			{
				targetColumnSum = ( long ) configJson.get( "target_value_sum" );

				if ( configJson.get( "target_value_period_in_minutes" ) != null )
				{
					targetValuePeriodInMinutes = ( long ) configJson.get( "target_value_period_in_minutes" );
				}
			}

			if ( configJson.get( "expiration_period_in_minutes" ) != null )
			{
				expirationPeriodInMinutes = ( long ) configJson.get( "expiration_period_in_minutes" );
			}

			columnNames = new ArrayList <>( );
			columnQueries = new ArrayList <>( );
			JSONArray valueDescriptorsArray = ( JSONArray ) configJson.get( "value_description" );
			for ( Object valueDescriptorObject : valueDescriptorsArray )
			{
				JSONObject valueDescriptorJson = ( JSONObject ) valueDescriptorObject;

				columnNames.add( ( String ) valueDescriptorJson.get( "name" ) );
				columnQueries.add( ( String ) valueDescriptorJson.get( "query" ) );
			}
		}

		public JSONObject getConfigObject( )
		{
			JSONObject configObject = new JSONObject( );

			configObject.put( "url", url );
			configObject.put( "data_type", dataType.toString( ) );
			configObject.put( "period", periodInMillis );
			configObject.put( "data_processing_method", dataProcessingMethod );
			configObject.put( "target_value_sum", targetColumnSum );
			configObject.put( "target_value_period_in_minutes", targetValuePeriodInMinutes );
			configObject.put( "expiration_period_in_minutes", expirationPeriodInMinutes );

			configObject.put( "headline", headline );

			JSONArray valueDescriptors = new JSONArray( );

			for ( int columnIndex = 0; columnIndex < columnNames.size( ); columnIndex += 1 )
			{
				JSONObject valueDescriptor = new JSONObject( );

				valueDescriptor.put( "name", columnNames.get( columnIndex ) );
				valueDescriptor.put( "query", columnQueries.get( columnIndex ) );

				valueDescriptors.add( valueDescriptor );
			}

			configObject.put( "value_description", valueDescriptors );

			return configObject;
		}

		public void setInitialDateTime( ZonedDateTime initialDateTime )
		{
			this.initialDateTime = initialDateTime;
		}

		public ZonedDateTime getInitialDateTime( )
		{
			return initialDateTime;
		}

		public void setUrl( String url )
		{
			this.url = url;
		}

		public String getUrl( )
		{
			return url;
		}

		public void setHeadline( String headline )
		{
			this.headline = headline;
		}

		public String getHeadline( )
		{
			return headline;
		}

		public void setColumnNames( List < String > columnNames )
		{
			this.columnNames = new ArrayList <>( );
			this.columnNames.addAll( columnNames );
		}

		public UnmodifiableArrayList < String > getColumnNames( )
		{
			String[] columnNamesArray = new String[ columnNames.size( ) ];
			for ( int i = 0; i < columnNames.size( ); i += 1 )
			{
				columnNamesArray[ i ] = columnNames.get( i );
			}

			return new UnmodifiableArrayList <>( columnNamesArray, columnNamesArray.length );
		}

		public void setColumnQueries( List < String > columnQueries )
		{
			this.columnQueries = new ArrayList <>( );
			this.columnQueries.addAll( columnQueries );
		}

		public UnmodifiableArrayList < String > getColumnQueries( )
		{
			String[] columnQueryArray = new String[ columnQueries.size( ) ];
			for ( int i = 0; i < columnQueryArray.length; i += 1 )
			{
				columnQueryArray[ i ] = columnQueries.get( i );
			}

			return new UnmodifiableArrayList <>( columnQueryArray, columnQueryArray.length );
		}

		public void setPeriodInMillis( int periodInMillis )
		{
			this.periodInMillis = periodInMillis;
		}

		public int getPeriodInMillis( )
		{
			return periodInMillis;
		}

		public void setDataType( DataType dataType )
		{
			this.dataType = dataType;
		}

		public DataType getDataType( )
		{
			return dataType;
		}

		public void setDataProcessingMethod( String dataProcessingMethod )
		{
			this.dataProcessingMethod = dataProcessingMethod;
		}

		public String getDataProcessingMethod( )
		{
			return dataProcessingMethod;
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

		public String getStatisticsId( )
		{
			return getUrl( );
		}
	}


	private StatisticsHeader header = new StatisticsHeader( );
	private List < DataPoint > dataPoints = new ArrayList <>( );


	public StatisticsHeader getHeader( )
	{
		return header;
	}

	public void setHeader( StatisticsHeader statisticsHeader )
	{
		this.header = statisticsHeader;
	}

	public void addDataPoint( DataPoint dataPoint )
	{
		dataPoints.add( dataPoint );
	}

	public List < DataPoint > getDataPoints( )
	{
		return Collections.unmodifiableList( dataPoints );
	}
}
