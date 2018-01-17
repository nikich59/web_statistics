package ru.nikich59.webstatistics.statister;

import ru.nikich59.webstatistics.statister.webdataacquirer.DataSelectorMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateParameter
{
	public static abstract class DataType
	{
		public abstract Object getFormattedValue( String value );

		protected abstract DataType fromMap( Map < String, Object > configMap );

		public static DataType createFromMap( Map < String, Object > configMap )
		{
			List < DataType > dataTypes = new ArrayList <>( );

			dataTypes.add( new DataTypeInt( ) );
			dataTypes.add( new DataTypeString( ) );

			for ( DataType dataType : dataTypes )
			{
				DataType createdDataType = dataType.fromMap( configMap );
				if ( createdDataType != null )
				{
					return createdDataType;
				}
			}

			return null;
		}

		public abstract Map < String, Object > getMap( );
	}

	public static class DataTypeInt extends DataType
	{
		private long minValue;
		private long maxValue;
		private long prefValue;

		@Override
		public Object getFormattedValue( String value )
		{
			if ( value.isEmpty( ) )
			{
				value = "0";
			}
			long intValue = Long.parseLong( value );

			if ( intValue < minValue )
			{
				intValue = minValue;
			}

			if ( intValue > maxValue )
			{
				intValue = maxValue;
			}

			return intValue;
		}

		@Override
		protected DataType fromMap( Map < String, Object > configMap )
		{
			String type = ( String ) configMap.get( "data_type" );
			if ( type == null )
			{
				return null;
			}

			if ( ! type.equals( "int" ) )
			{
				return null;
			}

			DataTypeInt dataTypeInt = new DataTypeInt( );
			dataTypeInt.minValue = Long.parseLong( configMap.get( "min_value" ).toString( ) );
			dataTypeInt.maxValue = Long.parseLong( configMap.get( "max_value" ).toString( ) );
			if ( configMap.get( "pref_value" ) != null )
			{
				dataTypeInt.prefValue = Long.parseLong( configMap.get( "pref_value" ).toString( ) );
			}
			else
			{
				dataTypeInt.prefValue = minValue;
			}

			return dataTypeInt;
		}

		@Override
		public Map < String, Object > getMap( )
		{
			Map < String, Object > configMap = new HashMap <>( );

			configMap.put( "data_type", "int" );
			configMap.put( "min_value", String.valueOf( minValue ) );
			configMap.put( "max_value", String.valueOf( maxValue ) );
			configMap.put( "pref_value", String.valueOf( prefValue ) );

			return configMap;
		}
	}

	public static class DataTypeString extends DataType
	{
		private String prefix;
		private String postfix;

		@Override
		public Object getFormattedValue( String value )
		{
			String stringValue = value;

			return prefix + stringValue + postfix;
		}

		@Override
		protected DataType fromMap( Map < String, Object > configMap )
		{
			String type = ( String ) configMap.get( "data_type" );
			if ( type != null && ! type.equals( "string" ) )
			{
				return null;
			}

			DataTypeString dataTypeString = new DataTypeString( );
			dataTypeString.prefix = ( String ) configMap.get( "prefix" );
			dataTypeString.postfix = ( String ) configMap.get( "postfix" );

			return dataTypeString;
		}

		@Override
		public Map < String, Object > getMap( )
		{
			Map < String, Object > configMap = new HashMap <>( );

			configMap.put( "data_type", "string" );

			return configMap;
		}
	}


	private String targetQuery;
	public DataType getDataType( )
	{
		return dataType;
	}
	private DataType dataType;
	public String getDescription( )
	{
		return description;
	}
	private String description;
	private String selector;

	public void setValue( String value )
	{
		this.value = value;
	}
	private String value = "";

	public TemplateParameter( Map < String, Object > configMap )
	{
		targetQuery = ( String ) configMap.get( "target_query" );
		description = ( String ) configMap.get( "description" );
		selector = ( String ) configMap.get( "selector" );

		dataType = DataType.createFromMap( configMap );
	}

	public void process( Map < String, Object > configMap )
	{
		DataSelectorMap dataSelectorJSON = new DataSelectorMap( configMap );

		if ( selector == null || selector.isEmpty( ) )
		{
			dataSelectorJSON.setValue( targetQuery,
					dataType.getFormattedValue( value ) );
		}
		else
		{
			Pattern pattern = Pattern.compile( selector );
			Matcher matcher = pattern.matcher( value );
			if ( ! matcher.find( ) )
			{
				throw new RuntimeException( "No pattern matches \'" + selector +
						"\' in \'" + value + "\'" );
			}

			dataSelectorJSON.setValue( targetQuery,
					dataType.getFormattedValue( matcher.group( ) ) );
		}
	}
}
