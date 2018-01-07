package ru.nikich59.webstatistics.visiocore.desktopapp.errohandler;

/**
 * Created by Nikita on 06.01.2018.
 */
public class ConsoleErrorHandler extends ErrorHandler
{
	@Override
	public void handleException( Exception e )
	{
		e.printStackTrace( );
	}
}
