package stats;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 27.12.2017.
 */
public class StatsFile
{
	public static final String COLUMN_SEPARATOR = " ";
	public static final String TIMESTAMP_COLUMN_NAME = "timestamp";
	public static final String COLUMN_QUERY_WRAP_SYMBOL = "\"";
	public static final int CAPTION_LINE_COUNT = 16;

	public static final int CAPTION_INDEX_INITIAL_DATETIME_LINE = 0;
	public static final int CAPTION_INDEX_LINK = 1;
	public static final int CAPTION_INDEX_HEADLINE = 2;
	public static final int CAPTION_INDEX_COLUMN_NAMES = 3;
	public static final int CAPTION_INDEX_COLUMN_QUERIES = 4;
	public static final int CAPTION_INDEX_DATA_PROCESSING_METHOD = 5;
	public static final int CAPTION_INDEX_PERIOD_IN_MILLIS = 6;
	public static final int CAPTION_INDEX_DATA_TYPE = 7;
	public static final int CAPTION_INDEX_TARGET_COLUMN_SUM = 8;
	public static final int CAPTION_INDEX_TARGET_PERIOD_IN_MINUTES = 9;
	public static final int CAPTION_INDEX_EXPIRATION_PERIOD_IN_MINUTES = 10;

	private Statistics statistics = new Statistics( );

	public Statistics getStatistics( )
	{
		return statistics;
	}

	public void setStatistics( Statistics statistics )
	{
		this.statistics = statistics;
	}

	public static StatsFile loadCaptionFromFile( String fileName )
			throws IOException
	{
		File file = new File( fileName );
		if ( ! file.exists( ) || ! file.isFile( ) )
		{
			throw new IOException( "File does not exist" );
		}

		StatsFile statsFile = new StatsFile( );

		try ( FileReader fileReader = new FileReader( file );
			  BufferedReader bufferedReader = new BufferedReader( fileReader ) )
		{
			List < String > captionLines = new ArrayList <>( );

			for ( int i = 0; i < CAPTION_LINE_COUNT; i += 1 )
			{
				captionLines.add( bufferedReader.readLine( ) );
			}

			String initialTimeStamp = captionLines.get( CAPTION_INDEX_INITIAL_DATETIME_LINE );
			String link = captionLines.get( CAPTION_INDEX_LINK );
			String headline = captionLines.get( CAPTION_INDEX_HEADLINE );
			String columnNames = captionLines.get( CAPTION_INDEX_COLUMN_NAMES );
			String columnQueries = captionLines.get( CAPTION_INDEX_COLUMN_QUERIES );
			String periodInMillisString = captionLines.get( CAPTION_INDEX_PERIOD_IN_MILLIS );
			String dataTypeString = captionLines.get( CAPTION_INDEX_DATA_TYPE );
			String dataProcessingMethodString = captionLines.get( CAPTION_INDEX_DATA_PROCESSING_METHOD );
			String targetColumnSumString = captionLines.get( CAPTION_INDEX_TARGET_COLUMN_SUM );
			String targetValuePeriodInMinutesString = captionLines.get( CAPTION_INDEX_TARGET_PERIOD_IN_MINUTES );
			String expirationPeriodInMinutesString = captionLines.get( CAPTION_INDEX_EXPIRATION_PERIOD_IN_MINUTES );

			List < String > columnNamesArray = separateWrappedParts( columnNames, COLUMN_QUERY_WRAP_SYMBOL );
			/*
			if ( columnNamesArray[ 0 ].equals( TIMESTAMP_COLUMN_NAME ) )
			{
				columnNamesArray = Arrays.copyOfRange( columnNamesArray, 1, columnNamesArray.length - 1 );
			}*/

			List < String > columnQueriesList = separateWrappedParts( columnQueries, COLUMN_QUERY_WRAP_SYMBOL );


			statsFile.statistics.setInitialDateTime( ZonedDateTime.parse( initialTimeStamp ) );
			statsFile.statistics.setLink( link );
			statsFile.statistics.setHeadline( headline );
			statsFile.statistics.setColumnNames( columnNamesArray );
			statsFile.statistics.setColumnQueries( columnQueriesList );
			statsFile.statistics.setPeriodInMillis( Integer.parseInt( periodInMillisString ) );
			statsFile.statistics.setDataType( DataType.fromString( dataTypeString ) );
			statsFile.statistics.setDataProcessingMethod( dataProcessingMethodString );
			statsFile.statistics.setTargetColumnSum( Long.parseLong( targetColumnSumString ) );
			statsFile.statistics.setTargetValuePeriodInMinutes( Long.parseLong( targetValuePeriodInMinutesString ) );
			statsFile.statistics.setExpirationPeriodInMinutes( Long.parseLong( expirationPeriodInMinutesString ) );
		}
		catch ( Exception e )
		{
			throw e;
		}

		return statsFile;
	}

	private static List < String > separateWrappedParts( String source, String wrapSymbol )
	{
		String sourceCopy = source;
		List < String > columnQueriesList = new ArrayList <>( );

		while ( sourceCopy.contains( wrapSymbol ) )
		{
			int beginIndex = sourceCopy.indexOf( wrapSymbol );
			int endIndex = sourceCopy.indexOf( wrapSymbol, beginIndex + 1 );

			if ( endIndex >= 0 )
			{
				String part = sourceCopy.substring( beginIndex + 1, endIndex );
				columnQueriesList.add( part );
			}
			else
			{
				break;
			}

			if ( sourceCopy.length( ) <= endIndex + 2 )
			{
				break;
			}

			sourceCopy = sourceCopy.substring( endIndex + 2 );
		}

		return columnQueriesList;
	}

