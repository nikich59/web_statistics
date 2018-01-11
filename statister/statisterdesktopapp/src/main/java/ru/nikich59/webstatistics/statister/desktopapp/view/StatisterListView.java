package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthItemViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthListViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterItemViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterListViewController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterListView extends View< StatisterListViewController >
{
	@FXML
	private VBox statisterList;

	public StatisterListView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "statister_list_view.fxml" ), this );
	}

	@Override
	public void setController( StatisterListViewController controller )
	{
		super.setController( controller );

		initializeUI( );
	}

	private void initializeUI( )
	{
		statisterList.getChildren( ).clear( );

		for ( SiteStatisticsAcquirer statisticsAcquirer : getController( ).getSiteStatisticsAcquirers( ) )
		{
			try
			{
				StatisterItemView statisterItemView = new StatisterItemView( statisticsAcquirer );

				StatisterItemViewController statisterItemViewController = new StatisterItemViewController(
						getController( ), statisterItemView, statisticsAcquirer );
				statisterItemView.setController( statisterItemViewController );

				statisterList.getChildren( ).add( statisterItemView );
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
