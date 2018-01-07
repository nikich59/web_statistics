package ru.nikich59.webstatistics.visiocore.model;

import ru.nikich59.webstatistics.visiocore.model.series.PlainSeries;
import ru.nikich59.webstatistics.visiocore.model.series.Series;
import stats.Statistics;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nikita on 28.12.2017.
 */

public class Model
{
	public static class StatisticsSeries
	{
		public Series< Number, Number > series;
		public ZonedDateTime seriesBeginDateTime;
		public String id = "";
		public boolean isEnabled;
		public Statistics statistics;
	}


	private List < StatisticsSeries > statisticsSeries = new ArrayList <>( );

	public void addStatistics( Statistics statistics )
	{
		List < Statistics.DataPoint > dataPoints = statistics.getDataPoints( );

		if ( dataPoints.isEmpty( ) )
		{
			return;
		}

		for ( int columnIndex = 0; columnIndex < dataPoints.get( 0 ).data.length; columnIndex += 1 )
		{
			StatisticsSeries series = new StatisticsSeries( );
			series.id = String.valueOf( statisticsSeries.size( ) );
			series.isEnabled = false;
			series.seriesBeginDateTime = statistics.getInitialDateTime( );

			List < Number > xAxis = new ArrayList <>( );
			List < Number > yAxis = new ArrayList <>( );

			for ( int dataPointIndex = 0; dataPointIndex < dataPoints.size( ); dataPointIndex += 1 )
			{
				xAxis.add( dataPoints.get( dataPointIndex ).dateTime.toEpochSecond( ) );
				try
				{
					yAxis.add( Float.parseFloat( dataPoints.get( dataPointIndex ).data[ columnIndex ] ) );
				}
				catch ( Exception e )
				{
					System.out.println( dataPoints.get( dataPointIndex ).dateTime );
				}
			}

			PlainSeries < Number, Number > plainSeries = new PlainSeries <>( );
			plainSeries.setName( statistics.getColumnNames( ).get( columnIndex ) );
			plainSeries.setxAxis( xAxis );
			plainSeries.setyAxis( yAxis );


			// TEST!
			/*
			EachNthSeriesSelector < Number > eachNthSeriesSelector = new EachNthSeriesSelector <>( );
			eachNthSeriesSelector.setN( 20 );
			eachNthSeriesSelector.setSelectMode( EachNthSeriesSelector.SelectMode.ARITHMETIC_MEAN );
			plainSeries.setSeriesSelector( eachNthSeriesSelector );
			*/
			// !TEST


			series.series = plainSeries;
			series.statistics = statistics;

			statisticsSeries.add( series );
		}
	}

	public void addSeries( StatisticsSeries series )
	{
		series.id = String.valueOf( statisticsSeries.size( ) );
		statisticsSeries.add( series );
	}

	public void clearSeries( )
	{
		statisticsSeries.clear( );
	}

	public List < StatisticsSeries > getData( )
	{
		return Collections.unmodifiableList( statisticsSeries );
	}

	public List < StatisticsSeries > getDrawableData( )
	{
		List < StatisticsSeries > statisticsSeries = new ArrayList <>( );

		for ( StatisticsSeries series : this.statisticsSeries )
		{
			if ( series.isEnabled )
			{
				statisticsSeries.add( series );
			}
		}

		return Collections.unmodifiableList( statisticsSeries );
	}

	public boolean setSeriesEnabled( String seriesId, boolean enabled )
	{
		for ( StatisticsSeries series : statisticsSeries )
		{
			if ( series.id.equals( seriesId ) )
			{
				series.isEnabled = enabled;

				return true;
			}
		}

		return false;
	}
}
