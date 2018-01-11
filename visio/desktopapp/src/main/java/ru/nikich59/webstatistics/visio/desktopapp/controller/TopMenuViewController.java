package ru.nikich59.webstatistics.visio.desktopapp.controller;

import ru.nikich59.webstatistics.visio.desktopapp.ViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.TopMenuView;
import ru.nikich59.webstatistics.visio.desktopapp.view.dialog.AddStatisticsDialog;
import stats.controller.StatsController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 08.01.2018.
 */
public class TopMenuViewController extends ViewController
{
	private TopMenuView view;


	public TopMenuViewController( TopMenuView view, ViewController otherController )
	{
		super( otherController );

		this.view = view;
	}

	public List < StatsController > getAvailableControllers( )
	{
		return getModel( ).getAvailableControllers( );
	}

	public void addStatsController( StatsController statsController )
	{
		try
		{
			getModel( ).loadStatisticsWithController( statsController );
		}
		catch ( IOException e )
		{
			handleException( e );
		}
	}

	public void addStatisticsEvent( )
	{
		AddStatisticsDialog addStatisticsDialog = getAddStatisticsDialog( );
		if ( addStatisticsDialog == null )
		{
			return;
		}

		Optional < StatsController > result = addStatisticsDialog.showAndWait( );

		result.ifPresent( this::addStatsController );

		getEventController( ).emitSeriesSetChangedEvent( );
	}

	public AddStatisticsDialog getAddStatisticsDialog( )
	{
		try
		{
			return new AddStatisticsDialog( getModel( ).getAvailableControllers( ) );
		}
		catch ( IOException e )
		{
			handleException( e );
		}

		return null;
	}
}
