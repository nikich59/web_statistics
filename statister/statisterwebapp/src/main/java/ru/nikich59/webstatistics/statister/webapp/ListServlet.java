package ru.nikich59.webstatistics.statister.webapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.Template;
import ru.nikich59.webstatistics.statister.TemplateLoader;
import ru.nikich59.webstatistics.statister.TemplateParameter;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 11.01.2018.
 */

@WebServlet(
		urlPatterns = { "/list/*" }
)
public class ListServlet extends Servlet
{
	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String listType = request.getParameter( "type" );

		JSONObject responseJson = new JSONObject( );

		switch ( listType )
		{
			case "sleuth":
				JSONArray sleuthList = new JSONArray( );

				for ( SleuthController sleuthController : getModel( ).getSleuthList( ) )
				{
					JSONObject sleuthControllerObject = sleuthController.getSleuth( ).getConfigObject( );

					if ( sleuthController.getSleuth( ).getLastException( ) != null )
					{
						sleuthControllerObject.put( "last_error",
								sleuthController.getSleuth( ).getLastException( ).getMessage( ) );
					}

					sleuthControllerObject.put( "id", sleuthController.getId( ) );

					sleuthList.add( sleuthControllerObject );
				}

				responseJson.put( "items", sleuthList );
				break;
			case "sleuth_template":
				List < Template > templates = TemplateLoader.listTemplates( getSleuthTemplateDirectory( ) );
				JSONArray templateArray = new JSONArray( );
				for ( Template template : templates )
				{
					JSONObject templateObject = new JSONObject( );
					templateObject.put( "description", template.getDescription( ) );
					JSONArray templateParameters = new JSONArray( );
					for ( TemplateParameter templateParameter : template.getParameters( ) )
					{
						JSONObject parameterObject = new JSONObject( );
						parameterObject.put( "description", templateParameter.getDescription( ) );
						parameterObject.put( "data_type", templateParameter.getDataType( ).getJson( ) );

						templateParameters.add( parameterObject );
					}

					templateObject.put( "parameters", templateParameters );

					templateArray.add( templateObject );
				}

				responseJson.put( "items", templateArray );
				break;
			case "statister_template":
				templates = TemplateLoader.listTemplates( getStatisterTemplateDirectoy( ) );
				templateArray = new JSONArray( );
				for ( Template template : templates )
				{
					JSONObject templateObject = new JSONObject( );
					templateObject.put( "description", template.getDescription( ) );
					JSONArray templateParameters = new JSONArray( );
					for ( TemplateParameter templateParameter : template.getParameters( ) )
					{
						JSONObject parameterObject = new JSONObject( );
						parameterObject.put( "description", templateParameter.getDescription( ) );
						parameterObject.put( "data_type", templateParameter.getDataType( ).getJson( ) );

						templateParameters.add( parameterObject );
					}

					templateObject.put( "parameters", templateParameters );

					templateArray.add( templateObject );
				}

				responseJson.put( "items", templateArray );
				break;
			case "statister":
				JSONArray statisterList = new JSONArray( );

				for ( SiteStatisticsAcquirer statisticsAcquirer : getModel( ).getSiteStatisticsAcquirers( ) )
				{
					JSONObject statisticsAcquirerObject =
							statisticsAcquirer.getStatisticsHeader( ).getConfigObject( );
					if ( statisticsAcquirer.getLastException( ) != null )
					{
						statisticsAcquirerObject.put( "last_error",
								statisticsAcquirer.getLastException( ).getMessage( ) );
					}

					statisticsAcquirerObject.put( "id", statisticsAcquirer.getStatisticsId( ) );

					JSONArray lastDataArray = new JSONArray( );
					for ( String s : statisticsAcquirer.getLastData( ) )
					{
						lastDataArray.add( s );
					}

					statisticsAcquirerObject.put( "last_data", lastDataArray );

					statisterList.add( statisticsAcquirerObject );
				}

				responseJson.put( "items", statisterList );
				break;
			default:
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		}

		response.getWriter( ).write( responseJson.toJSONString( ) );
	}
}
