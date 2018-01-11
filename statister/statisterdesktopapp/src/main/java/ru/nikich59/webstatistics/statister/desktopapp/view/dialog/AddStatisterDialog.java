package ru.nikich59.webstatistics.statister.desktopapp.view.dialog;

import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.Template;
import ru.nikich59.webstatistics.statister.TemplateLoader;
import ru.nikich59.webstatistics.statister.desktopapp.TemplateViewController;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.TemplateView;
import stats.Statistics;
import stats.controller.StatsController;
import stats.controller.StatsControllerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 10.01.2018.
 */
public class AddStatisterDialog extends Dialog < SiteStatisticsAcquirer >
{
	private VBox list;
	private ChoiceBox < String > templateChoiceBox;
	private StackPane templateViewStackPane = new StackPane( );


	private StatsControllerFactory statsControllerFactory;
	private String templateDirectory;

	private Template selectedTemplate;

	public AddStatisterDialog( StatisterViewController statisterViewController,
							   StatsControllerFactory statsControllerFactory,
							   String templateDirectory )
			throws IOException
	{
		this.statsControllerFactory = statsControllerFactory;
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
						Statistics.StatisticsHeader statisticsHeader =
								new Statistics.StatisticsHeader( selectedTemplate.getProcessedTemplate( ) );
						StatsController statsController =
								statsControllerFactory.createStatisticsController( statisticsHeader );
						SiteStatisticsAcquirer statisticsAcquirer = new SiteStatisticsAcquirer( statsController );
						setResult( statisticsAcquirer );

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
