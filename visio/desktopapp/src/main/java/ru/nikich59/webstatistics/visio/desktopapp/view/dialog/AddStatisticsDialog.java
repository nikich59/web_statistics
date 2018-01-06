package ru.nikich59.webstatistics.visio.desktopapp.view.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.desktopapp.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visio.desktopapp.view.StatisticsInfoView;
import stats.controller.StatsController;
import stats.controller.StatsControllerFactory;

import java.util.List;


/**
 * Created by Nikita on 01.01.2018.
 */
public class AddStatisticsDialog extends Dialog < StatsController >
{
	private ScrollPane scrollPane = new ScrollPane( );
	private VBox list = new VBox( );

	private StatsController result = null;

	public AddStatisticsDialog( String directory, ErrorHandler errorHandler )
	{
		StatsControllerFactory factory = new StatsControllerFactory( );
		List < StatsController > controllerList = factory.listStatsInDirectory( directory );

		for ( StatsController statsController : controllerList )
		{
			StatisticsInfoView statisticsView;

			try
			{
				statisticsView = new StatisticsInfoView( statsController );
			}
			catch ( Exception e )
			{
				errorHandler.handleException( e );

				continue;
			}

			statisticsView.setOnMouseClicked( ( event ) ->
					{
						result = statsController;

						close( );
					}
			);

			list.getChildren( ).add( statisticsView );
		}

		setOnCloseRequest( ( event ) ->
				setResult( result )
		);

		getDialogPane( ).getButtonTypes( ).add( ButtonType.CLOSE );

		scrollPane.setContent( list );
		scrollPane.setFitToWidth( true );
		getDialogPane( ).setContent( scrollPane );

		setResizable( true );
	}
}













