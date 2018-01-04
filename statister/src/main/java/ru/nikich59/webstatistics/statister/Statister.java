package ru.nikich59.webstatistics.statister;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.nikich59.webstatistics.statister.model.Model;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Nikita on 24.12.2017.
 */

public class Statister
{
	public static void main( String[] args )
			throws Exception
	{
		if ( args.length < 1 )
		{
			System.out.println( "Params: <config-file-path>" );
			return;
		}
		File configFile = new File( args[ 0 ] );
		try ( FileReader configFileReader = new FileReader( configFile ) )
		{
			JSONObject configJson = ( JSONObject ) new JSONParser( ).parse( configFileReader );

			int reportPeriodInMillis = 60000;
			if ( configJson.get( "report_period_in_millis" ) != null )
			{
				reportPeriodInMillis = ( int ) ( long ) configJson.get( "report_period_in_millis" );
			}

			Model model = new Model( reportPeriodInMillis );
			model.setStatistersDirectoryPath( ( String ) configJson.get( "statisters_dir" ) );
			model.setFinishedStatisticsDirectoryPath(
					( String ) configJson.get( "finished_statistics_directory_path" ) );

			Controller controller = new Controller( model );

			controller.setSleuthDirectoryPath( ( String ) configJson.get( "sleuths_dir" ) );
			controller.setStatistersDirectoryPath( ( String ) configJson.get( "statisters_dir" ) );
			controller.setFinishedStatisticsDirectoryPath(
					( String ) configJson.get( "finished_statistics_directory_path" ) );

			controller.start( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
