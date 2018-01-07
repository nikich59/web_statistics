package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.dialog.AddSeriesDialog;
import ru.nikich59.webstatistics.visiocore.model.Model;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 29.12.2017.
 */
public class SeriesListView extends BasicVisioView
{
	@FXML
	private VBox seriesViewBox;


	private SeriesListController controller;
	private List < Model.StatisticsSeries > statisticsSeries;

	public SeriesListView( )
			throws IOException
	{
		System.out.println( getClass( ).getResource(
				"series_view.fxml" ) );


		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader( getClass( ).getResource(
				"series_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );
		/*
		loadFxmlInto( getClass( ).getResource( "series_view.fxml" ), this );*/
	}

	public void update( List < Model.StatisticsSeries > statisticsSeries )
	{
		this.statisticsSeries = statisticsSeries;

		seriesViewBox.getChildren( ).clear( );
		for ( Model.StatisticsSeries series : statisticsSeries )
		{
			try
			{
				seriesViewBox.getChildren( ).add( new SeriesListItem( series, controller ) );
			}
			catch ( Exception e )
			{
				handleException( e );
			}
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

		for ( Model.StatisticsSeries statisticsSeries : statisticsSeries )
		{
			seriesList.add( statisticsSeries.series );
		}

		AddSeriesDialog addSeriesDialog = new AddSeriesDialog( seriesList );

		Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

		result.ifPresent( ( series ) ->
				{
					Model.StatisticsSeries statisticsSeries = new Model.StatisticsSeries( );
					statisticsSeries.series = series;
					controller.addSeries( statisticsSeries );
				}
		);
	}
}











