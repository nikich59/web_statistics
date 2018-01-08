package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

//import com.sun.istack.internal.NotNull;
//import com.sun.istack.internal.NotNull;

import ru.nikich59.webstatistics.visiocore.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visiocore.model.Model;

/**
 * Created by Nikita on 30.12.2017.
 */
public class VisioController
{
	private Model model;
	private VisioView view;
	private ErrorHandler errorHandler;

	private VisioMenuController menuController;
	private SeriesListController seriesListController;
	private VisioChartController chartController;

	public void setModel( Model model )
	{
		this.model = model;
	}

	public void setView( VisioView view )
	{
		this.view = view;
	}

	public VisioController( Model model, VisioView view, ErrorHandler errorHandler )
	{
		this.model = model;
		this.view = view;
		this.errorHandler = errorHandler;

		this.menuController = new VisioMenuController( model, view.getMenuView( ), errorHandler, this );
		this.seriesListController = new SeriesListController( model, view.getSeriesListView( ), errorHandler, this );
		this.chartController = new VisioChartController( model, view.getChartView( ), errorHandler, this );
	}

	public VisioMenuController getMenuController( )
	{
		return menuController;
	}

	public SeriesListController getSeriesListController( )
	{
		return seriesListController;
	}

	public VisioChartController getChartController( )
	{
		return chartController;
	}
}



