package ru.nikich59.webstatistics.visio.desktopcore.controller;

/**
 * Created by Nikita on 08.01.2018.
 */
public class ViewController <
		Model,
		ErrorHandler,
		EventHandler,
		EventController extends ru.nikich59.webstatistics.visio.desktopcore.EventController < EventHandler > >
{
	private Model model;

	protected final Model getModel( )
	{
		return model;
	}

	private ErrorHandler errorHandler;

	public ErrorHandler getErrorHandler( )
	{
		return errorHandler;
	}

	private EventController eventController;

	protected final EventController getEventController( )
	{
		return eventController;
	}

	private EventHandler eventHandler;

	public ViewController(
			Model model,
			ErrorHandler errorHandler,
			EventController eventController )
	{
		this.model = model;
		this.eventController = eventController;
		setErrorHandler( errorHandler );
	}

	public ViewController( ViewController < Model, ErrorHandler, EventHandler, EventController > other )
	{
		this.model = other.getModel( );
		this.eventController = other.getEventController( );
		setErrorHandler( other.getErrorHandler( ) );
	}


	protected void setErrorHandler( ErrorHandler errorHandler )
	{
		this.errorHandler = errorHandler;
	}

	protected final void subscribeToEvents( EventHandler eventHandler )
	{
		this.eventHandler = eventHandler;

		eventController.subscribeToEvents( eventHandler );
	}

	protected final void unsubscribeFromEvents( )
	{
		eventController.unsubscribeFromEvents( eventHandler );
	}
}
