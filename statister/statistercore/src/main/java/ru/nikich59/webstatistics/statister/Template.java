package ru.nikich59.webstatistics.statister;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class Template
{
	public List < TemplateParameter > getParameters( )
	{
		return parameters;
	}
	private List < TemplateParameter > parameters = new ArrayList <>( );
	private JSONObject template;
	public String getDescription( )
	{
		return description;
	}
	private String description;

	public Template( JSONObject configObject )
	{
		JSONArray parameterArray = ( JSONArray ) configObject.get( "parameters" );

		for ( Object parameterConfig : parameterArray )
		{
			JSONObject parameterConfigJson = ( JSONObject ) parameterConfig;

			parameters.add( new TemplateParameter( parameterConfigJson ) );
		}

		template = ( JSONObject ) configObject.get( "template" );

		description = ( String ) configObject.get( "description" );
	}

	public JSONObject getProcessedTemplate( )
	{
		for ( TemplateParameter templateParameter : parameters )
		{
			templateParameter.process( template );
		}

		return template;
	}
}
