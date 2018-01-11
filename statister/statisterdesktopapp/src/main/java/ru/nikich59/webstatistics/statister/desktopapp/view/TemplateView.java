package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.statister.TemplateParameter;
import ru.nikich59.webstatistics.statister.desktopapp.TemplateViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.TemplateParameterViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateView extends View < TemplateViewController >
{
	@FXML
	private Label descriptionLabel;


	@FXML
	private VBox parameterVBox;


	public TemplateView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "template_view.fxml" ), this );
	}

	@Override
	public void setController( TemplateViewController controller )
	{
		super.setController( controller );

		initializeUI( );
	}

	private void initializeUI( )
	{
		descriptionLabel.setText( getController( ).getTemplate( ).getDescription( ) );

		parameterVBox.getChildren( ).clear( );

		for ( TemplateParameter templateParameter : getController( ).getTemplate( ).getParameters( ) )
		{
			try
			{
				TemplateParameterView templateParameterView = new TemplateParameterView( );
				TemplateParameterViewController templateParameterViewController =
						new TemplateParameterViewController( getController( ),
								templateParameterView, templateParameter );
				templateParameterView.setController( templateParameterViewController );

				parameterVBox.getChildren( ).add( templateParameterView );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );

				// TODO: Implement error handling.
			}

		}
	}
}
