package ru.nikich59.webstatistics.statister.webdataacquirer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Nikita on 10.01.2018.
 */
public class DataSelectorJSON
{
	public static final String QUERY_PART_DELIMITER = " ";

	private JSONObject document;


	public DataSelectorJSON( JSONObject document )
	{
		this.document = document;
	}

	public String getValue( String query )
	{
		return ( String ) getElement( query );
	}

	public void setValue( String query, Object value )
	{
		setElement( query, value );
	}

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

	private void setElement( String cssQuery, Object value )
	{
		String[] queryTrace = cssQuery.split( QUERY_PART_DELIMITER );
		Object currentObject = document;

		for ( int queryStageIndex = 0; queryStageIndex < queryTrace.length; queryStageIndex += 1 )
		{
			if ( queryStageIndex == queryTrace.length - 1 )
			{
				put( ( JSONObject ) currentObject, queryTrace[ queryStageIndex ], value );
			}

			currentObject = select( ( JSONObject ) currentObject, queryTrace[ queryStageIndex ] );
		}
	}

	private void put( JSONObject source, String queryStage, Object value )
	{
		String[] queryParts = queryStage.split( "/" );

		String objectName = queryParts[ 0 ];
		if ( queryParts.length > 1 )
		{
			int index = Integer.parseInt( queryParts[ 1 ] );

			JSONArray array = ( JSONArray ) source.get( objectName );
			array.set( index, value );
		}
		else
		{
			source.put( objectName, value );
		}
	}

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
}
