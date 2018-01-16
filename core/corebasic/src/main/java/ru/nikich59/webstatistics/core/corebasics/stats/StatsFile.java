package ru.nikich59.webstatistics.core.corebasics.stats;

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
	public static final String COLUMN_WRAP_SYMBOL = "\"";
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
	public static final int CAPTION_INDEX_COLUMN_DATA_TYPE = 11;
	public static final int CAPTION_INDEX_IS_FINISHED = 12;

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
			throw new IOException( "File \'" + file.getAbsolutePath( ) + "\' does not exist" );
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
			String columnDataTypeStrings = captionLines.get( CAPTION_INDEX_COLUMN_DATA_TYPE );
			String ifFinishedString = captionLines.get( CAPTION_INDEX_IS_FINISHED );
			String periodInMillisString = captionLines.get( CAPTION_INDEX_PERIOD_IN_MILLIS );
			String dataTypeString = captionLines.get( CAPTION_INDEX_DATA_TYPE );
			String dataProcessingMethodString = captionLines.get( CAPTION_INDEX_DATA_PROCESSING_METHOD );
			String targetColumnSumString = captionLines.get( CAPTION_INDEX_TARGET_COLUMN_SUM );
			String targetValuePeriodInMinutesString = captionLines.get( CAPTION_INDEX_TARGET_PERIOD_IN_MINUTES );
			String expirationPeriodInMinutesString = captionLines.get( CAPTION_INDEX_EXPIRATION_PERIOD_IN_MINUTES );

			List < String > columnNamesList = separateWrappedColumns( columnNames );
			List < String > columnQueriesList = separateWrappedColumns( columnQueries );
			List < String > columnDataTypeStringList =
					separateWrappedColumns( columnDataTypeStrings );

			List < Statistics.ValueDataType > columnDataTypeList = new ArrayList <>( );
			columnDataTypeStringList.forEach( ( s ) ->
					columnDataTypeList.add( Statistics.ValueDataType.fromString( s ) )
			);

			if ( columnNamesList.size( ) != columnQueriesList.size( ) )
			{
				throw new StatsFileFormatException( "Column query count does not match column name count" );
			}

			if ( columnNamesList.size( ) != columnDataTypeList.size( ) )
			{
				throw new StatsFileFormatException( "Column data type count does not match column name count" );
			}

			List < Statistics.ValueDescription > valueDescriptions = new ArrayList <>( );
			for ( int columnIndex = 0; columnIndex < columnNamesList.size( ); columnIndex += 1 )
			{
				valueDescriptions.add( new Statistics.ValueDescription(
						columnNamesList.get( columnIndex ),
						columnQueriesList.get( columnIndex ),
						columnDataTypeList.get( columnIndex ) )
				);
			}

			statsFile.getStatistics( ).getHeader( ).clearValueDescriptions( );
			statsFile.getStatistics( ).getHeader( ).addValueDescriptions( valueDescriptions );

			statsFile.statistics.getHeader( ).setInitialDateTime( ZonedDateTime.parse( initialTimeStamp ) );
			statsFile.statistics.getHeader( ).setUrl( link );
			statsFile.statistics.getHeader( ).setHeadline( headline );


			statsFile.statistics.getHeader( ).setPeriodInMillis( Integer.parseInt( periodInMillisString ) );
			statsFile.statistics.getHeader( ).setDataType( WebDataType.fromString( dataTypeString ) );
