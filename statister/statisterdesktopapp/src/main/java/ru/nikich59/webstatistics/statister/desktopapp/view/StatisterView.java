package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthListViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterListViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterView extends View < StatisterViewController >
{
	@FXML
	private SleuthListView sleuthListView;

	@FXML
	private StatisterListView statisterListView;


	public StatisterView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "statister_view.fxml" ), this );
	}

	@Override
	public void setController( StatisterViewController controller )
	{
		super.setController( controller );

		initializeUI( );
	}

	private void initializeUI( )
	{
		sleuthListView.setController(
				new SleuthListViewController( sleuthListView, getController( ) ) );

		statisterListView.setController(
				new StatisterListViewController( statisterListView, getController( ) ) );
	}

	@FXML
	private void onAddSleuthEvent( )
	{
		getController( ).onAddSleuthEvent( );
	}

	@FXML
	private void onAddStatisterEvent( )
	{
		getController( ).onAddStatisterEvent( );
	}
}
