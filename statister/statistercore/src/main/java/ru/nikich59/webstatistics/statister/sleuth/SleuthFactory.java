package ru.nikich59.webstatistics.statister.sleuth;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 09.01.2018.
 */
public class SleuthFactory
{
	private String directory;

	public SleuthFactory( JSONObject configObject )
	{
		this.directory = ( String ) configObject.get( "sleuth_directory" );
	}

	public List < SleuthController > listSleuth( )
	{
		List < SleuthController > sleuthControllers = new ArrayList <>( );

		sleuthControllers.add( new SleuthControllerJSON( ) );

		List < SleuthController > sleuthAvailableControllers = new ArrayList <>( );

		for ( SleuthController sleuthController : sleuthControllers )
		{
			sleuthAvailableControllers.addAll( sleuthController.listSleuth( directory ) );
		}

		return sleuthAvailableControllers;
	}

	public String getDirectory( )
	{
		return directory;
	}
}
