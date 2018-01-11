package ru.nikich59.webstatistics.statister.desktopapp.controller;

import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.desktopapp.ViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.SleuthItemView;
import ru.nikich59.webstatistics.statister.desktopapp.view.StatisterItemView;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterItemViewController extends ViewController
{
	private StatisterItemView view;
	private SiteStatisticsAcquirer statisticsAcquirer;
	private StatisterListViewController statisterListViewController;

	public StatisterItemViewController( StatisterListViewController statisterListViewController,
										StatisterItemView view,
										SiteStatisticsAcquirer statisticsAcquirer )
	{
		super( statisterListViewController );

		this.statisticsAcquirer = statisticsAcquirer;
		this.view = view;
		this.statisterListViewController = statisterListViewController;
	}
}
