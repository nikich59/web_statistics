package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import stats.controller.StatsController;

import java.io.IOException;

/**
 * Created by Nikita on 01.01.2018.
 */
public class StatisticsView extends StackPane
{
	@FXML
	private Label titleLabel;

	private StatsController controller;

	public StatisticsView( StatsController controller )
	{
		this.controller = controller;


		FXMLLoader fxmlLoader = new FXMLLoader( getClass( ).getResource( "statistics_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		try
		{
			fxmlLoader.load( );
		}
		catch ( IOException exception )
		{
			throw new RuntimeException( exception );
		}

		titleLabel.setText( controller.getStatistics( ).getHeadline( ) );
	}
}
