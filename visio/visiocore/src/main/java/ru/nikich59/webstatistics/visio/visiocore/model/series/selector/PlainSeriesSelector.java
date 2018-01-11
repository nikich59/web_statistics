package ru.nikich59.webstatistics.visio.visiocore.model.series.selector;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class PlainSeriesSelector< X, Y > extends SeriesSelector< X, Y >
{
	public XYChart.Series < X, Y > selectSeries( List< X > xAxis, List< Y > yAxis )
	{
		XYChart.Series < X, Y > series = new XYChart.Series <>( );

		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			series.getData( ).add( new XYChart.Data <>( xAxis.get( i ), yAxis.get( i ) ) );
		}

		return series;
	}

}
