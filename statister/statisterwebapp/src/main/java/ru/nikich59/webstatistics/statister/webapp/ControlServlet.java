package ru.nikich59.webstatistics.statister.webapp;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.Template;
import ru.nikich59.webstatistics.statister.TemplateLoader;
import ru.nikich59.webstatistics.statister.TemplateParameter;
import ru.nikich59.webstatistics.statister.sleuth.SleuthControllerJSON;
import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 11.01.2018.
 */

@WebServlet(
		urlPatterns = { "/control/*" }
)
public class ControlServlet extends Servlet
{
	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
//		System.out.println( "POST" );

		/*
*/
		JSONObject requestJson;
		try ( BufferedReader reader = request.getReader( ) )
		{
			requestJson = ( JSONObject ) new JSONParser( ).parse( reader );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );

			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			return;
		}

		String command = ( String ) requestJson.get( "command" );

		if ( command.equals( "add" ) )
		{
			try
			{
				add( requestJson );
			}
			catch ( IOException e )
			{
				response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			}
		}
		if ( command.equals( "remove" ) )
		{
			try
			{
				remove( requestJson );
			}
			catch ( IOException e )
			{
				response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );

				return;
			}
		}

/*
		response.getWriter( ).write( "cmd: " + command + " type: " + type );

		switch ( command )
		{
			case "add":

		}*/
	}

	private void remove( JSONObject requestJson )
			throws IOException
	{
		String type = ( String ) requestJson.get( "type" );

		if ( type.equals( "sleuth" ) )
		{
			removeSleuth( ( JSONObject ) requestJson );
		}

		if ( type.equals( "statister" ) )
		{
			removeStatister( ( JSONObject ) requestJson );
		}
	}

	private void add( JSONObject requestJson )
			throws IOException
	{
		String type = ( String ) requestJson.get( "type" );

		if ( type.equals( "sleuth" ) )
		{
			addSleuth( ( JSONObject ) requestJson.get( "template" ) );
		}

		if ( type.equals( "statister" ) )
		{
			addStatister( ( JSONObject ) requestJson.get( "template" ) );
		}
	}

	private void removeSleuth( JSONObject requestJson )
			throws IOException
	{
		getModel( ).removeSleuth( ( String ) requestJson.get( "id" ) );
	}

	private void removeStatister( JSONObject requestJson )
			throws IOException
	{
		getModel( ).removeStatister( ( String ) requestJson.get( "id" ) );
	}

	private void addStatister( JSONObject templateJson )
			throws IOException
	{
		List < Template > templates = TemplateLoader.listTemplates( getStatisterTemplateDirectoy( ) );
		for ( Template template : templates )
		{
			if ( template.getDescription( ).equals( ( String ) templateJson.get( "description" ) ) )
			{
				JSONArray jsonParameters = ( JSONArray ) templateJson.get( "parameters" );
				List < TemplateParameter > templateParameters = template.getParameters( );
				for ( TemplateParameter templateParameter : templateParameters )
				{
					for ( Object jsonParameter : jsonParameters )
					{
						JSONObject parameterJsonObject = ( JSONObject ) jsonParameter;

						if ( templateParameter.getDescription( ).equals(
								( String ) parameterJsonObject.get( "description" ) ) )
						{
							templateParameter.setValue( String.valueOf( parameterJsonObject.get( "value" ) ) );
						}
					}
				}

				Statistics.StatisticsHeader statisticsHeader =
						new Statistics.StatisticsHeader( template.getProcessedTemplate( ) );
				StatsController statsController =
						getModel( ).getStatsControllerFactory( ).createStatisticsController( statisticsHeader );
				SiteStatisticsAcquirer statisticsAcquirer = new SiteStatisticsAcquirer( statsController );

				getModel( ).addStatisticsAcquirer( statisticsAcquirer );
			}
		}
	}

	private void addSleuth( JSONObject requestJson )
	{
		List < Template > templates = TemplateLoader.listTemplates( getSleuthTemplateDirectory( ) );
		for ( Template template : templates )
		{
			if ( template.getDescription( ).equals( ( String ) requestJson.get( "description" ) ) )
			{
				JSONArray jsonParameters = ( JSONArray ) requestJson.get( "parameters" );
				List < TemplateParameter > templateParameters = template.getParameters( );
				for ( TemplateParameter templateParameter : templateParameters )
				{
					for ( Object jsonParameter : jsonParameters )
					{
						JSONObject parameterJsonObject = ( JSONObject ) jsonParameter;

						if ( templateParameter.getDescription( ).equals(
								( String ) parameterJsonObject.get( "description" ) ) )
						{
							templateParameter.setValue( String.valueOf( parameterJsonObject.get( "value" ) ) );
						}
					}
				}
				SleuthControllerJSON sleuthController = ( SleuthControllerJSON )
						new SleuthControllerJSON( ).getFromConfig( template.getProcessedTemplate( ) );
				sleuthController.setSleuthDirectory( getModel( ).getSleuthFactory( ).getDirectory( ) +
						sleuthController.getSleuthFileName( ) );

				getModel( ).addSleuth( sleuthController );
			}
		}
	}
}
