package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import ru.nikich59.webstatistics.visio.desktopapp.controller.TopMenuViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 08.01.2018.
 */
public class TopMenuView extends View< TopMenuViewController >
{
	public TopMenuView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_menu_view.fxml" ), this );
	}

	@FXML
	private void menuFileAddClicked( )
	{
		getController( ).addStatisticsEvent( );
	}

}
