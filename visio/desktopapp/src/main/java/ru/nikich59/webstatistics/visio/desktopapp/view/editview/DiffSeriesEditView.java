package ru.nikich59.webstatistics.visio.desktopapp.view.editview;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.visiocore.model.series.DifferentialSeries;
import ru.nikich59.webstatistics.visio.visiocore.model.series.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class DiffSeriesEditView extends SeriesEditView
{
	private List < Series < Number, Number > > baseSeries;

	@FXML
	private ChoiceBox < String > choiceBox;


	public DiffSeriesEditView( List < Series < Number, Number > > baseSeries )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "diff_series_edit_view.fxml" ), this );

		this.baseSeries = baseSeries;

		List < String > choices = new ArrayList <>( );
		for ( Series < Number, Number > series : baseSeries )
		{
			choices.add( series.getName( ) );
		}

		choiceBox.setItems( FXCollections.observableArrayList( choices ) );
	}

	@Override
	public Series < Number, Number > getSeries( )
	{
		DifferentialSeries < Number > differentialSeries = new DifferentialSeries <>( );

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
