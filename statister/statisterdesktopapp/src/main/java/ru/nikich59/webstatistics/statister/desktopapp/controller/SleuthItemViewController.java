package ru.nikich59.webstatistics.statister.desktopapp.controller;

import ru.nikich59.webstatistics.statister.desktopapp.ViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.SleuthItemView;
import ru.nikich59.webstatistics.statister.sleuth.SleuthController;

/**
 * Created by Nikita on 10.01.2018.
 */
public class SleuthItemViewController extends ViewController
{
	private SleuthItemView view;
	private SleuthController sleuthController;
	private SleuthListViewController sleuthListViewController;

	public SleuthItemViewController( SleuthListViewController sleuthListViewController,
									 SleuthItemView view,
									 SleuthController sleuthController )
	{
		super( sleuthListViewController );

		this.sleuthController = sleuthController;
		this.view = view;
		this.sleuthListViewController = sleuthListViewController;
	}
}
