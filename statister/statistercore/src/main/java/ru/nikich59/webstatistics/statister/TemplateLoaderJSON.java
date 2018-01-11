package ru.nikich59.webstatistics.statister;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateLoaderJSON
{
	public static final String FILE_EXTENSION = "json";

	public static Template load( String path )
			throws IOException, ParseException
	{
		File templateFile = new File( path );

		String extension = FilenameUtils.getExtension( templateFile.getAbsolutePath( ) );
		if ( ! extension.equals( FILE_EXTENSION ) )
		{
			return null;
		}

		try ( FileReader templateFileReader = new FileReader( templateFile ) )
		{
			JSONObject templateObject = ( JSONObject ) new JSONParser( ).parse( templateFileReader );

			return new Template( templateObject );
		}
		catch ( Exception e )
		{
			throw e;
//			e.printStackTrace( );

			// TODO: Implement error handling.
		}
	}
}
