package ru.nikich59.webstatistics.statister.webdataacquirer;


import java.util.List;
import java.util.Map;

/**
 * Created by Nikita on 10.01.2018.
 */
public class DataSelectorMap
{
	public static final String QUERY_PART_DELIMITER = " ";

	private Map < String, Object > map;


	public DataSelectorMap( Map < String, Object > map )
	{
		this.map = map;
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
		Object currentObject = map;

		for ( String queryStage : queryTrace )
		{
			currentObject = select( ( Map < String, Object > ) currentObject, queryStage );
		}

		return currentObject;
	}

	private void setElement( String cssQuery, Object value )
	{
		String[] queryTrace = cssQuery.split( QUERY_PART_DELIMITER );
		Object currentObject = map;

		for ( int queryStageIndex = 0; queryStageIndex < queryTrace.length; queryStageIndex += 1 )
		{
			if ( queryStageIndex == queryTrace.length - 1 )
			{
				put( ( Map < String, Object > ) currentObject, queryTrace[ queryStageIndex ], value );
			}

			currentObject = select( ( Map < String, Object > ) currentObject, queryTrace[ queryStageIndex ] );
		}
	}

	private void put( Map < String, Object > source, String queryStage, Object value )
	{
		String[] queryParts = queryStage.split( "/" );

		String objectName = queryParts[ 0 ];
		if ( queryParts.length > 1 )
		{
			int index = Integer.parseInt( queryParts[ 1 ] );

			List < Object > array = ( List < Object > ) source.get( objectName );
			array.set( index, value );
		}
		else
		{
			source.put( objectName, value );
		}
	}

	private Object select( Map < String, Object > source, String queryStage )
	{
		String[] queryParts = queryStage.split( "/" );

		String objectName = queryParts[ 0 ];
		if ( queryParts.length > 1 )
		{
			int index = Integer.parseInt( queryParts[ 1 ] );

			List < Object > array = ( List < Object > ) source.get( objectName );
			return array.get( index );
		}
		else
		{
			return source.get( objectName );
		}
	}
}
