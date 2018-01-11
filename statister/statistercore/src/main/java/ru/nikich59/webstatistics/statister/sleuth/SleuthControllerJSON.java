package ru.nikich59.webstatistics.statister.sleuth;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 09.01.2018.
 */
public class SleuthControllerJSON extends SleuthController
{
	public static final String FILE_EXTENSION = "json";


	private Sleuth sleuth;

	private String sleuthDirectory;


	@Override
	public void storeSleuth( )
			throws IOException
	{
		File file = new File( sleuthDirectory );

		if ( file.isDirectory( ) || file.exists( ) )
		{
			return;
		}

		try ( FileWriter fileWriter = new FileWriter( file ) )
		{
			fileWriter.write( sleuth.getConfigObject( ).toJSONString( ) );
		}
		catch ( IOException e )
		{
			throw e;
		}
	}

	public SleuthController getFromConfig( JSONObject configObject )
	{
		SleuthControllerJSON sleuthJSONController = new SleuthControllerJSON( );
		sleuthJSONController.sleuth = new Sleuth( configObject );

		return sleuthJSONController;
	}

	@Override
	public void stop( )
	{
		File sleuthFile = new File( sleuthDirectory );

		if ( sleuthFile.exists( ) )
		{
			boolean isDeleted = sleuthFile.delete( );

			if ( ! isDeleted )
			{
				throw new RuntimeException( "File could not be deleted" );
			}
		}

		sleuth.stop( );
	}

	@Override
	public void loadSleuth( )
			throws IOException
	{
		File sleuthFile = new File( sleuthDirectory );

		if ( ! sleuthFile.exists( ) )
		{
			throw new IOException( "File \'" + sleuthFile.getAbsolutePath( ) + "\' does not exist" );
		}

		try ( FileReader fileReader = new FileReader( sleuthFile ) )
		{
			JSONObject jsonObject = ( JSONObject ) new JSONParser( ).parse( fileReader );

			this.sleuth = new Sleuth( jsonObject );
		}
		catch ( ParseException e )
		{
			// TODO: Implement error handling.
			e.printStackTrace( );
		}
	}

	public String getSleuthFileName( )
	{
		return sleuth.getFileName( ) + "." + FILE_EXTENSION;
	}

	@Override
	public Sleuth getSleuth( )
	{
		return sleuth;
	}

	@Override
	public List < SleuthController > listSleuth( String sleuthDirectory )
	{
		ArrayList < SleuthController > sleuthList = new ArrayList <>( );

		File sleuthDir = new File( sleuthDirectory );

		File[] sleuthFiles = sleuthDir.listFiles( );

		if ( sleuthFiles == null )
		{
			return sleuthList;
		}

		for ( File sleuthFile : sleuthFiles )
		{
			String extension = FilenameUtils.getExtension( sleuthFile.getAbsolutePath( ) );

			if ( extension.equals( FILE_EXTENSION ) )
			{
				try
				{
					SleuthControllerJSON sleuthJSONController = new SleuthControllerJSON( );
					sleuthJSONController.setSleuthDirectory( sleuthFile.getAbsolutePath( ) );
					sleuthJSONController.loadSleuth( );

					sleuthList.add( sleuthJSONController );
				}
				catch ( Exception e )
				{
					// TODO: Implement error handling.
					// Ignoring exception.
				}
			}
		}

		return sleuthList;
	}

	@Override
	public String getId( )
	{
		return sleuth.getId( ) + "." + FILE_EXTENSION;
	}

	@Override
	public void setSleuthDirectory( String sleuthDirectory )
	{
		this.sleuthDirectory = sleuthDirectory;
	}
}
