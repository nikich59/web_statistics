package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.nikich59.webstatistics.statister.desktopapp.controller.TemplateParameterViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateParameterView extends View < TemplateParameterViewController >
{
	@FXML
	private Label titleLabel;

	@FXML
	private TextField input;


	public TemplateParameterView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "template_parameter_view.fxml" ), this );
	}

	@Override
	public void setController( TemplateParameterViewController controller )
	{
		super.setController( controller );

		titleLabel.setText( controller.getTemplateParameter( ).getDescription( ) );

		input.textProperty( ).addListener( ( observable, oldValue, newValue ) ->
				getController( ).setValue( newValue )
		);
	}
}
