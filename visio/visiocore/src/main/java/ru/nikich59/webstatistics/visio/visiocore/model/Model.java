package ru.nikich59.webstatistics.visio.visiocore.model;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;
import ru.nikich59.webstatistics.core.corejpa.StatsControllerFactory;
import ru.nikich59.webstatistics.visio.visiocore.model.series.PlainSeries;
import ru.nikich59.webstatistics.visio.visiocore.model.series.Series;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

//	private List < String > statisticsDirectories = new ArrayList <>( );

	private StatsControllerFactory statsControllerFactory;

	private List < StatisticsSeries > statisticsSeries = new ArrayList <>( );

	public Model( Map < String, Object > configMap )
	{
		statsControllerFactory = new StatsControllerFactory( configMap );
	}

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

		for ( int columnIndex = 0; columnIndex < dataPoints.get( 0 ).getData( ).size( ); columnIndex += 1 )
		{
			StatisticsSeries series = new StatisticsSeries( );
			series.id = String.valueOf( statisticsSeries.size( ) );
			series.isEnabled = false;
			series.seriesBeginDateTime = statistics.getHeader( ).getInitialDateTime( );

			List < Number > xAxis = new ArrayList <>( );
			List < Number > yAxis = new ArrayList <>( );

			for ( int dataPointIndex = 0; dataPointIndex < dataPoints.size( ); dataPointIndex += 1 )
			{
				xAxis.add( dataPoints.get( dataPointIndex ).getDateTime( ).toEpochSecond( ) );
				try
				{
					yAxis.add( Float.parseFloat( dataPoints.get( dataPointIndex ).getData( ).get( columnIndex ) ) );
				}
				catch ( Exception e )
				{
					System.out.println( dataPoints.get( dataPointIndex ).getDateTime( ) );

					// TODO: Implement error handling.
				}
			}

			PlainSeries < Number, Number > plainSeries = new PlainSeries <>( );
			plainSeries.setName( statistics.getHeader( ).getValueDescriptions( ).get( columnIndex ).getName( ) );
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
	/*
		public void addStatisticsDirectory( String statisticsDirectory )
		{
			statisticsDirectories.add( statisticsDirectory );
		}
	*/
	public List < StatsController > getAvailableControllers( )
			throws IOException
	{
		return statsControllerFactory.listStats( );
		/*
		List < StatsController > availableControllers = new ArrayList <>( );


		for ( String statisticsDirectory : statisticsDirectories )
		{
			StatsControllerFactory factory = new StatsControllerFactory( statisticsDirectory );
			availableControllers.addAll( factory.listStatsInDirectory( ) );
		}

		return availableControllers;*/
	}
}
