package ru.nikich59.webstatistics.visiocore.errohandler;

/**
 * Created by Nikita on 06.01.2018.
 */
public class ConsoleErrorHandler implements ErrorHandler
{
	@Override
	public void handleException( Exception e )
	{
		e.printStackTrace( );
	}
}
