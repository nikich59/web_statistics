package ru.nikich59.webstatistics.visio.desktopcore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 08.01.2018.
 */
public class EventController< EventHandler >
{
	private List < EventHandler > eventHandlers = new ArrayList <>( );

	protected final List< EventHandler > getEventHandlers( )
	{
		return eventHandlers;
	}

	public void subscribeToEvents( EventHandler eventHandler )
	{
		eventHandlers.add( eventHandler );
	}

	public void unsubscribeFromEvents( EventHandler eventHandler )
	{
		eventHandlers.remove( eventHandler );
	}
}