	public static StatsFile loadFromFile( String fileName )
			throws IOException
	{
		File file = new File( fileName );
		if ( ! file.exists( ) )
		{
			throw new IOException( "File does not exist" );
		}

		StatsFile statsFile = loadCaptionFromFile( fileName );
		if ( statsFile == null )
		{
			throw new IOException( "File cannot be read" );
		}

		try ( FileReader fileReader = new FileReader( file );
			  BufferedReader bufferedReader = new BufferedReader( fileReader ) )
		{
			for ( int i = 0; i < CAPTION_LINE_COUNT; i += 1 )
			{
				bufferedReader.readLine( );
			}

			String line;
			while ( ( line = bufferedReader.readLine( ) ) != null )
			{
				statsFile.statistics.addDataPoint( dataPointFromLine( line ) );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}

		return statsFile;
	}

	private static Statistics.DataPoint dataPointFromLine( String line )
	{
		String[] values = line.split( " " );

		Statistics.DataPoint dataPoint = new Statistics.DataPoint( );
		dataPoint.dateTime = ZonedDateTime.parse( values[ 0 ] );
		dataPoint.data = new String[ values.length - 1 ];

		for ( int i = 1; i < values.length; i += 1 )
		{
			dataPoint.data[ i - 1 ] = values[ i ];
		}

		return dataPoint;
	}

	private static String dataPointToLine( Statistics.DataPoint dataPoint )
			throws IOException
	{
		String line = dataPoint.dateTime.toString( );

		for ( String s : dataPoint.data )
		{
			line += COLUMN_SEPARATOR + s;
		}

		line += System.lineSeparator( );

		return line;
	}

	private String getCaption( )
	{
		String caption = "";

		String[] captionLines = new String[ CAPTION_LINE_COUNT ];
		for ( int i = 0; i < captionLines.length; i += 1 )
		{
			captionLines[ i ] = "";
		}

		captionLines[ CAPTION_INDEX_INITIAL_DATETIME_LINE ] =
				statistics.getInitialDateTime( ).toString( );
		captionLines[ CAPTION_INDEX_LINK ] = statistics.getLink( );
		captionLines[ CAPTION_INDEX_HEADLINE ] = statistics.getHeadline( );

		captionLines[ CAPTION_INDEX_COLUMN_NAMES ] = TIMESTAMP_COLUMN_NAME;
		for ( String columnName : statistics.getColumnNames( ) )
		{
			captionLines[ CAPTION_INDEX_COLUMN_NAMES ] += COLUMN_SEPARATOR + "\"" + columnName + "\"";
		}

		captionLines[ CAPTION_INDEX_COLUMN_QUERIES ] = TIMESTAMP_COLUMN_NAME;
		for ( String columnQuery : statistics.getColumnQueries( ) )
		{
			captionLines[ CAPTION_INDEX_COLUMN_QUERIES ] += COLUMN_SEPARATOR + "\"" + columnQuery + "\"";
		}

		captionLines[ CAPTION_INDEX_PERIOD_IN_MILLIS ] = String.valueOf( statistics.getPeriodInMillis( ) );

		captionLines[ CAPTION_INDEX_DATA_TYPE ] = statistics.getDataType( ).toString( );

		captionLines[ CAPTION_INDEX_DATA_PROCESSING_METHOD ] = statistics.getDataProcessingMethod( );

		captionLines[ CAPTION_INDEX_TARGET_COLUMN_SUM ] = String.valueOf( statistics.getTargetColumnSum( ) );

		captionLines[ CAPTION_INDEX_TARGET_PERIOD_IN_MINUTES ] = String.valueOf( statistics.getTargetValuePeriodInMinutes( ) );

		captionLines[ CAPTION_INDEX_EXPIRATION_PERIOD_IN_MINUTES ] = String.valueOf( statistics.getExpirationPeriodInMinutes( ) );


		for ( String captionLine : captionLines )
		{
			caption += captionLine + System.lineSeparator( );
		}

		return caption;
	}

	public void writeToFile( String filePath )
			throws IOException
	{
		File file = new File( filePath );
		if ( ! file.exists( ) || ! file.isFile( ) )
		{
			throw new IOException( "No such file: \'" + filePath + "\'" );
		}

		FileWriter fileWriter = new FileWriter( file );

		fileWriter.write( getCaption( ) );

		for ( Statistics.DataPoint dataPoint : statistics.getDataPoints() )
		{
			fileWriter.write( dataPointToLine( dataPoint ) );
		}

		fileWriter.close( );
	}

	public static void appendData( String filePath, Statistics.DataPoint dataPoint )
			throws IOException
	{
		File file = new File( filePath );

		try ( FileOutputStream fileOutputStream = new FileOutputStream( file, true ) )
		{
			String line = dataPointToLine( dataPoint );
			fileOutputStream.write( line.getBytes( ) );
		}
		catch ( Exception e )
		{
			throw e;
		}
	}

	public static void appendToFile( Statistics.DataPoint dataPoint, String filePath )
			throws IOException
	{
		File file = new File( filePath );
		if ( ! file.exists( ) || ! file.isFile( ) )
		{
			throw new IOException( "No such file: \'" + filePath + "\'" );
		}

		FileWriter fileWriter = new FileWriter( file, true );

		fileWriter.write( dataPointToLine( dataPoint ) );

		fileWriter.close( );
	}
}

















