package ru.nikich59.webstatistics.statister.webapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;
import stats.Statistics;
import stats.controller.StatsController;

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
	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		Model model = new Model( );

		model.addStatisticsDirectory( "../../stats/stats/statisters/" );
		model.addStatisticsDirectory( "../../stats/stats/finished/" );

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
		for ( int i = 0; i < statistics.getHeader( ).getColumnNames( ).size( ); i += 1 )
		{
			JSONArray pointArray = new JSONArray( );

			if ( statistics.getHeader( ).getColumnNames( ).get( i ).equals( "views" ) )
			{
				viewsIndex = i;
			}
			else
			{
				columnNameAray.add( statistics.getHeader( ).getColumnNames( ).get( i ) );
			}
		}

		responseJson.put( "column_names", columnNameAray );

		for ( Statistics.DataPoint dataPoint : statistics.getDataPoints( ) )
		{
			JSONArray pointArray = new JSONArray( );

			pointArray.add( dataPoint.dateTime.toEpochSecond( ) );

			for ( int i = 0; i < dataPoint.data.length; i += 1 )
			{
				if ( i != viewsIndex )
				{
					pointArray.add( Long.parseLong( dataPoint.data[ i ] ) );
				}
			}

			pointsArray.add( pointArray );
		}
		responseJson.put( "items", pointsArray );


		response.getWriter( ).write( responseJson.toJSONString( ) );
	}
}
