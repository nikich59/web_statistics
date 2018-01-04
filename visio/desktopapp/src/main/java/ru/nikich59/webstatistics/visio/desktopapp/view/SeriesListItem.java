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
public class SeriesListItem extends StackPane
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
	{
		this.series = series;
		this.controller = controller;


		FXMLLoader fxmlLoader = new FXMLLoader( getClass( ).getResource( "series_list_item.fxml" ) );
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
