package stats;

/**
 * Created by Nikita on 27.12.2017.
 */
public enum DataType
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

	public static DataType fromString( String s )
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
