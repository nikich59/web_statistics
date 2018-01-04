package ru.nikich59.webstatistics.visio.view;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Nikita on 25.12.2017.
 */
public class ChartListPane extends StackPane
{
	private static class ListElement extends GridPane
	{
		private Label titleLabel = new Label( );
		private File dataFile;

		ListElement( File file )
		{
			dataFile = file;

			String initialTimeStamp = "";
			String link = "";
			String headline = "";

			try ( FileReader fileReader = new FileReader( file );
				  BufferedReader bufferedReader = new BufferedReader( fileReader ) )
			{
				initialTimeStamp = bufferedReader.readLine( );
				link = bufferedReader.readLine( );
				headline = bufferedReader.readLine( );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			getChildren( ).add( titleLabel );

			titleLabel.setText( headline );

			setRowIndex( titleLabel, 2 );

			setOnMouseClicked( ( e ) ->
					{
						VisioPane visioPane = new VisioPane( );

						visioPane.setFileName( dataFile.getAbsolutePath( ) );

						Stage stage = new Stage( );
						stage.setTitle( "My New Stage Title" );
						stage.setScene( new Scene( visioPane, 900, 600 ) );
						stage.show( );

//						visioPane.setFileName( dataFile.getAbsolutePath( ) );
					}
			);
		}
	}

	private ListView < ListElement > listView = new ListView <>( );
	private String dataPath = "";

	public ChartListPane( )
	{
		getChildren( ).setAll( listView );
	}

	public void setDataPath( String dataPath )
	{
		this.dataPath = dataPath;

		File dataDir = new File( dataPath );
		File[] files = dataDir.listFiles( );

		listView.getItems( ).clear( );
		listView.setOrientation( Orientation.VERTICAL );
		for ( File file : files )
		{
			listView.getItems( ).add( new ListElement( file ) );
		}

		listView.getItems().sort( ( i1, i2 ) ->
				{
					if ( i1.dataFile.lastModified( ) == i2.dataFile.lastModified( ) )
					{
						return 0;
					}
					if ( i1.dataFile.lastModified( ) > i2.dataFile.lastModified( ) )
					{
						return -1;
					}
					else
					{
						return 1;
					}
				}
		);
	}


}
