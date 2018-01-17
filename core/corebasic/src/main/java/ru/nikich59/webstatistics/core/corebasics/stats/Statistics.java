package ru.nikich59.webstatistics.core.corebasics.stats;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Nikita on 30.12.2017.
 */
public class Statistics
{
	public static class DataPoint
	{
		private ZonedDateTime dateTime;
		public ZonedDateTime getDateTime( )
		{
			return dateTime;
		}
		public void setDateTime( ZonedDateTime dateTime )
		{
			this.dateTime = dateTime;
		}
		public List < String > getData( )
		{
			return data;
		}
		public void setData( List < String > data )
		{
			this.data = data;
		}
		private List < String > data = new ArrayList <>( );

		public DataPoint( )
		{
			dateTime = ZonedDateTime.now( );
		}

		public DataPoint( ZonedDateTime dateTime, List < String > data )
		{
			this.dateTime = dateTime;
			this.data = data;
		}
	}

	public static abstract class ValueDataType
	{
		@Override
		public abstract String toString( );

		public static ValueDataType fromString( String s )
		{
			if ( s == null || s.isEmpty( ) )
			{
				return new ValueDataTypeString( );
			}

			for ( ValueDataType valueDataType : getAvailableDataTypes( ) )
			{
				if ( s.equals( valueDataType.toString( ) ) )
				{
					return valueDataType;
				}
			}

			return null;
		}

		public static List < ValueDataType > getAvailableDataTypes( )
		{
			List < ValueDataType > availableDataTypes = new ArrayList <>( );

			availableDataTypes.add( new ValueDataTypeInt( ) );
			availableDataTypes.add( new ValueDataTypeString( ) );

			return availableDataTypes;
		}

		public abstract String getProcessedData( String sourceData );

		protected final String getStringWithAlphabetSymbols( String sourceString, String alphabet )
		{
			StringBuilder dataBuilder = new StringBuilder( );

			for ( int charIndex = 0; charIndex < sourceString.length( ); charIndex += 1 )
			{
				if ( alphabet.indexOf( sourceString.charAt( charIndex ) ) >= 0 )
				{
					dataBuilder.append( sourceString.charAt( charIndex ) );
				}
			}

			return dataBuilder.toString( );
		}
	}

	public static class ValueDataTypeInt extends ValueDataType
	{
		private static final String TYPE_STRING = "int";

		@Override
		public String toString( )
		{
			return TYPE_STRING;
		}

		@Override
		public String getProcessedData( String sourceData )
		{
			if ( sourceData == null || sourceData.isEmpty( ) )
			{
				return "0";
			}

			return getStringWithAlphabetSymbols( sourceData, "0123456789" );
		}
	}

	public static class ValueDataTypeString extends ValueDataType
	{
		private static final String TYPE_STRING = "string";

		@Override
		public String toString( )
		{
			return TYPE_STRING;
		}

		@Override
		public String getProcessedData( String sourceData )
		{
			return sourceData;
		}
	}

	public static class ValueDescription implements Cloneable
	{
		public ValueDescription( )
		{

		}

		public ValueDescription( Map < String, Object > configMap )
		{
			setName( configMap.get( "name" ).toString( ) );
			setQuery( configMap.get( "query" ).toString( ) );
			if ( configMap.get( "data_type" ) == null )
			{
				setDataType( ValueDataType.fromString( "" ) );
			}
			else
			{
				setDataType( ValueDataType.fromString( configMap.get( "data_type" ).toString( ) ) );
			}
		}

		public ValueDescription( String name, String query, ValueDataType dataType )
		{
			setName( name );
			setQuery( query );
			setDataType( dataType );
		}

		public Map < String, Object > toConfigMap( )
		{
			Map < String, Object > configMap = new HashMap <>( );

			configMap.put( "name", getName( ) );
			configMap.put( "query", getQuery( ) );
			configMap.put( "data_type", getDataType( ).toString( ) );

			return configMap;
		}

		private String name;
		public String getName( )
		{
			return name;
		}
		public void setName( String name )
		{
			this.name = name;
		}
		public String getQuery( )
		{
			return query;
		}
		public void setQuery( String query )
		{
			this.query = query;
		}
		public ValueDataType getDataType( )
		{
			return dataType;
		}
		public void setDataType( ValueDataType dataType )
		{
			this.dataType = dataType;
		}
		private String query;
		private ValueDataType dataType = new ValueDataTypeString( );

		@Override
		public ValueDescription clone( )
		{
			ValueDescription clone = new ValueDescription( );

			clone.setName( getName( ) );
			clone.setDataType( getDataType( ) );
			clone.setQuery( getQuery( ) );

			return clone;
		}
	}

