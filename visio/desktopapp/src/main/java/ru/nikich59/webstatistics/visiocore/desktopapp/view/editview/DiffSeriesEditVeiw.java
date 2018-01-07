package ru.nikich59.webstatistics.visiocore.desktopapp.view.editview;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visiocore.model.series.DifferentialSeries;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class DiffSeriesEditVeiw extends SeriesEditView
{
	private List < Series< Number, Number > > baseSeries;

	private ChoiceBox < String > choiceBox;
	private VBox vBox;

	public DiffSeriesEditVeiw( List < Series < Number, Number > > baseSeries )
	{
		this.baseSeries = baseSeries;

		List < String > choices = new ArrayList <>( );
		for ( Series < Number, Number > series : baseSeries )
		{
			choices.add( series.getName( ) );
		}

		choiceBox = new ChoiceBox <>( FXCollections.observableArrayList( choices ) );

		vBox = new VBox( choiceBox );

		getChildren( ).setAll( vBox );
	}

	@Override
	public Series < Number, Number > getSeries( )
	{
		DifferentialSeries< Number > differentialSeries = new DifferentialSeries <>( );

		Series < Number, Number > baseSeries = getSelectedSeries( );
		XYChart.Series < Number, Number > baseSeriesSeries = baseSeries.getSeries( );
		List < Number > xAxis = new ArrayList <>( );
		for ( XYChart.Data < Number, Number > dataPoint : baseSeriesSeries.getData( ) )
		{
			xAxis.add( dataPoint.getXValue( ) );
		}

		differentialSeries.setxAxis( xAxis );
		differentialSeries.setyAxis( baseSeries.getData( ) );

		differentialSeries.setName( "(diff " + baseSeries.getName( ) + ")" );

		return differentialSeries;
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
