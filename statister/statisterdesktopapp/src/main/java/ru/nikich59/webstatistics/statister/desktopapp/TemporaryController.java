package ru.nikich59.webstatistics.statister.desktopapp;

import net.minidev.json.JSONObject;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemporaryController extends ru.nikich59.webstatistics.statister.statistercli.Controller
{
	public TemporaryController( JSONObject configObject )
	{
		super( configObject );
	}

	@Override
	protected void initializeReportTimer( )
	{
	}
}
