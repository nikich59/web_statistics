package ru.nikich59.webstatistics.visio.controller;

import ru.nikich59.webstatistics.visio.model.VisioModel;
import ru.nikich59.webstatistics.visio.model.series.Series;

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

	public void addSeries( VisioModel.StatisticsSeries series )
	{
		visioController.addSeries( series );
	}
}
