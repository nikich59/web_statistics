package ru.nikich59.webstatistics.visio.visiocore.model;

import ru.nikich59.webstatistics.visio.visiocore.model.series.PlainSeries;
import ru.nikich59.webstatistics.visio.visiocore.model.series.Series;
import stats.Statistics;
import stats.controller.StatsController;
import stats.controller.StatsControllerFactory;

import java.io.IOException;
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
		public Series < Number, Number > series;
		public ZonedDateTime seriesBeginDateTime;
		public String id = "";
		public boolean isEnabled;
		public Statistics.StatisticsHeader statisticsHeader;
	}

	private List < String > statisticsDirectories = new ArrayList <>( );

	private List < StatisticsSeries > statisticsSeries = new ArrayList <>( );

	public void loadStatisticsWithController( StatsController statsController )
			throws IOException
	{
		statsController.loadStatistics( );

		addStatistics( statsController.getStatistics( ) );
	}

	private void addStatistics( Statistics statistics )
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
			series.seriesBeginDateTime = statistics.getHeader( ).getInitialDateTime( );

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
			plainSeries.setName( statistics.getHeader( ).getColumnNames( ).get( columnIndex ) );
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
			series.statisticsHeader = statistics.getHeader( );

			statisticsSeries.add( series );
		}
	}

	public void addSeries( Series < Number, Number > series, ZonedDateTime seriesBeginDateTime )
	{
		StatisticsSeries newStatisticsSeries = new StatisticsSeries( );
		newStatisticsSeries.series = series;
		newStatisticsSeries.id = String.valueOf( statisticsSeries.size( ) );
		newStatisticsSeries.seriesBeginDateTime = seriesBeginDateTime;
		statisticsSeries.add( newStatisticsSeries );
	}

	public boolean removeSeries( String seriesId )
	{
		StatisticsSeries targetSeries = null;
		for ( StatisticsSeries series : statisticsSeries )
		{
			if ( series.id.equals( seriesId ) )
			{
				targetSeries = series;
			}
		}

		if ( targetSeries != null )
		{
			return statisticsSeries.remove( targetSeries );
		}

		return false;
	}

	public void clearSeries( )
	{
		statisticsSeries.clear( );
	}

	public List < StatisticsSeries > getData( )
	{
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

	public void addStatisticsDirectory( String statisticsDirectory )
	{
		statisticsDirectories.add( statisticsDirectory );
	}

	public List < StatsController > getAvailableControllers( )
	{
		List < StatsController > availableControllers = new ArrayList <>( );


		for ( String statisticsDirectory : statisticsDirectories )
		{
			StatsControllerFactory factory = new StatsControllerFactory( statisticsDirectory );
			availableControllers.addAll( factory.listStatsInDirectory( ) );
		}

		return availableControllers;
	}
}
