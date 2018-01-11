package ru.nikich59.webstatistics.statister.desktopapp;

/**
 * Created by Nikita on 10.01.2018.
 */
public class EventController extends ru.nikich59.webstatistics.visio.desktopcore.EventController < EventHandler >
{
	public void emitUpdateEvent( )
	{
		getEventHandlers( ).forEach( EventHandler::onUpdateEvent );
	}
}
