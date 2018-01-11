package ru.nikich59.webstatistics.visio.desktopapp.controller;

import ru.nikich59.webstatistics.visio.desktopapp.ViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.SeriesListView;
import ru.nikich59.webstatistics.visio.desktopapp.view.dialog.AddSeriesDialog;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;
import ru.nikich59.webstatistics.visio.visiocore.model.series.Series;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 30.12.2017.
 */
public class SeriesListViewController extends ViewController
{
	private SeriesListView view;

	private class EventHandler extends ru.nikich59.webstatistics.visio.desktopapp.EventHandler
	{
		@Override
		public void onSeriesSetChangedEvent( )
		{
			updateView( );
		}
	}


	public SeriesListViewController( SeriesListView view, ViewController otherController )
	{
		super( otherController );

		subscribeToEvents( new EventHandler( ) );

		this.view = view;
	}

	public void setSeriesEnabled( String seriesId, boolean enabled )
	{
		getModel( ).setSeriesEnabled( seriesId, enabled );

		updateView( );

		getEventController( ).emitSeriesEnabledSetEvent( );
	}

	public void onAddSeriesEvent( )
	{
		List < Series < Number, Number > > seriesList = new ArrayList <>( );

		for ( Model.StatisticsSeries statisticsSeries : getModel( ).getData( ) )
		{
			seriesList.add( statisticsSeries.series );
		}

		AddSeriesDialog addSeriesDialog = new AddSeriesDialog( seriesList );

		Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

		result.ifPresent( ( series ) ->
				getModel( ).addSeries( series, null )
		);

		getEventController( ).emitSeriesSetChangedEvent( );
	}

	public void onClearSeriesEvent( )
	{
		getModel( ).clearSeries( );

		getEventController( ).emitSeriesSetChangedEvent( );
	}

	public void addSeries( Series < Number, Number > series, ZonedDateTime initialSeriesDateTime )
	{
		getModel( ).addSeries( series, initialSeriesDateTime );
	}

	public boolean removeSeries( String seriesId )
	{
		boolean isSuccessfulRemove = getModel( ).removeSeries( seriesId );

		if ( isSuccessfulRemove )
		{
			getEventController( ).emitSeriesSetChangedEvent( );
		}

		return isSuccessfulRemove;
	}

	public void updateView( )
	{
		view.update( getModel( ).getData( ) );
	}
}
