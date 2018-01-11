package ru.nikich59.webstatistics.statister.desktopapp.view.dialog;

import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.statister.Template;
import ru.nikich59.webstatistics.statister.TemplateLoader;
import ru.nikich59.webstatistics.statister.desktopapp.TemplateViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.TemplateView;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthControllerJSON;
import ru.nikich59.webstatistics.statister.sleuth.SleuthFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class AddSleuthDialog extends Dialog < SleuthController >
{
	private VBox list;
	private ChoiceBox < String > templateChoiceBox;
	private StackPane templateViewStackPane = new StackPane( );


	private SleuthFactory sleuthFactory;
	private String templateDirectory;

	private Template selectedTemplate;

	public AddSleuthDialog( StatisterViewController statisterViewController,
							SleuthFactory sleuthFactory,
							String templateDirectory )
			throws IOException
	{
		this.sleuthFactory = sleuthFactory;
		this.templateDirectory = templateDirectory;


		templateChoiceBox = new ChoiceBox <>( );
		List < String > choices = new ArrayList <>( );
		List < Template > templates = TemplateLoader.listTemplates( templateDirectory );
		for ( Template template : templates )
		{
			choices.add( template.getDescription( ) );
		}

		templateChoiceBox.setItems( FXCollections.observableArrayList( choices ) );
		templateChoiceBox.setOnAction( ( e ) ->
				{
					try
					{
						selectedTemplate = templates.get( templateChoiceBox.getSelectionModel( ).getSelectedIndex( ) );

						TemplateView templateView = new TemplateView( );

						TemplateViewController templateViewController = new TemplateViewController(
								statisterViewController, templateView, selectedTemplate );
						templateView.setController( templateViewController );

						templateViewStackPane.getChildren( ).setAll( templateView );
					}
					catch ( IOException ex )
					{
						ex.printStackTrace( );

						// TODO: Implement error handling.
					}
				}
		);

		list = new VBox( templateChoiceBox, templateViewStackPane );

		this.setOnCloseRequest( ( event ) ->
				{
					try
					{
						SleuthControllerJSON sleuthController = ( SleuthControllerJSON )
								new SleuthControllerJSON( ).getFromConfig( selectedTemplate.getProcessedTemplate( ) );
						sleuthController.setSleuthDirectory( sleuthFactory.getDirectory( ) +
								sleuthController.getSleuthFileName( ) );
						setResult( sleuthController );

						return;
					}
					catch ( Exception e )
					{
						e.printStackTrace( );

						setResult( null );

						return;
					}
				}
		);

		setResizable( true );

		getDialogPane( ).getButtonTypes( ).add( ButtonType.CLOSE );

		getDialogPane( ).getButtonTypes( ).add( ButtonType.OK );

		getDialogPane( ).setContent( list );
	}
}
