package ru.nikich59.webstatistics.visiocore.desktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visiocore.desktopapp.errohandler.ConsoleErrorHandler;
import ru.nikich59.webstatistics.visiocore.desktopapp.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visiocore.desktopapp.model.bridge.ModelToDesktopAppBridge;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visiocore.model.Model;

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
			visioView = new VisioView( null );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			return;
		}
		visioView.setErrorHandler( errorHandler );

		ModelToDesktopAppBridge modelToDesktopAppBridge = new ModelToDesktopAppBridge( visioView, visioModel );

		VisioController visioController = new VisioController( visioModel, modelToDesktopAppBridge );
		visioController.setStatisticsDirectory( "C:\\Java\\webstatistics\\statister\\stats2\\statisters\\" );

		visioView.setController( visioController );

/*
		visioView.getChartXAxis( ).setForceZeroInRange( false );
		visioView.getChartYAxis( ).setForceZeroInRange( false );
*/

		modelToDesktopAppBridge.updateView( );

		Scene scene = new Scene( visioView, 600, 400 );

		stage.setScene( scene );
		stage.show( );

		visioController.updateModel( );
	}


	public static void main( String[] args )
	{
		launch( args );
	}
}
