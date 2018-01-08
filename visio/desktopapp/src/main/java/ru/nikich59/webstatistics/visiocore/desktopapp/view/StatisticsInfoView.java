package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import stats.controller.StatsController;

import java.io.IOException;

import static ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader.loadFxmlInto;

/**
 * Created by Nikita on 01.01.2018.
 */
public class StatisticsInfoView extends StackPane
{
	@FXML
	private Label titleLabel;

	private StatsController controller;

	public StatisticsInfoView( StatsController controller )
			throws IOException
	{
		this.controller = controller;

		loadFxmlInto( getClass( ).getResource( "statistics_view.fxml" ), this );

		titleLabel.setText( controller.getStatistics( ).getHeader( ).getHeadline( ) );
	}
}
