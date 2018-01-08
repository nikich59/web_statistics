package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import stats.Statistics;

import java.io.IOException;

import ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader;

/**
 * Created by Nikita on 01.01.2018.
 */
public class StatisticsInfoView extends StackPane
{
	@FXML
	private Label titleLabel;

	public StatisticsInfoView( Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{
		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
				getClass( ).getResource( "statistics_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );
		/*
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "statistics_view.fxml" ), this );*/

		titleLabel.setText( statisticsHeader.getHeadline( ) );
	}
}
