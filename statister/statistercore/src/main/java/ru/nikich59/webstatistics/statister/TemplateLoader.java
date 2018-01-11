package ru.nikich59.webstatistics.statister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateLoader
{
	public static Template load( String path )
			throws Exception
	{
		return TemplateLoaderJSON.load( path );
	}

	public static List < Template > listTemplates( String directory )
	{
		List < Template > templates = new ArrayList <>( );

		File directoryFile = new File( directory );

		File[] files = directoryFile.listFiles( );
		if ( files == null )
		{
			return templates;
		}

		for ( File file : files )
		{
			try
			{
				templates.add( TemplateLoaderJSON.load( file.getAbsolutePath( ) ) );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );

				// TODO: Implement error handling.
			}
		}

		return templates;
	}
}
