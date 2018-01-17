package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;

import java.io.IOException;

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
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "statistics_view.fxml" ), this );

		initializeUI( statisticsHeader );
	}

	private void initializeUI( Statistics.StatisticsHeader statisticsHeader )
	{
		titleLabel.setText( statisticsHeader.getHeadline( ) );
	}
}
