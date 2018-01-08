package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

import ru.nikich59.webstatistics.visiocore.desktopapp.view.SeriesListView;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.dialog.AddSeriesDialog;
import ru.nikich59.webstatistics.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visiocore.model.Model;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 30.12.2017.
 */
public class SeriesListController
{
	private Model model;
	private SeriesListView view;
	private ErrorHandler errorHandler;
	private VisioController visioController;

	public SeriesListController( Model model, SeriesListView view,
								 ErrorHandler errorHandler, VisioController visioController )
	{
		this.model = model;
		this.view = view;
		this.errorHandler = errorHandler;
		this.visioController = visioController;
	}

	public void setSeriesEnabled( String seriesId, boolean enabled )
	{
		model.setSeriesEnabled( seriesId, enabled );

		updateView( );

		visioController.getChartController( ).updateView( );
	}

	public void addSeries( Series < Number, Number > series, ZonedDateTime initialSeriesDateTime )
	{
		model.addSeries( series, initialSeriesDateTime );
	}

	public void addSeriesEvent( )
	{
		List < Series < Number, Number > > seriesList = new ArrayList <>( );

		for ( Model.StatisticsSeries statisticsSeries : model.getData( ) )
		{
			seriesList.add( statisticsSeries.series );
		}

		AddSeriesDialog addSeriesDialog = new AddSeriesDialog( seriesList );

		Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

		result.ifPresent( ( series ) ->
				model.addSeries( series, null )
		);

		updateView( );
	}

	public void updateView( )
	{
		view.update( model.getData( ) );
	}
}
