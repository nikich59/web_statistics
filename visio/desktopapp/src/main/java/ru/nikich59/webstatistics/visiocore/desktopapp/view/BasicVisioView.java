package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visiocore.errohandler.ConsoleErrorHandler;
import ru.nikich59.webstatistics.visiocore.errohandler.ErrorHandler;

/**
 * Created by Nikita on 06.01.2018.
 */
public abstract class BasicVisioView extends StackPane
{
	private ErrorHandler errorHandler = new ConsoleErrorHandler( );

	public final void setErrorHandler( ErrorHandler errorHandler )
	{
		this.errorHandler = errorHandler;
	}

	protected final ErrorHandler getErrorHandler( )
	{
		return errorHandler;
	}

	protected final void handleException( Exception e )
	{
		errorHandler.handleException( e );
	}

}
