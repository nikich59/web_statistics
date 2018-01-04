package ru.nikich59.webstatistics.statister;

import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.statister.sleuth.Sleuth;
import stats.controller.StatsFileController;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 27.12.2017.
 */
public class Controller
{
	private String sleuthDirectoryPath;
	private String statistersDirectoryPath;
	private String finishedStatisticsDirectoryPath;

	private Timer sleuthTimer;
	private Timer statistersTimer;

	private Model model;

	private int timersPeriodInMillis = 10000;

	public Controller( Model model )
	{
		this.model = model;
	}

	public void setSleuthDirectoryPath( String sleuthDirectoryPath )
	{
		this.sleuthDirectoryPath = sleuthDirectoryPath;
	}

	public void setStatistersDirectoryPath( String statistersDirectoryPath )
	{
		this.statistersDirectoryPath = statistersDirectoryPath;
	}

	public void setFinishedStatisticsDirectoryPath( String finishedStatisticsDirectoryPath )
	{
		this.finishedStatisticsDirectoryPath = finishedStatisticsDirectoryPath;
	}

	public void start( )
	{
		if ( sleuthTimer != null )
		{
			sleuthTimer.cancel( );
		}
		sleuthTimer = new Timer( );

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
						File file = new File( statistersDirectoryPath );

						File[] files = file.listFiles( );
						if ( files == null )
						{
							return;
						}

						for ( File configFile : files )
						{
							try
							{
								if ( ! model.hasStatister( configFile.getAbsolutePath( ) ) )
								{
									StatsFileController statsFileController = new StatsFileController( );
									statsFileController.setFinishedStatisticsDirectoryPath( finishedStatisticsDirectoryPath );
									statsFileController.setStatisticsDirectory( statistersDirectoryPath );

									model.addStatisticsAcquirer(
											new SiteStatisticsAcquirer( statsFileController ) );
								}
							}
							catch ( Exception e )
							{
								e.printStackTrace( );
							}
						}
					}
				},
				0,
				timersPeriodInMillis
		);

		sleuthTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						File file = new File( sleuthDirectoryPath );

						File[] files = file.listFiles( );
						if ( files == null )
						{
							return;
						}

						for ( File configFile : files )
						{
							try
							{
								if ( ! model.hasSleuth( configFile.getAbsolutePath( ) ) )
								{
									model.addSleuth( new Sleuth( configFile.getAbsolutePath( ) ) );
								}
							}
							catch ( Exception e )
							{
								e.printStackTrace( );
							}
						}
					}
				},
				0,
				timersPeriodInMillis
		);

	}
}









