package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visiocore.model.Model;

import java.io.IOException;

/**
 * Created by Nikita on 30.12.2017.
 */
public class SeriesListItem extends BasicVisioView
{
	@FXML
	private Label titleLabel;

	@FXML
	private Button editButton;

	@FXML
	private CheckBox enableCheckBox;


	private Model.StatisticsSeries series;
	private SeriesListController controller;


	public SeriesListItem( Model.StatisticsSeries series, SeriesListController controller )
			throws IOException
	{
		this.series = series;
		this.controller = controller;

		ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader.loadFxmlInto(
				getClass( ).getResource( "series_list_item.fxml" ), this );

		initializeUI( );
	}

	private void initializeUI( )
	{
		titleLabel.setText( series.series.getName( ) );
		enableCheckBox.setSelected( series.isEnabled );
	}

	@FXML
	private void onEnabledCheckboxAction( )
	{
		controller.setSeriesEnabled( series.id, enableCheckBox.isSelected( ) );
	}
}
