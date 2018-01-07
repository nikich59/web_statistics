package ru.nikich59.webstatistics.visiocore.desktopapp;

import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikita on 06.01.2018.
 */
public class FXMLLoader
{
	public static final void loadFxmlInto( URL fxmlFilePath, Parent parent )
			throws IOException
	{
		System.out.println( fxmlFilePath );



		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader( fxmlFilePath );
		fxmlLoader.setRoot( parent );
		fxmlLoader.setController( parent );

		fxmlLoader.load( );
	}
}