//			statsFile.statistics.getHeader( ).setDataProcessingMethod( dataProcessingMethodString );
			statsFile.statistics.getHeader( ).setFinished( Boolean.parseBoolean( ifFinishedString ) );
			statsFile.statistics.getHeader( ).setTargetColumnSum( Long.parseLong( targetColumnSumString ) );
			statsFile.statistics.getHeader( )
					.setTargetValuePeriodInMinutes( Long.parseLong( targetValuePeriodInMinutesString ) );
			statsFile.statistics.getHeader( )
					.setExpirationPeriodInMinutes( Long.parseLong( expirationPeriodInMinutesString ) );
		}
		catch ( Exception e )
		{
			throw e;
		}

		return statsFile;
	}

	private static List < String > separateWrappedColumns( String line )
	{
		return separateWrappedParts( line, COLUMN_WRAP_SYMBOL );
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
			throws IOException, StatsFileFormatException
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
				Statistics.DataPoint dataPoint = dataPointFromLine( line,
						statsFile.getStatistics( ).getHeader( ).getValueDescriptions( ).size( ) );

				if ( dataPoint == null )
				{
					continue;
				}
				statsFile.statistics.addDataPoint( dataPoint );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}

		return statsFile;
	}

	private static Statistics.DataPoint dataPointFromLine( String line, int valueCount )
	{
		if ( line.isEmpty( ) )
		{
			return null;
		}
		String[] values = line.split( " " );

		if ( valueCount > 0 && values.length - 1 != valueCount )
		{
			return null;
			/*
			throw new StatsFileFormatException( "Value count in line: \'" + line +
					"\' does not match the required " + String.valueOf( valueCount ) );
					*/
		}

		Statistics.DataPoint dataPoint = new Statistics.DataPoint( );
		dataPoint.setDateTime( ZonedDateTime.parse( values[ 0 ] ) );

		List < String > valueList = new ArrayList <>( );

		for ( int valueIndex = 1; valueIndex < values.length; valueIndex += 1 )
		{
			valueList.add( values[ valueIndex ] );
		}

		dataPoint.setData( valueList );

		return dataPoint;
	}

	private static String dataPointToLine( Statistics.DataPoint dataPoint )
			throws IOException
	{
		String line = dataPoint.getDateTime( ).toString( );

		for ( String s : dataPoint.getData( ) )
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
				statistics.getHeader( ).getInitialDateTime( ).toString( );
		captionLines[ CAPTION_INDEX_LINK ] = statistics.getHeader( ).getUrl( );
		captionLines[ CAPTION_INDEX_HEADLINE ] = statistics.getHeader( ).getHeadline( );

		captionLines[ CAPTION_INDEX_COLUMN_NAMES ] = TIMESTAMP_COLUMN_NAME;

		captionLines[ CAPTION_INDEX_COLUMN_QUERIES ] = TIMESTAMP_COLUMN_NAME;

		captionLines[ CAPTION_INDEX_COLUMN_DATA_TYPE ] = TIMESTAMP_COLUMN_NAME;
		for ( Statistics.ValueDescription valueDescription : statistics.getHeader( ).getValueDescriptions( ) )
		{
			captionLines[ CAPTION_INDEX_COLUMN_NAMES ] +=
					COLUMN_SEPARATOR + getWrappedColumn( valueDescription.getName( ) );
			captionLines[ CAPTION_INDEX_COLUMN_QUERIES ] +=
					COLUMN_SEPARATOR + getWrappedColumn( valueDescription.getQuery( ) );
			captionLines[ CAPTION_INDEX_COLUMN_DATA_TYPE ] +=
					COLUMN_SEPARATOR + getWrappedColumn( valueDescription.getDataType( ).toString( ) );
		}


		captionLines[ CAPTION_INDEX_PERIOD_IN_MILLIS ] =
				String.valueOf( statistics.getHeader( ).getPeriodInMillis( ) );

		captionLines[ CAPTION_INDEX_IS_FINISHED ] = String.valueOf( getStatistics( ).getHeader( ).isFinished( ) );

		captionLines[ CAPTION_INDEX_DATA_TYPE ] = statistics.getHeader( ).getDataType( ).toString( );
/*
		captionLines[ CAPTION_INDEX_DATA_PROCESSING_METHOD ] =
				statistics.getHeader( ).getDataProcessingMethod( );
*/
		captionLines[ CAPTION_INDEX_TARGET_COLUMN_SUM ] =
				String.valueOf( statistics.getHeader( ).getTargetColumnSum( ) );

		captionLines[ CAPTION_INDEX_TARGET_PERIOD_IN_MINUTES ] =
				String.valueOf( statistics.getHeader( ).getTargetValuePeriodInMinutes( ) );

		captionLines[ CAPTION_INDEX_EXPIRATION_PERIOD_IN_MINUTES ] =
				String.valueOf( statistics.getHeader( ).getExpirationPeriodInMinutes( ) );


		for ( String captionLine : captionLines )
		{
			captionLine = captionLine.replace( "\n", "" );
			caption += captionLine + System.lineSeparator( );
		}

		return caption;
	}

	private String getWrappedColumn( String column )
	{
		return COLUMN_WRAP_SYMBOL + column + COLUMN_WRAP_SYMBOL;
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

		for ( Statistics.DataPoint dataPoint : statistics.getDataPoints( ) )
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

















