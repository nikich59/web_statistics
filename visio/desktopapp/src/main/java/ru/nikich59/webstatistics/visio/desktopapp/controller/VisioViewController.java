package ru.nikich59.webstatistics.visio.desktopapp.controller;


import ru.nikich59.webstatistics.visio.desktopapp.ViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visio.desktopapp.EventController;
import ru.nikich59.webstatistics.visio.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

/**
 * Created by Nikita on 30.12.2017.
 */
public class VisioViewController extends ViewController
{
	private VisioView view;


	public VisioViewController( Model model, VisioView view, ErrorHandler errorHandler, EventController eventController )
	{
		super( model, errorHandler, eventController );

		this.view = view;
	}
/*
	public TopMenuViewController createMenuController(  )
	{
		return new TopMenuViewController( getModel() );
	}

	public SeriesListViewController createSeriesListController( )
	{
		return seriesListController;
	}

	public ChartViewController createChartController( )
	{
		return chartController;
	}*/
}



