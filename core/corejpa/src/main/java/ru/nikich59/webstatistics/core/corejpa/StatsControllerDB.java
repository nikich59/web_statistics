package ru.nikich59.webstatistics.core.corejpa;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.WebDataType;
import ru.nikich59.webstatistics.core.corebasics.stats.controller.StatsController;
import ru.nikich59.webstatistics.core.corejpa.entity.StatisticsDataPoint;
import ru.nikich59.webstatistics.core.corejpa.entity.StatisticsValue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikita on 13.01.2018.
 */
public class StatsControllerDB extends StatsController
{
	private EntityManagerFactory entityManagerFactory;

	private Statistics statistics = null;

	public Statistics.StatisticsHeader loadStatisticsCaption( )
			throws IOException
	{
		return statistics.getHeader( );
	}

	public void storeStatistics( Statistics statistics )
			throws IOException
	{
		this.statistics = statistics;

		storeCaption( );

		for ( Statistics.DataPoint dataPoint : statistics.getDataPoints( ) )
		{
			appendData( dataPoint );
		}
	}

	private void storeCaption( )
	{
		EntityManager entityManager = entityManagerFactory.createEntityManager( );

		entityManager.getTransaction( ).begin( );

		ru.nikich59.webstatistics.core.corejpa.entity.Statistics dbStatitics = new
				ru.nikich59.webstatistics.core.corejpa.entity.Statistics( );

		dbStatitics.setExpirationPeriodInMinutes( statistics.getHeader( ).getExpirationPeriodInMinutes( ) );
		dbStatitics.setFinished( statistics.getHeader( ).isFinished( ) );
		dbStatitics.setHeadline( statistics.getHeader( ).getHeadline( ) );
		dbStatitics.setInitialDateTime( statistics.getHeader( ).getInitialDateTime( ) );
		dbStatitics.setPeriodInMillis( statistics.getHeader( ).getPeriodInMillis( ) );
		dbStatitics.setUrl( statistics.getHeader( ).getUrl( ) );
		dbStatitics.setWebDataType( ru.nikich59.webstatistics.core.corejpa.entity.WebDataType.findByName(
				entityManager, statistics.getHeader( ).getDataType( ).toString( ) ) );

		entityManager.persist( dbStatitics );

		entityManager.getTransaction( ).commit( );
	}

	public void initStatistics( Statistics.StatisticsHeader statisticsHeader )
			throws IOException
	{

	}

	public void finish( )
			throws IOException
	{

	}

	public void appendData( Statistics.DataPoint dataPoint )
			throws IOException
	{
		EntityManager entityManager = entityManagerFactory.createEntityManager( );

		entityManager.getTransaction( ).begin( );

		StatisticsDataPoint dbDataPoint = new StatisticsDataPoint( );

		dbDataPoint.setStatistics(
				ru.nikich59.webstatistics.core.corejpa.entity.Statistics.findByUrl(
						entityManager, statistics.getHeader( ).getUrl( ) ) );

		for ( String dataValue : dataPoint.getData( ) )
		{
			StatisticsValue value = new StatisticsValue( );

			value.setDataPoint( dbDataPoint );

			List < StatisticsValue > statisticsValues =
					StatisticsValue.findValuesByDataPoint( entityManager, dbDataPoint );

			value.setValue( dataValue );


		}
	}

	public void loadStatistics( )
			throws IOException
	{
		ru.nikich59.webstatistics.core.corejpa.entity.Statistics dbStatistics =
				ru.nikich59.webstatistics.core.corejpa.entity.Statistics.findByUrl(
						entityManagerFactory.createEntityManager( ), statistics.getHeader( ).getUrl( ) );

		statistics = getStatsFromDB( dbStatistics, true );
	}

	public String getId( )
	{
		return statistics.getHeader( ).getStatisticsId( ) + ".db";
	}

	public Statistics getStatistics( )
	{
		return statistics;
	}

	public Statistics.StatisticsHeader getStatisticsHeader( )
	{
		return statistics.getHeader( );
	}

	public void initFromMap( Map < String, Object > configMap )
	{
		/*
		entityManagerFactory = ru.nikich59.webstatistics.core.corejpa.EntityManagerFactory.
				createEntityManagerFactory( ( String ) configMap.get( "database_connection_string" ) );
				*/
		Map < String, String > properties = new HashMap <>( );

		properties.put( "javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/statsdb" );
		properties.put( "javax.persistence.jdbc.driver", "org.postgresql.Driver" );
		properties.put( "javax.persistence.jdbc.user", "postgres" );
		properties.put( "javax.persistence.jdbc.password", "142434" );

		entityManagerFactory = Persistence.createEntityManagerFactory( "Statistics", properties );
	}

	public List < StatsController > listStatistics( )
	{
		List < StatsController > statsControllers = new ArrayList <>( );

		List < ru.nikich59.webstatistics.core.corejpa.entity.Statistics > statisticsList =
				ru.nikich59.webstatistics.core.corejpa.entity.Statistics.listAll(
						entityManagerFactory.createEntityManager( ) );

		for ( ru.nikich59.webstatistics.core.corejpa.entity.Statistics dbStatistics : statisticsList )
		{
			StatsControllerDB statsController = new StatsControllerDB( );
			statsController.statistics = getStatsFromDB( dbStatistics, false );

			statsControllers.add( statsController );
		}

		return statsControllers;
	}

	private Statistics getStatsFromDB( ru.nikich59.webstatistics.core.corejpa.entity.Statistics dbStatistics,
									   boolean doLoadData )
	{
		EntityManager entityManager = entityManagerFactory.createEntityManager( );

		Statistics statistics = new Statistics( );

		Statistics.StatisticsHeader statisticsHeader = new Statistics.StatisticsHeader( );

		statisticsHeader.setFinished( dbStatistics.isFinished( ) );
		statisticsHeader.setDataType( WebDataType.fromString( dbStatistics.getWebDataType( ).getName( ) ) );
		statisticsHeader.setExpirationPeriodInMinutes( dbStatistics.getExpirationPeriodInMinutes( ) );
		statisticsHeader.setInitialDateTime( dbStatistics.getInitialDateTime( ) );
		statisticsHeader.setHeadline( dbStatistics.getHeadline( ) );
/*
		StatisticsExpirationChecker statisticsExpirationChecker =
				LinkExpirationCheckerToStatistics.findStatisticsExpirationCheckerByStatistics(
						entityManager, dbStatistics );

		if ( statisticsExpirationChecker != null )
		{
			statisticsHeader.setTargetColumnSum( Long.parseLong( statisticsExpirationChecker.getTargetValue( ) ) );
			statisticsHeader.setTargetValuePeriodInMinutes( statisticsExpirationChecker.getTargetPeriodInMinutes( ) );
		}
*/
		statisticsHeader.setUrl( dbStatistics.getUrl( ) );
		statistics.setHeader( statisticsHeader );

		if ( doLoadData )
		{
			statistics.setDataPoints( StatisticsDataPoint.findValuesByDataPoint( entityManager, dbStatistics ) );
		}

		this.statistics = statistics;

		return statistics;
	}
}
