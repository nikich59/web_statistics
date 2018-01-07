package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

import ru.nikich59.webstatistics.visiocore.model.Model;

/**
 * Created by Nikita on 30.12.2017.
 */
public class SeriesListController
{
	private VisioController visioController;

	public SeriesListController( VisioController visioController )
	{
		this.visioController = visioController;
	}

	public void setSeriesEnabled( String seriesId, boolean enabled )
	{
		visioController.setSeriesEnabled( seriesId, enabled );
	}

	public void addSeries( Model.StatisticsSeries series )
	{
		visioController.addSeries( series );
	}
}
