package ru.nikich59.webstatistics.statister;

import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthControllerJSON;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthTemplateLoader
{
	public SleuthController getFromTemplate( JSONObject configObject )
	{
		SleuthControllerJSON sleuthController = new SleuthControllerJSON( );

		sleuthController.getFromConfig( configObject );

		return sleuthController;
	}
}
