package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import ru.nikich59.webstatistics.statister.desktopapp.controller.SleuthItemViewController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthItemView extends View < SleuthItemViewController >
{
	@FXML
	private Label urlLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Label periodLabel;

	@FXML
	private Label errorLabel;

	@FXML
	private TextArea errorTextArea;


	private SleuthController sleuthController;

	public SleuthItemView( SleuthController sleuthController )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "sleuth_item_view.fxml" ), this );

		this.sleuthController = sleuthController;

		initializeUI( );
	}

	private void initializeUI( )
	{
		urlLabel.setText( sleuthController.getSleuth( ).getUrl( ) );
		urlLabel.setTextFill( getColorForSleuth( sleuthController ) );

		if ( sleuthController.getSleuth( ).getLastException( ) != null )
		{
			errorLabel.setText( sleuthController.getSleuth( ).getLastException( ).getLocalizedMessage( ) );
			errorLabel.setVisible( true );
		}
		else
		{
			errorLabel.setVisible( false );
		}

		descriptionLabel.setText( sleuthController.getSleuth( ).getDescription( ) );

		periodLabel.setText( String.valueOf(
				( ( float ) sleuthController.getSleuth( ).getPeriodInMillis( ) ) / 1000.0f ) + " sec" );


	}

	private Color getColorForSleuth( SleuthController sleuthController )
	{
		if ( sleuthController.getSleuth( ).getLastException( ) == null )
		{
			return Color.GREEN;
		}
		else
		{
			return Color.RED;
		}
	}
}
