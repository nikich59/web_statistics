package ru.nikich59.webstatistics.statister.model;

import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.sleuth.Sleuth;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.statister.sleuth.SleuthFactory;
import stats.controller.StatsController;
import stats.controller.StatsControllerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nikita on 27.12.2017.
 */
public class Model
{
	private List < SleuthController > sleuthList = new ArrayList <>( );
	private List < SiteStatisticsAcquirer > statisticsAcquirers = new ArrayList <>( );

	private StatsControllerFactory statsControllerFactory;
	private SleuthFactory sleuthFactory;

	public Model( JSONObject configObject )
	{
		statsControllerFactory = new StatsControllerFactory( configObject );

		sleuthFactory = new SleuthFactory( configObject );
	}

	public List < SiteStatisticsAcquirer > getSiteStatisticsAcquirers( )
	{
		List < SiteStatisticsAcquirer > statisticsAcquirersToRemove = new ArrayList <>( );
		for ( SiteStatisticsAcquirer statisticsAcquirer : statisticsAcquirers )
		{
			if ( ! statisticsAcquirer.isRunning( ) )
			{
				statisticsAcquirersToRemove.add( statisticsAcquirer );
			}
		}

		statisticsAcquirers.removeAll( statisticsAcquirersToRemove );

		ArrayList < SiteStatisticsAcquirer > arrayCopy = new ArrayList <>( );
		arrayCopy.addAll( statisticsAcquirers );

		return Collections.unmodifiableList( arrayCopy );
	}

	public void stop( )
	{
		for ( SleuthController sleuthController : sleuthList )
		{
			sleuthController.getSleuth( ).stop( );
		}

		for ( SiteStatisticsAcquirer statisticsAcquirer : statisticsAcquirers )
		{
			try
			{
				statisticsAcquirer.stop( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );

				// TODO: Implement error handling.
			}
		}
	}

	public SleuthFactory getSleuthFactory( )
	{
		return sleuthFactory;
	}

	public StatsControllerFactory getStatsControllerFactory( )
	{
		return statsControllerFactory;
	}

	public void addSleuth( SleuthController sleuthController )
	{
		sleuthList.add( sleuthController );

		sleuthController.getSleuth( ).setSiteDescriptorConsumer( ( statisticsHeader ) ->
				{
					StatsController statsController;
					try
					{
						statsController = statsControllerFactory.createStatisticsController( statisticsHeader );
					}
					catch ( IOException e )
					{
						e.printStackTrace( );

						return;
					}

					SiteStatisticsAcquirer siteStatisticsAcquirer = new SiteStatisticsAcquirer( statsController );

					addStatisticsAcquirer( siteStatisticsAcquirer );
				}
		);

		try
		{
			sleuthController.storeSleuth( );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );

			// TODO: Implement error handling.
		}

		sleuthController.getSleuth( ).start( );
	}

	public void removeSleuth( String id )
			throws IOException
	{
		SleuthController controllerToRemove = null;

		for ( SleuthController sleuthController : sleuthList )
		{
			if ( id.equals( sleuthController.getId( ) ) )
			{
				controllerToRemove = sleuthController;
			}
		}

		if ( controllerToRemove != null )
		{
			sleuthList.remove( controllerToRemove );
			controllerToRemove.stop( );
		}
	}

	public void removeStatister( String id )
			throws IOException
	{
		SiteStatisticsAcquirer statisticsAcquirerToRemove = null;

		for ( SiteStatisticsAcquirer statisticsAcquirer : statisticsAcquirers )
		{
			if ( id.equals( statisticsAcquirer.getStatisticsId( ) ) )
			{
				statisticsAcquirerToRemove = statisticsAcquirer;
			}
		}

		if ( statisticsAcquirerToRemove != null )
		{
			statisticsAcquirerToRemove.finish( );
			statisticsAcquirers.remove( statisticsAcquirerToRemove );
		}
	}

	public void updateStatistersList( )
	{
		List < StatsController > statsControllers = statsControllerFactory.listStatsInDirectory( );
		for ( StatsController statsController : statsControllers )
		{
			try
			{
				statsController.loadStatisticsCaption( );
			}
			catch ( Exception e )
			{
				// TODO: Implement error handling.
				e.printStackTrace( );
			}

			if ( ! hasStatister( statsController.getId( ) ) )
			{
				addStatisticsAcquirer( new SiteStatisticsAcquirer( statsController ) );
			}
		}
	}

	public void updateSleuthList( )
	{
		List < SleuthController > sleuthList = sleuthFactory.listSleuth( );
		for ( SleuthController sleuthController : sleuthList )
		{
			try
			{
				sleuthController.loadSleuth( );
			}
			catch ( Exception e )
			{
				// TODO: Implement error handling.
				e.printStackTrace( );
			}

			if ( ! hasSleuth( sleuthController.getId( ) ) )
			{
				Sleuth sleuth = sleuthController.getSleuth( );
				sleuth.setSleuthController( sleuthController );
				addSleuth( sleuthController );
			}
		}
	}

	public void addStatisticsAcquirer( SiteStatisticsAcquirer siteStatisticsAcquirer )
	{
		if ( hasStatister( siteStatisticsAcquirer.getStatisticsId( ) ) )
		{
			return;
		}

		statisticsAcquirers.add( siteStatisticsAcquirer );

		try
		{
			siteStatisticsAcquirer.start( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	public List < SleuthController > getSleuthList( )
	{
		ArrayList < SleuthController > arrayCopy = new ArrayList <>( );
		arrayCopy.addAll( sleuthList );

		return Collections.unmodifiableList( arrayCopy );
	}

	public boolean hasSleuth( String sleuthId )
	{
		for ( SleuthController sleuthController : sleuthList )
		{
			if ( sleuthController.getId( ) != null &&
					sleuthController.getId( ).equals( sleuthId ) )
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasStatister( String statisticsId )
	{
		for ( SiteStatisticsAcquirer statisticsAcquirer : statisticsAcquirers )
		{
			if ( statisticsAcquirer.getStatisticsId( ) != null &&
					statisticsAcquirer.getStatisticsId( ).equals( statisticsId ) )
			{
				return true;
			}
		}

		return false;
	}
}
















