package ru.nikich59.webstatistics.statister.dataprocessor;

/**
 * Created by Nikita on 10.01.2018.
 */
public class DataProcessorInt implements DataProcessor
{
	@Override
	public String getProcessedData( String rawData )
	{
		String processedData = "";

		String digits = "0123456789";

		for ( int i = 0; i < rawData.length( ); i += 1 )
		{
			if ( digits.contains( new String( new char[]{ rawData.charAt( i ) } ) ) )
			{
				processedData += rawData.charAt( i );
			}
		}

		if ( processedData.isEmpty( ) )
		{
			processedData = "0";
		}

		return processedData;
	}
}
