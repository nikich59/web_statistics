package ru.nikich59.webstatistics.visiocore.model.series.selector;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class EachNthSeriesSelector < X > extends SeriesSelector < X, Number >
{
	public enum SelectMode
	{
		EACH_NTH,
		SUM,
		ARITHMETIC_MEAN,
		MAX,
		MIN
	}

	private int n = 1;
	private SelectMode selectMode = SelectMode.EACH_NTH;

	public void setN( int n )
	{
		this.n = n;
	}

	public void setSelectMode( SelectMode selectMode )
	{
		this.selectMode = selectMode;
	}

	public XYChart.Series < X, Number > selectSeries( List < X > xAxis, List < Number > yAxis )
	{
		switch ( selectMode )
		{
			case EACH_NTH:
				return selectSeriesEachNth( xAxis, yAxis );
			case SUM:
				return selectSeriesSum( xAxis, yAxis );
			case ARITHMETIC_MEAN:
				return selectSeriesArithmeticMean( xAxis, yAxis );
			case MAX:
				return selectSeriesMax( xAxis, yAxis );
			case MIN:
				return selectSeriesMin( xAxis, yAxis );
		}

		throw new UnsupportedOperationException( );
	}

	private XYChart.Series < X, Number > selectSeriesEachNth( List < X > xAxis, List < Number > yAxis )
	{
		XYChart.Series < X, Number > series = new XYChart.Series <>( );

		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += n )
		{
			series.getData( ).add( new XYChart.Data <>( xAxis.get( i ), yAxis.get( i ) ) );
		}

		return series;
	}

	private XYChart.Series < X, Number > selectSeriesSum( List < X > xAxis, List < Number > yAxis )
	{
		XYChart.Series < X, Number > series = new XYChart.Series <>( );

		float value = 0.0f;
		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			if ( i % n == ( n - 1 ) )
			{
				series.getData( ).add( new XYChart.Data <>( xAxis.get( i ), value ) );
				value = 0.0f;
			}

			value += yAxis.get( i ).floatValue( );
		}

		return series;
	}

	private XYChart.Series < X, Number > selectSeriesArithmeticMean( List < X > xAxis, List < Number > yAxis )
	{
		XYChart.Series < X, Number > series = selectSeriesSum( xAxis, yAxis );

		for ( int i = 0; i < series.getData( ).size( ); i += 1 )
		{
			series.getData( ).get( i ).setYValue( series.getData( ).get( i ).getYValue( ).floatValue( ) / ( float ) n );
		}

		return series;
	}

	private XYChart.Series < X, Number > selectSeriesMax( List < X > xAxis, List < Number > yAxis )
	{
		XYChart.Series < X, Number > series = new XYChart.Series <>( );

		float value = yAxis.get( 0 ).floatValue( );
		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			if ( i % n == ( n - 1 ) )
			{
				series.getData( ).add( new XYChart.Data <>( xAxis.get( i ), value ) );
				value = yAxis.get( i ).floatValue( );
			}

			if ( yAxis.get( i ).floatValue( ) > value )
			{
				value = yAxis.get( i ).floatValue( );
			}
		}

		return series;
	}

	private XYChart.Series < X, Number > selectSeriesMin( List < X > xAxis, List < Number > yAxis )
	{
		XYChart.Series < X, Number > series = new XYChart.Series <>( );

		float value = yAxis.get( 0 ).floatValue( );
		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			if ( i % n == ( n - 1 ) )
			{
				series.getData( ).add( new XYChart.Data <>( xAxis.get( i ), value ) );
				value = yAxis.get( i ).floatValue( );
			}

			if ( yAxis.get( i ).floatValue( ) < value )
			{
				value = yAxis.get( i ).floatValue( );
			}
		}

		return series;
	}
}
