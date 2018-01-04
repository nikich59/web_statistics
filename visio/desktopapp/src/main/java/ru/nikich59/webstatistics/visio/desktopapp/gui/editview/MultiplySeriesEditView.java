package ru.nikich59.webstatistics.visio.desktopapp.gui.editview;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.model.series.MultipliedSeries;
import ru.nikich59.webstatistics.visio.model.series.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class MultiplySeriesEditView extends SeriesEditView
{
	private List < Series< Number, Number > > baseSeries;

	private ChoiceBox < String > choiceBox;
	private TextField rateField;
	private VBox vBox;

	public MultiplySeriesEditView( List < Series < Number, Number > > baseSeries )
	{
		this.baseSeries = baseSeries;

		List < String > choices = new ArrayList <>( );
		for ( Series < Number, Number > series : baseSeries )
		{
			choices.add( series.getName( ) );
		}

		choiceBox = new ChoiceBox <>( FXCollections.observableArrayList( choices ) );

		rateField = new TextField( );

		vBox = new VBox( choiceBox, rateField );

		getChildren( ).setAll( vBox );
	}

	@Override
	public Series < Number, Number > getSeries( )
	{
		MultipliedSeries< Number > multipliedSeries = new MultipliedSeries <>( );

		Series < Number, Number > baseSeries = getSelectedSeries( );
		XYChart.Series < Number, Number > baseSeriesSeries = baseSeries.getSeries( );
		List < Number > xAxis = new ArrayList <>( );
		for ( XYChart.Data < Number, Number > dataPoint : baseSeriesSeries.getData( ) )
		{
			xAxis.add( dataPoint.getXValue( ) );
		}

		multipliedSeries.setxAxis( xAxis );
		multipliedSeries.setyAxis( baseSeries.getData( ) );

		float ratio = 1.0f;
		try
		{
			ratio = Float.parseFloat( rateField.getText( ) );
		}
		catch ( Exception e )
		{

		}

		multipliedSeries.setRatio( ratio );

		multipliedSeries.setName( "(" + String.valueOf( ratio ) + " x " + baseSeries.getName( ) + ")" );

		return multipliedSeries;
	}

	private Series < Number, Number > getSelectedSeries( )
	{
		String selectedSreiesName = choiceBox.getSelectionModel( ).getSelectedItem( );

		for ( Series < Number, Number > series : baseSeries )
		{
			if ( series.getName( ).equals( selectedSreiesName ) )
			{
				return series;
			}
		}

		return null;
	}
}
