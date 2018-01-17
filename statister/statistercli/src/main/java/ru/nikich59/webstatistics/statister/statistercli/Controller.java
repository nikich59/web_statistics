package ru.nikich59.webstatistics.statister.statistercli;

import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 09.01.2018.
 */
public class Controller
{
	private Timer reportTimer;

	public void setModel( Model model )
	{
		this.model = model;
	}
	private Model model;

	private long reportTimerPeriodInMillis = 60000;

	private Timer sleuthTimer;
	private Timer statistersTimer;

	private long sleuthTimerPeriodInMillis = 10000;
	private long statisterTimerPeriodInMillis = 10000;


	public Controller( Map < String, Object > configMap )
	{
		if ( configMap.get( "report_period_in_millis" ) != null )
		{
			reportTimerPeriodInMillis = Long.parseLong( configMap.get( "report_period_in_millis" ).toString( ) );
		}
		if ( configMap.get( "sleuth_timer_period_in_millis" ) != null )
		{
			sleuthTimerPeriodInMillis = Long.parseLong( configMap.get( "sleuth_timer_period_in_millis" ).toString( ) );
		}
		if ( configMap.get( "statister_timer_period_in_millis" ) != null )
		{
			statisterTimerPeriodInMillis =
					Long.parseLong( configMap.get( "statister_timer_period_in_millis" ).toString( ) );
		}

		initializeReportTimer( );
	}

	public void stop( )
	{
		if ( reportTimer != null )
		{
			reportTimer.cancel( );
		}

		if ( sleuthTimer != null )
		{
			sleuthTimer.cancel( );
		}

		if ( statistersTimer != null )
		{
			statistersTimer.cancel( );
		}
	}

	protected void initializeReportTimer( )
	{
		if ( reportTimer != null )
		{
			reportTimer.cancel( );
		}

		reportTimer = new Timer( );

		reportTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						System.out.println( );
						System.out.println( "As for " + LocalDateTime.now( ).toString( ) + ": " );

						for ( SiteStatisticsAcquirer siteStatisticsAcquirer : model.getSiteStatisticsAcquirers( ) )
						{
							String message = "";
							if ( siteStatisticsAcquirer.getLastException( ) == null )
							{
								message = "OK";
							}
							else
							{
								message = siteStatisticsAcquirer.getLastException( ).toString( );
							}

							System.out.println( "	Statistics acquirer for " +
									siteStatisticsAcquirer.getUrl( ) + " :\n		" + message );
						}

						for ( SleuthController sleuthController : model.getSleuthList( ) )
						{
							String message = "";
							if ( sleuthController.getSleuth( ).getLastException( ) == null )
							{
								message = "OK";
							}
							else
							{
								message = sleuthController.getSleuth( ).getLastException( ).toString( );
							}

							System.out.println( "	Sleuth for " +
									sleuthController.getSleuth( ).getUrl( ) + " :\n		" + message );
						}

						System.out.println( "---" );
						System.out.println( );
						System.out.println( );
					}
				},
				2000,
				reportTimerPeriodInMillis
		);
	}

	protected void initializeSleuthTimer( )
	{
		if ( sleuthTimer != null )
		{
			sleuthTimer.cancel( );
		}
		sleuthTimer = new Timer( );

		sleuthTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						model.updateSleuthList( );
					}
				},
				0,
				sleuthTimerPeriodInMillis
		);
	}

	public void initializeSiteAcquirersTimer( )
	{
		if ( statistersTimer != null )
		{
			statistersTimer.cancel( );
		}
		statistersTimer = new Timer( );

		statistersTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						model.updateStatistersList( );
					}
				},
				0,
				statisterTimerPeriodInMillis
		);
	}

	public void start( )
	{
		initializeReportTimer( );

		initializeSiteAcquirersTimer( );

		initializeSleuthTimer( );
	}
}
