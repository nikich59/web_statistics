package ru.nikich59.webstatistics.statister;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public Template( Map < String, Object > configMap )
	{
		List < Object > parameterArray = ( List < Object > ) configMap.get( "parameters" );

		for ( Object parameterConfig : parameterArray )
		{
			Map < String, Object > parameterMap = ( Map < String, Object > ) parameterConfig;

			parameters.add( new TemplateParameter( parameterMap ) );
		}

		template = ( JSONObject ) configMap.get( "template" );

		description = ( String ) configMap.get( "description" );
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
