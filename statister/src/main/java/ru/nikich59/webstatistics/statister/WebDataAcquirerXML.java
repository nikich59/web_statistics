package ru.nikich59.webstatistics.statister;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by Nikita on 24.12.2017.
 */

public class WebDataAcquirerXML implements WebDataAcquirer
{
	public static final String QUERY_PART_SEPARATOR = " ";
	public static final String QUERY_INDEX_PREFIX = "$index$";
	public static final String QUERY_ATTRIBUTE_PREFIX = "$attr$";


	private String url = "";
	private Document document;

	public WebDataAcquirerXML( String url )
	{
		this.url = url;
	}

	@Override
	public WebDataAcquirerXML acquireData( )
			throws IOException
	{
		document = Jsoup.connect( url ).get( );

		return this;
	}

	@Override
	public String getValue( String query )
	{
		if ( query.isEmpty( ) )
		{
			return "";
		}

		String[] queryParts = query.split( QUERY_PART_SEPARATOR );

		if ( queryParts.length == 0 )
		{
			return "";
		}

		String currentQuery = "";
		Element currentElement = document;

		for ( String queryPart : queryParts )
		{
			if ( queryPart.startsWith( QUERY_INDEX_PREFIX ) )
			{
				int index = Integer.parseInt( queryPart.substring( QUERY_INDEX_PREFIX.length( ) ) );

				currentElement = currentElement.select( currentQuery ).get( index );

				currentQuery = "";

				continue;
			}

			if ( queryPart.startsWith( QUERY_ATTRIBUTE_PREFIX ) )
			{
				String attributeName = queryPart.substring( QUERY_ATTRIBUTE_PREFIX.length( ) );

				if ( currentQuery.isEmpty( ) )
				{
					return currentElement.attr( attributeName );
				}

				currentElement = currentElement.selectFirst( currentQuery );

				return currentElement.attr( attributeName );
			}

			currentQuery += QUERY_PART_SEPARATOR + queryPart;
		}

		if ( !currentQuery.isEmpty( ) )
		{
			currentElement = currentElement.selectFirst( currentQuery );
		}

		return currentElement.html( );
	}

	public String getDocument( )
	{
		return document.toString( );
	}
}
