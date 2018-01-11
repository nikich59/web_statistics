package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

import java.io.IOException;

/**
 * Created by Nikita on 30.12.2017.
 */
public class SeriesListItem extends View< SeriesListViewController >
{
	@FXML
	private Label titleLabel;

	@FXML
	private Button editButton;

	@FXML
	private CheckBox enableCheckBox;

	@FXML
	private Button removeButton;


	private Model.StatisticsSeries series;


	public SeriesListItem( Model.StatisticsSeries series, SeriesListViewController controller )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "series_list_item.fxml" ), this );

		this.series = series;
		setController( controller );

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
		getController( ).setSeriesEnabled( series.id, enableCheckBox.isSelected( ) );
	}

	@FXML
	private void onRemoveButtonAction( )
	{
		getController( ).removeSeries( series.id );
	}
}
