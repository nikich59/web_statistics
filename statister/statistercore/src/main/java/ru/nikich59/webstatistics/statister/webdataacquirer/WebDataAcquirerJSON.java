package ru.nikich59.webstatistics.statister.webdataacquirer;


import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nikita on 25.12.2017.
 */

public class WebDataAcquirerJSON implements WebDataAcquirer
{
	private String url = "";
	private JSONObject document;

	public WebDataAcquirerJSON( String url )
	{
		this.url = url;
	}

	@Override
	public WebDataAcquirerJSON acquireData( )
			throws AcquiringException
	{
		try
		{
			String data = readDataFromUrl( url );
			document = ( JSONObject ) new JSONParser( ).parse( data );
		}
		catch ( Exception e )
		{
			throw new AcquiringException( e.getMessage( ) );
		}

		return this;
	}

	@Override
	public String getValue( String query )
	{
		DataSelectorMap dataSelectorMap = new DataSelectorMap( document );

		return dataSelectorMap.getValue( query );
	}
/*
	private Object getElement( String cssQuery )
	{
		String[] queryTrace = cssQuery.split( QUERY_PART_DELIMITER );
		Object currentObject = document;

		for ( String queryStage : queryTrace )
		{
			currentObject = select( ( JSONObject ) currentObject, queryStage );
		}

		return currentObject;
	}
*/
	public String getDocument( )
	{
		return document.toString( );
	}
/*
	private Object select( JSONObject source, String queryStage )
	{
		String[] queryParts = queryStage.split( "/" );

		String objectName = queryParts[ 0 ];
		if ( queryParts.length > 1 )
		{
			int index = Integer.parseInt( queryParts[ 1 ] );

			JSONArray array = ( JSONArray ) source.get( objectName );
			return array.get( index );
		}
		else
		{
			return source.get( objectName );
		}
	}
*/
	private String readDataFromUrl( String urlToRead )
			throws IOException
	{
		StringBuilder result = new StringBuilder( );
		URL url = new URL( urlToRead );
		HttpURLConnection conn = ( HttpURLConnection ) url.openConnection( );
		conn.setRequestMethod( "GET" );
		BufferedReader rd = new BufferedReader( new InputStreamReader( conn.getInputStream( ) ) );
		String line;
		while ( ( line = rd.readLine( ) ) != null )
		{
			result.append( line );
		}
		rd.close( );
		return result.toString( );
	}
}
