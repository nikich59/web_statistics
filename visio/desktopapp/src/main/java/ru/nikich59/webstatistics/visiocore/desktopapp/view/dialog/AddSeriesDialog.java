package ru.nikich59.webstatistics.visiocore.desktopapp.view.dialog;

import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.editview.DiffSeriesEditVeiw;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.editview.MultiplySeriesEditView;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.editview.RateSeriesEditView;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.editview.SeriesEditView;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class AddSeriesDialog extends Dialog < Series < Number, Number > >
{
	private StackPane choiceBoxStackPane;
	private StackPane editViewStackPane;
	private SeriesEditView seriesEditView;
	private VBox vBox;
	private ChoiceBox < String > choiceBox;
	private List < Series < Number, Number > > baseSeries;

	public AddSeriesDialog( List < Series < Number, Number > > baseSeries )
	{
		this.baseSeries = baseSeries;

		List < String > choices = new ArrayList <>( );
		choices.add( "multiplied" );
		choices.add( "rate" );
		choices.add( "diff" );

		choiceBox = new ChoiceBox <>( FXCollections.observableArrayList( choices ) );
		choiceBox.setOnAction( ( e ) ->
				{
					switch ( choiceBox.getSelectionModel( ).getSelectedItem( ) )
					{
						case "multiplied":
							seriesEditView = new MultiplySeriesEditView( baseSeries );
							break;
						case "rate":
							seriesEditView = new RateSeriesEditView( baseSeries );
							break;
						case "diff":
							seriesEditView = new DiffSeriesEditVeiw( baseSeries );
							break;
					}

					if ( seriesEditView != null )
					{
						editViewStackPane.getChildren( ).setAll( seriesEditView );
					}
				}
		);

		choiceBoxStackPane = new StackPane( choiceBox );
		editViewStackPane = new StackPane( );
		vBox = new VBox( choiceBoxStackPane, editViewStackPane );

		this.getDialogPane( ).setContent( vBox );

		this.setOnCloseRequest( ( event ) ->
				{
					if ( choiceBox.getSelectionModel( ).isEmpty( ) )
					{
						setResult( null );
						return;
					}

					if ( seriesEditView != null )
					{
						setResult( seriesEditView.getSeries( ) );
						return;
					}

					setResult( null );
				}
		);

		this.setResizable( true );
		this.getDialogPane( ).getButtonTypes( ).add( ButtonType.CANCEL );
		this.getDialogPane( ).getButtonTypes( ).add( ButtonType.OK );
	}
}
