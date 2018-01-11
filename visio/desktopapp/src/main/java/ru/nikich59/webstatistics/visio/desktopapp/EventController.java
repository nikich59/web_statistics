package ru.nikich59.webstatistics.visio.desktopapp;

/**
 * Created by Nikita on 08.01.2018.
 */
public class EventController extends ru.nikich59.webstatistics.visio.desktopcore.EventController < EventHandler >
{
	public void emitSeriesSetChangedEvent( )
	{
		getEventHandlers( ).forEach( EventHandler::onSeriesSetChangedEvent );
	}

	public void emitSeriesEnabledSetEvent( )
	{
		getEventHandlers( ).forEach( EventHandler::onSeriesEnableSetEvent );
	}
}







