package ru.nikich59.webstatistics.statister.sleuth;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 09.01.2018.
 */
public abstract class SleuthController
{
	public abstract void storeSleuth( )
			throws IOException;

	public abstract void loadSleuth( )
			throws IOException;

	public abstract Sleuth getSleuth( );

	public abstract List < SleuthController > listSleuth( String sleuthDirectory );

	public abstract String getId( );

	public abstract void stop( )
			throws IOException;

	public abstract void setSleuthDirectory( String directory );
}
