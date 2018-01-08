package stats;

import com.sun.javafx.UnmodifiableArrayList;

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

	public static class StatisticsHeader
	{
		private ZonedDateTime initialDateTime;
		private String link;
		private String headline;
		private List < String > columnNames = new ArrayList <>( );
		private List < String > columnQueries = new ArrayList <>( );
		private int periodInMillis;
		private DataType dataType;
		private String dataProcessingMethod = "";
		private long targetColumnSum;
		private long targetValuePeriodInMinutes;
		private long expirationPeriodInMinutes;

		public StatisticsHeader( )
		{
			initialDateTime = ZonedDateTime.now( );
		}

		public void setInitialDateTime( ZonedDateTime initialDateTime )
		{
			this.initialDateTime = initialDateTime;
		}

		public ZonedDateTime getInitialDateTime( )
		{
			return initialDateTime;
		}

		public void setLink( String link )
		{
			this.link = link;
		}

		public String getLink( )
		{
			return link;
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
			return getLink( );
		}
	}


	private StatisticsHeader header;
	private List < DataPoint > dataPoints = new ArrayList <>( );


	public StatisticsHeader getHeader( )
	{
		return header;
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
