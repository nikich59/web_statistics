package ru.nikich59.webstatistics.visio.desktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ConsoleErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

/**
 * Created by Nikita on 29.12.2017.
 */
public class VisioApplication extends Application
{
	@Override
	public void start( Stage stage )
	{
		stage.setTitle( "Visio app" );

		ErrorHandler errorHandler = new ConsoleErrorHandler( );

		Model visioModel = new Model( );
		VisioView visioView;


		try
		{
			visioView = new VisioView( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			return;
		}

		EventController eventController = new EventController( );

		VisioViewController visioController = new VisioViewController( visioModel, visioView, errorHandler, eventController );
		visioModel.addStatisticsDirectory( "C:\\Java\\webstatistics\\statister\\stats2\\statisters\\" );

		visioView.setController( visioController );

/*
		visioView.getChartXAxis( ).setForceZeroInRange( false );
		visioView.getChartYAxis( ).setForceZeroInRange( false );
*/

		Scene scene = new Scene( visioView, 600, 400 );

		stage.setScene( scene );
		stage.show( );
	}


	public static void main( String[] args )
	{
		launch( args );
	}
}
