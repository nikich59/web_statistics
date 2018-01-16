package ru.nikich59.webstatistics.statister.webapp;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 11.01.2018.
 */

@WebServlet(
		urlPatterns = { "/visio_list/*" }
)
public class VisioListServlet extends HttpServlet
{
	private Model model;


	@Override
	public void init( ServletConfig config )
			throws ServletException
	{
		super.init( config );

		model = ( Model ) config.getServletContext( ).getAttribute( "visio_model" );
	}


	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
/*
		model.addStatisticsDirectory( "../../ru.nikich59.webstatistics.core.corebasics.stats/ru.nikich59.webstatistics.core.corebasics.stats/statisters/" );
		model.addStatisticsDirectory( "../../ru.nikich59.webstatistics.core.corebasics.stats/ru.nikich59.webstatistics.core.corebasics.stats/finished/" );
*/
		List < StatsController > controllers = model.getAvailableControllers( );

		JSONObject responseJson = new JSONObject( );
		JSONArray controllerArray = new JSONArray( );

		for ( StatsController controller : controllers )
		{
			JSONObject controllerObject = new JSONObject( );

			controllerObject.put( "id", getId( controller.getId( ) ) );

			controllerObject.put( "headline", controller.getStatisticsHeader( ).getHeadline( ) );

			controllerArray.add( controllerObject );
		}

		responseJson.put( "items", controllerArray );

		response.getWriter( ).write( responseJson.toJSONString( ) );
	}

	public static String getId( String id )
	{
		return id.replace( " ", "" ).replace( "?", "" ).
				replace( ".", "" ).replace( "\\", "" ).
				replace( "/", "" ).replace( ":", "" ).
				replace( "&", "" );
	}
}
