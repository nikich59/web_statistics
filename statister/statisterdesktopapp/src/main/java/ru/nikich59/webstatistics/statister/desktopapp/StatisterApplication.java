package ru.nikich59.webstatistics.statister.desktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.StatisterView;
import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ConsoleErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterApplication extends Application
{
	@Override
	public void start( Stage stage )
	{
		stage.setTitle( "Visio app" );

		ErrorHandler errorHandler = new ConsoleErrorHandler( );

		File file = new File( "config.json" );
		JSONObject configObject;
		try ( FileReader fileReader = new FileReader( file ) )
		{
			configObject = ( JSONObject ) new JSONParser( ).parse( fileReader );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			return;
		}

		Model model = new Model( configObject );
		TemporaryController controller = new TemporaryController( configObject );
		controller.setModel( model );
		controller.start( );

		StatisterView statisterView;

		try
		{
			statisterView = new StatisterView( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			return;
		}

		EventController eventController = new EventController( );
		StatisterViewController statisterViewController = new StatisterViewController(
				model, statisterView, new ConsoleErrorHandler( ), eventController );

		statisterView.setController( statisterViewController );

		Scene scene = new Scene( statisterView, 600, 400 );

		stage.setOnCloseRequest( ( e ) -> System.exit( 0 ) );

		stage.setScene( scene );
		stage.show( );
	}


	public static void main( String[] args )
	{
		launch( args );
	}
}
