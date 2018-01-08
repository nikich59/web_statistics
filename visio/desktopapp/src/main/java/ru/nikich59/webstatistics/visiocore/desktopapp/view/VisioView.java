package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visiocore.model.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the top view of desktop application.
 */

public class VisioView extends BasicVisioView
{
	public SeriesListView getSeriesListView( )
	{
		return seriesListView;
	}

	/**
	 * This view contains list of all series that can be exhibited.
	 */
	@FXML
	private SeriesListView seriesListView;


	public ChartView getChartView( )
	{
		return chartView;
	}

	public VisioMenuView getMenuView( )
	{
		return menuView;
	}

	/**
	 * This is the view that contains the chart exhibiting all the selected series.
	 */
	@FXML
	private ChartView chartView;


	@FXML
	private VisioMenuView menuView;


	/**
	 * These are series that can be exhibited.
	 */
	private List < Model.StatisticsSeries > statisticsSeries = new ArrayList <>( );

	private VisioController controller;


	public VisioView( )
			throws IOException
	{
		System.out.println( getClass( ).getResource( "visio_view.fxml" ) );




		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
				getClass( ).getResource( "visio_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );
		/*
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_view.fxml" ), this );*/
	}

	public void updateChartView( )
	{
		List < XYChart.Series < Number, Number > > series = new ArrayList <>( );
		for ( Model.StatisticsSeries statisticsSeries : this.statisticsSeries )
		{
			if ( statisticsSeries.isEnabled )
			{
				series.add( statisticsSeries.series.getSeries( ) );
			}
		}

		chartView.getChartData( ).setAll( series );
	}

	private void initializeUI( )
	{
		/*
		seriesListView.setErrorHandler( getErrorHandler( ) );
		chartView.setErrorHandler( getErrorHandler( ) );
		*/
	}

	public void setController( VisioController controller )
	{
		this.controller = controller;

		menuView.setController( controller.getMenuController( ) );

		seriesListView.setController( controller.getSeriesListController( ) );

		chartView.setController( controller.getChartController( ) );
	}
}









