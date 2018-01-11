package ru.nikich59.webstatistics.statister;

import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.webdataacquirer.DataSelectorJSON;

import java.util.ArrayList;
import java.util.List;
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

		protected abstract DataType fromJson( JSONObject configObject );

		public static DataType createFromJson( JSONObject jsonObject )
		{
			List < DataType > dataTypes = new ArrayList <>( );

			dataTypes.add( new DataTypeInt( ) );
			dataTypes.add( new DataTypeString( ) );

			for ( DataType dataType : dataTypes )
			{
				DataType createdDataType = dataType.fromJson( jsonObject );
				if ( createdDataType != null )
				{
					return createdDataType;
				}
			}

			return null;
		}

		public abstract JSONObject getJson( );
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
		protected DataType fromJson( JSONObject configObject )
		{
			String type = ( String ) configObject.get( "data_type" );
			if ( type == null )
			{
				return null;
			}

			if ( ! type.equals( "int" ) )
			{
				return null;
			}

			DataTypeInt dataTypeInt = new DataTypeInt( );
			dataTypeInt.minValue = ( long ) configObject.get( "min_value" );
			dataTypeInt.maxValue = ( long ) configObject.get( "max_value" );
			if ( configObject.get( "pref_value" ) != null )
			{
				dataTypeInt.prefValue = ( long ) configObject.get( "pref_value" );
			}
			else
			{
				dataTypeInt.prefValue = minValue;
			}

			return dataTypeInt;
		}

		@Override
		public JSONObject getJson( )
		{
			JSONObject jsonObject = new JSONObject( );

			jsonObject.put( "data_type", "int" );
			jsonObject.put( "min_value", minValue );
			jsonObject.put( "max_value", maxValue );
			jsonObject.put( "pref_value", prefValue );

			return jsonObject;
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
		protected DataType fromJson( JSONObject configObject )
		{
			String type = ( String ) configObject.get( "data_type" );
			if ( type != null && ! type.equals( "string" ) )
			{
				return null;
			}

			DataTypeString dataTypeString = new DataTypeString( );
			dataTypeString.prefix = ( String ) configObject.get( "prefix" );
			dataTypeString.postfix = ( String ) configObject.get( "postfix" );

			return dataTypeString;
		}

		@Override
		public JSONObject getJson( )
		{
			JSONObject jsonObject = new JSONObject( );

			jsonObject.put( "data_type", "string" );

			return jsonObject;
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

	public TemplateParameter( JSONObject configObject )
	{
		targetQuery = ( String ) configObject.get( "target_query" );
		description = ( String ) configObject.get( "description" );
		selector = ( String ) configObject.get( "selector" );

		dataType = DataType.createFromJson( configObject );
	}

	public void process( JSONObject targetTemplate )
	{
		DataSelectorJSON dataSelectorJSON = new DataSelectorJSON( targetTemplate );

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
