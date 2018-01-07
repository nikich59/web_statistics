package ru.nikich59.webstatistics.visiocore.desktopapp.errohandler;

import ru.nikich59.webstatistics.visiocore.desktopapp.model.bridge.ModelToDesktopAppBridge;

/**
 * Created by Nikita on 06.01.2018.
 */
public abstract class ErrorHandler
{
	private ModelToDesktopAppBridge bridge;

	public ErrorHandler( )
	{

	}

	public abstract void handleException( Exception e );

	public void setBridge( ModelToDesktopAppBridge bridge )
	{
		this.bridge = bridge;
	}
}
