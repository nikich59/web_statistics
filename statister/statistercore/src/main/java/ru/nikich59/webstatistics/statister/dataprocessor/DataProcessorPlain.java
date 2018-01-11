package ru.nikich59.webstatistics.statister.dataprocessor;

/**
 * Created by Nikita on 10.01.2018.
 */
public class DataProcessorPlain implements DataProcessor
{
	@Override
	public String getProcessedData( String rawData )
	{
		return rawData;
	}
}
