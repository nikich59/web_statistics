package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.dialog.AddStatisticsDialog;
import ru.nikich59.webstatistics.visiocore.model.Model;
import stats.controller.StatsController;
import stats.controller.StatsFileController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the top view of desktop application.
 */

public class VisioView extends BasicVisioView
{
	/**
	 * This view contains list of all series that can be exhibited.
	 */
	@FXML
	private SeriesListView seriesListView;

	/**
	 * This is the view that contains the chart exhibiting all the selected series.
	 */
	@FXML
	private ChartView chartView;


	/**
	 * These are series that can be exhibited.
	 */
	private List < Model.StatisticsSeries > statisticsSeries = new ArrayList <>( );

	private VisioController controller;


	public VisioView( VisioController controller )
			throws IOException
	{
		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader( getClass( ).getResource(
				"visio_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );



		/*
		loadFxmlInto( getClass( ).getResource(
				"visio_view.fxml" ), this );*/




		initializeUI( );

		setController( controller );
	}

	private void initializeUI( )
	{
		/*
		seriesListView.setErrorHandler( getErrorHandler( ) );
		chartView.setErrorHandler( getErrorHandler( ) );
		*/
	}

	public void setData( List < Model.StatisticsSeries > data )
	{
		this.statisticsSeries.clear( );
		this.statisticsSeries.addAll( data );

		List < XYChart.Series < Number, Number > > series = new ArrayList <>( );
		for ( Model.StatisticsSeries statisticsSeries : this.statisticsSeries )
		{
			if ( statisticsSeries.isEnabled )
			{
				series.add( statisticsSeries.series.getSeries( ) );
			}
		}

		chartView.getChartData( ).setAll( series );

		seriesListView.update( data );
	}

	public NumberAxis getChartXAxis( )
	{
		return chartView.getChartXAxis( );
	}

	public NumberAxis getChartYAxis( )
	{
		return chartView.getChartYAxis( );
	}


	public void setChartingMode( ChartView.ChartingMode chartingMode )
	{
		chartView.setChartingMode( chartingMode );
	}

	public void setController( VisioController controller )
	{
		this.controller = controller;
/*
		seriesListView.setController( new SeriesListController( controller ) );

		chartView.setController( new VisioChartController( controller ) );*/
	}

	@FXML
	private void menuFileAddClicked( )
	{
		AddStatisticsDialog addStatisticsDialog = new AddStatisticsDialog(
				controller.getStatisticsDirectory( ), getErrorHandler( ) );

		Optional < StatsController > result = addStatisticsDialog.showAndWait( );

		result.ifPresent( ( statsController ) ->
				controller.addStatsController( statsController )
		);
	}

	@FXML
	private void menuStatsFileClicked( )
	{
		FileChooser fileChooser = new FileChooser( );
		fileChooser.setSelectedExtensionFilter(
				new FileChooser.ExtensionFilter( "Statistics files",
						"*" + StatsFileController.FILE_EXTENSION ) );

		File file = fileChooser.showOpenDialog( null );

		if ( file == null || ! file.exists( ) )
		{
			return;
		}

		StatsFileController statsFileController = new StatsFileController( );
		statsFileController.setStatisticsDirectory( file.getAbsolutePath( ) );

		controller.addStatsController( statsFileController );
	}

	@FXML
	private void autoRange( )
	{
		chartView.autoRangeXAxis( );
		chartView.autoRangeYAxis( );
	}

	@FXML
	private void forceYAxisZeroInRange( )
	{
		chartView.setYAxisForceZeroInRange( true );
	}

	public void zoomChartIn( )
	{
		chartView.zoomIn( );
	}

	public void zoomChartOut( )
	{
		chartView.zoomOut( );
	}

	public void moveChart( Point2D move )
	{
		chartView.moveCenter( move );
	}
}









