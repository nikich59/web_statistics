package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

import ru.nikich59.webstatistics.visiocore.desktopapp.view.VisioMenuView;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.dialog.AddStatisticsDialog;
import ru.nikich59.webstatistics.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visiocore.model.Model;
import stats.controller.StatsController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 08.01.2018.
 */
public class VisioMenuController
{
	private Model model;
	private VisioMenuView view;
	private ErrorHandler errorHandler;
	private VisioController visioController;

	public ErrorHandler getErrorHandler( )
	{
		return errorHandler;
	}


	public VisioMenuController( Model model, VisioMenuView view,
								ErrorHandler errorHandler, VisioController visioController )
	{
		this.model = model;
		this.view = view;
		this.errorHandler = errorHandler;
		this.visioController = visioController;
	}

	public List < StatsController > getAvailableControllers( )
	{
		return model.getAvailableControllers( );
	}

	public void addStatsController( StatsController statsController )
	{
		try
		{
			model.loadStatisticsWithController( statsController );
		}
		catch ( IOException e )
		{
			errorHandler.handleException( e );
		}
	}

	public void addStatisticsEvent( )
	{
		AddStatisticsDialog addStatisticsDialog = getAddStatisticsDialog( );
		if ( addStatisticsDialog == null )
		{
			return;
		}

		Optional < StatsController > result = addStatisticsDialog.showAndWait( );

		result.ifPresent( this::addStatsController );

		visioController.getSeriesListController( ).updateView( );

		visioController.getChartController( ).updateView( );
	}

	public AddStatisticsDialog getAddStatisticsDialog( )
	{
		try
		{
			return new AddStatisticsDialog( model.getAvailableControllers( ) );
		}
		catch ( IOException e )
		{
			errorHandler.handleException( e );
		}

		return null;
	}
}
