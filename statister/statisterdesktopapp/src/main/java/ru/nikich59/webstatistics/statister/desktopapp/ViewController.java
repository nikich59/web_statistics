package ru.nikich59.webstatistics.statister.desktopapp;

import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;

/**
 * Created by Nikita on 10.01.2018.
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
}
