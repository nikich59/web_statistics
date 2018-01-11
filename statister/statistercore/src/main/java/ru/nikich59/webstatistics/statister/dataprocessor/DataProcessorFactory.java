package ru.nikich59.webstatistics.statister.dataprocessor;

/**
 * Created by Nikita on 10.01.2018.
 */
public class DataProcessorFactory
{
	public static final String PROCESSOR_TYPE_INT_STRING = "int";
	public static final String PROCESSOR_TYPE_PLAIN_STRING = "";


	public static DataProcessor getDataProcessor( String dataProcessorDescription )
	{
		switch ( dataProcessorDescription )
		{
			case PROCESSOR_TYPE_INT_STRING:
				return new DataProcessorInt( );
			case PROCESSOR_TYPE_PLAIN_STRING:
				return new DataProcessorPlain( );
		}

		throw new UnsupportedOperationException(
				"Data processor description \'" + dataProcessorDescription + "\' cannot be interpreted" );
	}
}
