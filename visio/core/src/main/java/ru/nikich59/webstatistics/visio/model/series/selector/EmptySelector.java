package ru.nikich59.webstatistics.visio.model.series.selector;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class EmptySelector< X, Y > extends SeriesSelector< X, Y >
{
	public XYChart.Series < X, Y > selectSeries( List< X > xAxis, List< Y > yAxis )
	{
		XYChart.Series < X, Y > series = new XYChart.Series <>( );

		return series;
	}

}
