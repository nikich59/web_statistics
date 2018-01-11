package ru.nikich59.webstatistics.statister.desktopapp.controller;

import javafx.application.Platform;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.desktopapp.ViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.StatisterListView;

import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterListViewController extends ViewController
{
	private class EventHandler extends ru.nikich59.webstatistics.statister.desktopapp.EventHandler
	{
		@Override
		public void onUpdateEvent( )
		{
			Platform.runLater( ( ) ->
					view.updateView( )
			);
		}
	}

	private StatisterListView view;

	public StatisterListViewController( StatisterListView view, StatisterViewController statisterViewController )
	{
		super( statisterViewController );

		subscribeToEvents( new EventHandler( ) );

		this.view = view;
	}

	public List < SiteStatisticsAcquirer > getSiteStatisticsAcquirers( )
	{
		return getModel( ).getSiteStatisticsAcquirers( );
	}
}
