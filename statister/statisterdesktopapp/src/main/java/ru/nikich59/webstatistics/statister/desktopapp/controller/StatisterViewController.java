package ru.nikich59.webstatistics.statister.desktopapp.controller;

import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.desktopapp.EventController;
import ru.nikich59.webstatistics.statister.desktopapp.view.StatisterView;
import ru.nikich59.webstatistics.statister.desktopapp.view.dialog.AddSleuthDialog;
import ru.nikich59.webstatistics.statister.desktopapp.view.dialog.AddStatisterDialog;
import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterViewController extends ru.nikich59.webstatistics.statister.desktopapp.ViewController
{
	private StatisterView view;

	private Timer updateTimer;


	public StatisterViewController( Model model, StatisterView view,
									ErrorHandler errorHandler, EventController eventController )
	{
		super( model, errorHandler, eventController );

		initialize( view );
	}

	private void initialize( StatisterView view )
	{
		if ( updateTimer != null )
		{
			updateTimer.cancel( );
		}

		updateTimer = new Timer( );
		updateTimer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						getEventController( ).emitUpdateEvent( );
					}
				},
				0,
				100
		);

		this.view = view;
	}

	public void onAddSleuthEvent( )
	{
		AddSleuthDialog addSleuthDialog;

		try
		{
			addSleuthDialog = new AddSleuthDialog(
					this,
					getModel( ).getSleuthFactory( ),
					"C:\\main\\projects\\webstatistics\\web_statistics\\statister\\" +
							"statisterdesktopapp\\stats\\sleuth_templates\\" );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );

			return;
		}

		Optional < SleuthController > sleuthController = addSleuthDialog.showAndWait( );

		if ( sleuthController.isPresent( ) )
		{
			Exception exception = null;
			for ( int attemptIndex = 0; attemptIndex < 3; attemptIndex += 1 )
			{
				try
				{
					sleuthController.get( ).getSleuth( ).testConnection( );

					break;
				}
				catch ( Exception e )
				{
					exception = e;
				}
			}

			if ( exception != null )
			{
				exception.printStackTrace( );

				return;

				// TODO: Implement error handling.
			}

			sleuthController.ifPresent( ( sleuthController1 ) -> getModel( ).addSleuth( sleuthController1 ) );
		}
	}

	public void onAddStatisterEvent( )
	{
		AddStatisterDialog addStatisterDialog;

		try
		{
			addStatisterDialog = new AddStatisterDialog(
					this,
					getModel( ).getStatsControllerFactory( ),
					"C:\\main\\projects\\webstatistics\\web_statistics\\statister\\" +
							"statisterdesktopapp\\stats\\statister_templates\\" );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );

			return;
		}

		Optional < SiteStatisticsAcquirer > siteStatisticsAcquirer = addStatisterDialog.showAndWait( );

		if ( siteStatisticsAcquirer.isPresent( ) )
		{
			Exception exception = null;
			for ( int attemptIndex = 0; attemptIndex < 3; attemptIndex += 1 )
			{
				try
				{
					siteStatisticsAcquirer.get( ).testConnection( );

					break;
				}
				catch ( Exception e )
				{
					exception = e;
				}
			}

			if ( exception != null )
			{
				exception.printStackTrace( );

				return;

				// TODO: Implement error handling.
			}

			getModel( ).addStatisticsAcquirer( siteStatisticsAcquirer.get( ) );
		}
	}
}
