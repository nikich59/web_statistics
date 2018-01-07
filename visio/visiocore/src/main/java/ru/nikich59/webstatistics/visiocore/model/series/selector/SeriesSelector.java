package ru.nikich59.webstatistics.visiocore.model.series.selector;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public abstract class SeriesSelector< X, Y >
{
	public abstract XYChart.Series< X, Y > selectSeries( List< X > xAxis, List< Y > yAxis );
}
