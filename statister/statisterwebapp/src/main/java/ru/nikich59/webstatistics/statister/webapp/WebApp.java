package ru.nikich59.webstatistics.statister.webapp;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import ru.nikich59.webstatistics.statister.desktopapp.TemporaryController;
import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.statister.statistercli.Controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Nikita on 10.01.2018.
 */

@WebListener
public class WebApp implements ServletContextListener
{
	private Model model;
	private Controller controller;
	private ru.nikich59.webstatistics.visio.visiocore.model.Model visioModel;


	@Override
	public void contextInitialized( ServletContextEvent event )
	{


		File file = new File( "../../stats/config.json" );

		if ( file.exists( ) )
		{
			System.out.println( "EXISTS" );
		}
		else
		{
			System.out.println( "DOES NOT EXIST" );
		}

		try ( FileReader fileReader = new FileReader( file ) )
		{
			JSONObject configObject = ( JSONObject ) new JSONParser( ).parse( fileReader );
			model = new Model( configObject );

			visioModel = new ru.nikich59.webstatistics.visio.visiocore.model.Model( configObject );

//			controller = new Controller( configObject );
			controller = new TemporaryController( configObject );

			controller.setModel( model );

			controller.start( );

			event.getServletContext( ).setAttribute( "model", model );
			event.getServletContext( ).setAttribute( "controller", controller );

			event.getServletContext( ).setAttribute( "visio_model", visioModel );

			event.getServletContext( ).setAttribute( "sleuth_template_directory",
					( String ) configObject.get( "sleuth_template_directory" ) );

			event.getServletContext( ).setAttribute( "statister_template_directory",
					( String ) configObject.get( "statister_template_directory" ) );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
//		System.out.println( file.exists( ) );
/*

		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );
		System.out.println( "SUCCESS" );

		Timer timer = new Timer( );

		timer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						System.out.println( "running" );
					}
				},
				1000,
				1000
		);*/
	}

	@Override
	public void contextDestroyed( ServletContextEvent event )
	{
		model.stop( );

		controller.stop( );
	}
}
