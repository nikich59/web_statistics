package ru.nikich59.webstatistics.statister.desktopapp.controller;

import javafx.application.Platform;
import ru.nikich59.webstatistics.statister.desktopapp.ViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.SleuthListView;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;

import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthListViewController extends ViewController
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


	private SleuthListView view;

	public SleuthListViewController( SleuthListView view, StatisterViewController statisterViewController )
	{
		super( statisterViewController );

		subscribeToEvents( new EventHandler( ) );

		this.view = view;
	}

	public List < SleuthController > getSleuthList( )
	{
		return getModel( ).getSleuthList( );
	}
}
