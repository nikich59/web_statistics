package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visio.model.VisioModel;

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


	private VisioModel.StatisticsSeries series;
	private SeriesListController controller;


	public SeriesListItem( VisioModel.StatisticsSeries series, SeriesListController controller )
			throws IOException
	{
		this.series = series;
		this.controller = controller;

		ru.nikich59.webstatistics.visio.desktopapp.FXMLLoader.loadFxmlInto(
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
