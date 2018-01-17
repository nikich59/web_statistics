package ru.nikich59.webstatistics.statister;

import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthControllerJSON;

import java.util.Map;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthTemplateLoader
{
	public SleuthController getFromTemplate( Map< String, Object > configMap )
	{
		SleuthControllerJSON sleuthController = new SleuthControllerJSON( );

		sleuthController.getFromConfig( configMap );

		return sleuthController;
	}
}
