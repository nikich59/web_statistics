package ru.nikich59.webstatistics.visio.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.controller.SeriesListController;
import ru.nikich59.webstatistics.visio.model.VisioModel;
import ru.nikich59.webstatistics.visio.model.series.Series;
import ru.nikich59.webstatistics.visio.series.gui.AddSeriesDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 29.12.2017.
 */
public class SeriesView extends StackPane
{
	@FXML
	private VBox seriesViewBox;


	private SeriesListController controller;
	private List < VisioModel.StatisticsSeries > statisticsSeries;

	public SeriesView( )
	{
		FXMLLoader fxmlLoader = new FXMLLoader( getClass( ).getResource( "series_view.fxml" ) );
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
	}

	public void update( List < VisioModel.StatisticsSeries > statisticsSeries )
	{
		this.statisticsSeries = statisticsSeries;

		seriesViewBox.getChildren( ).clear( );
		for ( VisioModel.StatisticsSeries series : statisticsSeries )
		{
			seriesViewBox.getChildren( ).add( new SeriesListItem( series, controller ) );
		}
	}

	public void setController( SeriesListController controller )
	{
		this.controller = controller;
	}

	@FXML
	public void addSeriesButtonClicked( )
	{
		List < Series < Number, Number > > seriesList = new ArrayList <>( );

		for ( VisioModel.StatisticsSeries statisticsSeries : statisticsSeries )
		{
			seriesList.add( statisticsSeries.series );
		}

		AddSeriesDialog addSeriesDialog = new AddSeriesDialog( seriesList );

		Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

		result.ifPresent( ( series ) ->
				{
					VisioModel.StatisticsSeries statisticsSeries = new VisioModel.StatisticsSeries( );
					statisticsSeries.series = series;
					controller.addSeries( statisticsSeries );
				}
		);
	}
}











