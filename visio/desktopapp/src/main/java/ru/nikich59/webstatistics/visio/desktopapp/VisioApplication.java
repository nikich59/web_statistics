package ru.nikich59.webstatistics.visio.desktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visio.desktopapp.model.bridge.ModelToDesktopAppBridge;
import ru.nikich59.webstatistics.visio.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visio.model.VisioModel;

/**
 * Created by Nikita on 29.12.2017.
 */
public class VisioApplication extends Application
{
	@Override
	public void start( Stage stage )
	{
		stage.setTitle( "Visio app" );

		VisioModel visioModel = new VisioModel( );
		VisioView visioView = new VisioView( null );
		ModelToDesktopAppBridge modelToDesktopAppBridge = new ModelToDesktopAppBridge( visioView, visioModel );
//		StatsFileController statsFileController = new StatsFileController( );
/*		statsFileController.setStatisticsDirectory(
				"C:\\Java\\webstatistics\\statister\\stats2\\statisters\\" +
						"httpswwwgoogleapiscomyoutubev3videospart=statistics&id=Tz50vEX0nwE&key=AIzaSyD1yAAiGOS2fygCzie7d5dvWz9pU4EwPlM.stats" );
*/
		VisioController visioController = new VisioController( visioModel, modelToDesktopAppBridge );
		visioController.setStatisticsDirectory( "C:\\Java\\webstatistics\\statister\\stats2\\statisters\\" );
//		visioController.addStatsController( statsFileController );

		visioView.setController( visioController );


		visioView.getChartXAxis( ).setForceZeroInRange( false );
		visioView.getChartYAxis( ).setForceZeroInRange( false );

//		visioView.setChartingMode( VisioChartView.ChartingMode.AREA_CHART );

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
