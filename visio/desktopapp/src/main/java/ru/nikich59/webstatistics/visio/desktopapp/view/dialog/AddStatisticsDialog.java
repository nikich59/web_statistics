package ru.nikich59.webstatistics.visio.desktopapp.view.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.desktopapp.view.StatisticsInfoView;
import stats.controller.StatsController;

import java.io.IOException;
import java.util.List;


/**
 * Created by Nikita on 01.01.2018.
 */
public class AddStatisticsDialog extends Dialog < StatsController >
{
	private ScrollPane scrollPane = new ScrollPane( );
	private VBox list = new VBox( );

	private StatsController result = null;

	public AddStatisticsDialog( List < StatsController > availableStatistics )
			throws IOException
	{
		for ( StatsController statsController : availableStatistics )
		{
			StatisticsInfoView statisticsView;

			statisticsView = new StatisticsInfoView( statsController.getStatistics( ).getHeader( ) );

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













