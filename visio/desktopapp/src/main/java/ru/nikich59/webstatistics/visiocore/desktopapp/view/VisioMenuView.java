package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.VisioMenuController;
import stats.controller.StatsFileController;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nikita on 08.01.2018.
 */
public class VisioMenuView extends StackPane
{
	private VisioMenuController controller;


	public VisioMenuView( )
			throws IOException
	{
		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
				getClass( ).getResource( "visio_menu_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );
		/*
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_menu_view.fxml" ), this );*/
	}

	@FXML
	private void menuFileAddClicked( )
	{
		controller.addStatisticsEvent( );
	}

	public void setController( VisioMenuController controller )
	{
		this.controller = controller;
	}
}
