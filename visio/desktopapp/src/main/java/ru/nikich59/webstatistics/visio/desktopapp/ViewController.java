package ru.nikich59.webstatistics.visio.desktopapp;

import ru.nikich59.webstatistics.visio.visiocore.errohandler.ConsoleErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

/**
 * Created by Nikita on 08.01.2018.
 */
public class ViewController
		extends ru.nikich59.webstatistics.visio.desktopcore.controller.ViewController <
		Model, ErrorHandler, EventHandler, EventController >
{
	public ViewController(
			Model model,
			ErrorHandler errorHandler,
			EventController eventController )
	{
		super( model, errorHandler, eventController );
	}

	public ViewController( ViewController other )
	{
		super( other );
	}

	@Override
	public void setErrorHandler( ErrorHandler errorHandler )
	{
		super.setErrorHandler( errorHandler );

		if ( errorHandler == null )
		{
			setErrorHandler( new ConsoleErrorHandler( ) );
		}
	}

	public final void handleException( Exception e )
	{
		getErrorHandler( ).handleException( e );
	}
}
