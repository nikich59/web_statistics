package ru.nikich59.webstatistics.visio.model.series;

import javafx.scene.chart.XYChart;
import ru.nikich59.webstatistics.visio.model.series.selector.PlainSeriesSelector;
import ru.nikich59.webstatistics.visio.model.series.selector.SeriesSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public abstract class Series < X, Y >
{
	private String name = "";

	protected List < X > xAxis = new ArrayList <>( );

	private SeriesSelector< X, Y > seriesSelector = new PlainSeriesSelector <>( );

	public final void setName( String name )
	{
		this.name = name;
	}

	public final String getName( )
	{
		return name;
	}

	public final void setSeriesSelector( SeriesSelector < X, Y > seriesSelector )
	{
		this.seriesSelector = seriesSelector;
	}

	public final XYChart.Series < X, Y > getSeries( )
	{
		XYChart.Series < X, Y > series = seriesSelector.selectSeries( xAxis, getData( ) );
		series.setName( getName( ) );

		return series;
	}


	public void setxAxis( List < X > xAxis )
	{
		this.xAxis = xAxis;
	}

	public abstract List < Y > getData( );
}
