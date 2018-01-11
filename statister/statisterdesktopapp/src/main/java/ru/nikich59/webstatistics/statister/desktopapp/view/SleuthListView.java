package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthItemViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthListViewController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthListView extends View < SleuthListViewController >
{
	@FXML
	private VBox sleuthList;

	public SleuthListView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "sleuth_list_view.fxml" ), this );
	}

	@Override
	public void setController( SleuthListViewController controller )
	{
		super.setController( controller );

		initializeUI( );
	}

	private void initializeUI( )
	{
		sleuthList.getChildren( ).clear( );

		for ( SleuthController sleuthController : getController( ).getSleuthList( ) )
		{
			try
			{
				SleuthItemView sleuthItemView = new SleuthItemView( sleuthController );

				SleuthItemViewController sleuthItemViewController = new SleuthItemViewController(
						getController( ), sleuthItemView, sleuthController );
				sleuthItemView.setController( sleuthItemViewController );

				sleuthList.getChildren( ).add( sleuthItemView );
			}
			catch ( IOException e )
			{
				getController( ).getErrorHandler( ).handleException( e );
			}
		}
	}

	public void updateView( )
	{
		initializeUI( );
	}
}
