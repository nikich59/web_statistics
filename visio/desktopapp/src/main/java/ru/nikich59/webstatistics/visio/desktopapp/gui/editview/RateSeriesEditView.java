package ru.nikich59.webstatistics.visio.desktopapp.gui.editview;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.model.series.RateSeries;
import ru.nikich59.webstatistics.visio.model.series.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class RateSeriesEditView extends SeriesEditView
{
	private List < Series < Number, Number > > baseSeries;

	private ChoiceBox < String > choiceBoxAbove;
	private ChoiceBox < String > choiceBoxBelow;
	private VBox vBox;

	public RateSeriesEditView( List < Series < Number, Number > > baseSeries )
	{
		this.baseSeries = baseSeries;

		List < String > choices = new ArrayList <>( );
		for ( Series < Number, Number > series : baseSeries )
		{
			choices.add( series.getName( ) );
		}

		choiceBoxAbove = new ChoiceBox <>( FXCollections.observableArrayList( choices ) );
		choiceBoxBelow = new ChoiceBox <>( FXCollections.observableArrayList( choices ) );


		vBox = new VBox( choiceBoxAbove, choiceBoxBelow );

		getChildren( ).setAll( vBox );
	}

	@Override
	public Series < Number, Number > getSeries( )
	{
		RateSeries< Number > rateSeries = new RateSeries <>( );

		Series< Number, Number > selectedSeriesAbove = getSelectedSeries( choiceBoxAbove );
		Series< Number, Number > selectedSeriesBelow = getSelectedSeries( choiceBoxBelow );
		if ( selectedSeriesAbove == null || selectedSeriesBelow == null )
		{
			return null;
		}

		XYChart.Series < Number, Number > baseSeries = selectedSeriesAbove.getSeries( );
		List < Number > xAxis = new ArrayList <>( );
		for ( XYChart.Data < Number, Number > dataPoint : baseSeries.getData( ) )
		{
			xAxis.add( dataPoint.getXValue( ) );
		}

		rateSeries.setxAxis( xAxis );
		rateSeries.setyAxisAbove( selectedSeriesAbove.getData( ) );
		rateSeries.setyAxisBelow( selectedSeriesBelow.getData( ) );

		rateSeries.setName( "(" + selectedSeriesAbove.getName( ) + " / " + selectedSeriesBelow.getName( ) + ")" );

		return rateSeries;
	}

	private Series < Number, Number > getSelectedSeries( ChoiceBox< String > choiceBox )
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
