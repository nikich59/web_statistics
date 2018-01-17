package ru.nikich59.webstatistics.statister.webdataacquirer;

import ru.nikich59.webstatistics.core.corebasics.stats.WebDataType;

/**
 * Created by Nikita on 27.12.2017.
 */
public class WebDataAcquirerFactory
{
	public static WebDataAcquirer getDataAcquirer( WebDataType dataType, String url )
	{
		switch ( dataType )
		{
			case XML:
				return new WebDataAcquirerXML( url );
			case JSON:
				return new WebDataAcquirerJSON( url );
		}

		throw new UnsupportedOperationException( );
	}
}
