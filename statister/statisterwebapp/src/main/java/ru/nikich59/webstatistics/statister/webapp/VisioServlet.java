package ru.nikich59.webstatistics.statister.webapp;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
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
		urlPatterns = { "/visio_data/*" }
)
public class VisioServlet extends HttpServlet
{
	Model model;


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

		String id = request.getParameter( "id" );

		StatsController controller = null;
		for ( StatsController statsController : controllers )
		{
			if ( VisioListServlet.getId( statsController.getId( ) ).equals( id ) )
			{
				controller = statsController;
			}
		}

		if ( controller == null )
		{
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );

			return;
		}

		controller.loadStatistics( );
		Statistics statistics = controller.getStatistics( );

		JSONObject responseJson = new JSONObject( );
		JSONArray pointsArray = new JSONArray( );

		int viewsIndex = - 1;
		JSONArray columnNameAray = new JSONArray( );
		for ( int i = 0; i < statistics.getHeader( ).getValueDescriptions( ).size( ); i += 1 )
		{
			JSONArray pointArray = new JSONArray( );

			if ( statistics.getHeader( ).getValueDescriptions( ).get( i ).getName( ).equals( "views" ) )
			{
				viewsIndex = i;
			}
			else
			{
				columnNameAray.add( statistics.getHeader( ).getValueDescriptions( ).get( i ).getName( ) );
			}
		}

		responseJson.put( "column_names", columnNameAray );

		for ( Statistics.DataPoint dataPoint : statistics.getDataPoints( ) )
		{
			JSONArray pointArray = new JSONArray( );

			pointArray.add( dataPoint.getDateTime( ).toEpochSecond( ) );

			for ( int i = 0; i < dataPoint.getData( ).size( ); i += 1 )
			{
				if ( i != viewsIndex )
				{
					pointArray.add( Long.parseLong( dataPoint.getData( ).get( i ) ) );
				}
			}

			pointsArray.add( pointArray );
		}
		responseJson.put( "items", pointsArray );


		response.getWriter( ).write( responseJson.toJSONString( ) );
	}
}
