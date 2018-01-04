package stats.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 01.01.2018.
 */
public class StatsControllerFactory
{
	public List < StatsController > listStatsInDirectory( String directory )
	{
		List < StatsController > statsControllers = new ArrayList <>( );


		StatsFileController statsFileController = new StatsFileController( );
		statsControllers.addAll( statsFileController.listStatistics( directory ) );

		// TODO: Add new types of StatsController here.

		return statsControllers;
	}
}
