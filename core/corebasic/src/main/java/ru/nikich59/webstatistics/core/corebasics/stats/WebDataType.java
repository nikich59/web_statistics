package ru.nikich59.webstatistics.core.corebasics.stats;

/**
 * Created by Nikita on 27.12.2017.
 */
public enum WebDataType
{
	XML,
	JSON;

	@Override
	public String toString( )
	{
		switch ( this )
		{
			case XML:
				return "xml";
			case JSON:
				return "json";
		}

		throw new UnsupportedOperationException( );
	}

	public static WebDataType fromString( String s )
	{
		String dataTypeString = s.toLowerCase( );

		switch ( dataTypeString )
		{
			case "xml":
				return XML;
			case "json":
				return JSON;
		}

		throw new UnsupportedOperationException( );
	}
}
