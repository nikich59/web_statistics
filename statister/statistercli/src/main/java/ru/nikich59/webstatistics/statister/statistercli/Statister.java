package ru.nikich59.webstatistics.statister.statistercli;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
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

			Model model = new Model( configJson );

			Controller controller = new Controller( configJson );

			controller.setModel( model );

			controller.start( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