	public static class StatisticsHeader implements Cloneable
	{
		private ZonedDateTime initialDateTime = ZonedDateTime.now( );
		private String url;
		private String headline;
		public List < ValueDescription > getValueDescriptions( )
		{
			return Collections.unmodifiableList( valueDescriptions );
		}
		public void clearValueDescriptions( )
		{
			valueDescriptions.clear( );
		}
		public void addValueDescriptions( List < ValueDescription > valueDescriptions )
		{
			this.valueDescriptions.addAll( valueDescriptions );
		}

		private List < ValueDescription > valueDescriptions = new ArrayList <>( );
		private long periodInMillis = 60000;
		private WebDataType dataType;
		//		private String dataProcessingMethod = "";
		private long targetColumnSum = 1000000000000L;
		private long targetValuePeriodInMinutes = 60;
		private long expirationPeriodInMinutes = 60 * 24 * 30; // 30 days
		public boolean isFinished( )
		{
			return isFinished;
		}
		public void setFinished( boolean finished )
		{
			isFinished = finished;
		}
		private boolean isFinished;

		public StatisticsHeader( )
		{

		}

		public StatisticsHeader( StatisticsHeader other )
		{
			url = other.url;
			headline = other.headline;

			valueDescriptions = new ArrayList <>( );
			other.getValueDescriptions( ).forEach( ( d ) ->
					valueDescriptions.add( d.clone( ) )
			);

			setFinished( other.isFinished( ) );
			dataType = other.dataType;
			periodInMillis = other.periodInMillis;
//			dataProcessingMethod = other.dataProcessingMethod;
			targetColumnSum = other.targetColumnSum;
			targetValuePeriodInMinutes = other.targetValuePeriodInMinutes;
			expirationPeriodInMinutes = other.expirationPeriodInMinutes;
		}

		public StatisticsHeader( Map < String, Object > configMap )
		{
			url = ( String ) configMap.get( "url" );
			dataType = WebDataType.fromString( ( String ) configMap.get( "data_type" ) );
			periodInMillis = Long.parseLong( configMap.get( "period" ).toString( ) );
			/*
			if ( configMap.get( "data_processing_method" ) != null )
			{
				dataProcessingMethod = ( String ) configMap.get( "data_processing_method" );
			}
			else
			{
				dataProcessingMethod = "";
			}
*/
			if ( configMap.get( "headline" ) != null )
			{
				headline = ( String ) configMap.get( "headline" );
			}
			else
			{
				headline = "";
			}

			if ( configMap.get( "is_finished" ) != null )
			{
				isFinished = Boolean.parseBoolean( configMap.get( "is_finished" ).toString( ) );
			}

			if ( configMap.get( "target_value_sum" ) != null )
			{
				targetColumnSum = Long.parseLong( configMap.get( "target_value_sum" ).toString( ) );

				if ( configMap.get( "target_value_period_in_minutes" ) != null )
				{
					targetValuePeriodInMinutes = Long.parseLong( configMap.get( "target_value_period_in_minutes" )
							.toString( ) );
				}
			}

			if ( configMap.get( "expiration_period_in_minutes" ) != null )
			{
				expirationPeriodInMinutes = Long.parseLong( configMap.get( "expiration_period_in_minutes" )
						.toString( ) );
			}

			List < Object > valueDescriptorsList = ( List < Object > ) configMap.get( "value_description" );
			for ( Object valueDescriptorObject : valueDescriptorsList )
			{
				valueDescriptions.add( new ValueDescription( ( Map < String, Object > ) valueDescriptorObject ) );
			}
		}

		public Map < String, Object > getConfigMap( )
		{
			Map < String, Object > configMap = new HashMap <>( );

			configMap.put( "url", url );
			configMap.put( "data_type", dataType.toString( ) );
			configMap.put( "period", periodInMillis );
//			configMap.put( "data_processing_method", dataProcessingMethod );
			configMap.put( "target_value_sum", targetColumnSum );
			configMap.put( "target_value_period_in_minutes", targetValuePeriodInMinutes );
			configMap.put( "expiration_period_in_minutes", expirationPeriodInMinutes );

			configMap.put( "headline", headline );

			List < Object > valueDescriptorsArray = new ArrayList <>( );

			for ( ValueDescription valueDescription : valueDescriptions )
			{
				valueDescriptorsArray.add( valueDescription.toConfigMap( ) );
			}

			configMap.put( "value_description", valueDescriptorsArray );

			return configMap;
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

		public void setPeriodInMillis( int periodInMillis )
		{
			this.periodInMillis = periodInMillis;
		}

		public long getPeriodInMillis( )
		{
			return periodInMillis;
		}

		public void setDataType( WebDataType dataType )
		{
			this.dataType = dataType;
		}

		public WebDataType getDataType( )
		{
			return dataType;
		}
		/*
				public void setDataProcessingMethod( String dataProcessingMethod )
				{
					this.dataProcessingMethod = dataProcessingMethod;
				}

				public String getDataProcessingMethod( )
				{
					return dataProcessingMethod;
				}
		*/
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

	public void setDataPoints( List < DataPoint > dataPoints )
	{
		this.dataPoints = dataPoints;
	}
}
