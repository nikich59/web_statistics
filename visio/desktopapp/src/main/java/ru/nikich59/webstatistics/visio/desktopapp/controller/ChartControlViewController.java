package ru.nikich59.webstatistics.visio.desktopapp.controller;

import ru.nikich59.webstatistics.visio.desktopapp.ViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.ChartControlView;

/**
 * Created by Nikita on 08.01.2018.
 */
public class ChartControlViewController extends ViewController
{
	public void setChartViewController( ChartViewController chartViewController )
	{
		this.chartViewController = chartViewController;
	}


	private ChartControlView view;
	private ChartViewController chartViewController;


	public ChartControlViewController( ChartControlView view, ChartViewController chartViewController )
	{
		super( chartViewController );

		this.chartViewController = chartViewController;

		this.view = view;
	}

	public void autoRangeChart( )
	{
		chartViewController.autoRange( );
	}

	public void forceYZeroInRange( )
	{
		chartViewController.setYAxisForceZeroInRange( true );
	}

	public void setAllowXAxisControl( boolean allowXAxisControl )
	{
		chartViewController.setAllowXAxisControl( allowXAxisControl );
	}

	public void setAllowYAxisControl( boolean allowYAxisControl )
	{
		chartViewController.setAllowYAxisControl( allowYAxisControl );
	}

	public boolean allowYAxisZoom( )
	{
		return chartViewController.allowYAxisControl( );
	}
}
