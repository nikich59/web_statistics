package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioChartController;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visio.model.VisioModel;
import stats.controller.StatsController;
import stats.controller.StatsFileController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 29.12.2017.
 */

public class VisioView extends StackPane
{
	@FXML
	private TextField textField;

	@FXML
	private SeriesView seriesView;

	@FXML
	private VisioChartView visioChartView;


	private List < VisioModel.StatisticsSeries > statisticsSeries = new ArrayList <>( );

	private VisioController controller;


	public VisioView( VisioController controller )
	{
		System.out.println( getClass( ).getResource( "visio_view.fxml" ) );



		FXMLLoader fxmlLoader = new FXMLLoader( getClass( ).getResource( "visio_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		try
		{
			fxmlLoader.load( );
		}
		catch ( IOException exception )
		{
			throw new RuntimeException( exception );
		}

		setController( controller );
	}

	public void setData( List < VisioModel.StatisticsSeries > data )
	{
		this.statisticsSeries.clear( );
		this.statisticsSeries.addAll( data );

		List < XYChart.Series < Number, Number > > series = new ArrayList <>( );
		for ( VisioModel.StatisticsSeries statisticsSeries : this.statisticsSeries )
		{
			if ( statisticsSeries.isEnabled )
			{
				series.add( statisticsSeries.series.getSeries( ) );
			}
		}

		visioChartView.getChartData( ).setAll( series );

		seriesView.update( data );
	}

	public NumberAxis getChartXAxis( )
	{
		return visioChartView.getChartXAxis( );
	}

	public NumberAxis getChartYAxis( )
	{
		return visioChartView.getChartYAxis( );
	}


	public void setChartingMode( VisioChartView.ChartingMode chartingMode )
	{
		visioChartView.setChartingMode( chartingMode );
	}

	public void setController( VisioController controller )
	{
		this.controller = controller;

		seriesView.setController( new SeriesListController( controller ) );

		visioChartView.setController( new VisioChartController( controller ) );
	}

	@FXML
	private void menuFileAddClicked( )
	{
		AddStatisticsDialog addStatisticsDialog = new AddStatisticsDialog( controller.getStatisticsDirectory( ) );

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

		StatsFileController statsFileController = new StatsFileController( );
		statsFileController.setStatisticsDirectory( file.getAbsolutePath( ) );

		controller.addStatsController( statsFileController );
	}

	@FXML
	private void autoRange( )
	{
		visioChartView.autoRangeXAxis( );
		visioChartView.autoRangeYAxis( );
	}

	@FXML
	private void forceYAxisZeroInRange( )
	{
		visioChartView.setYAxisForceZeroInRange( true );
	}

	public void zoomChartIn( )
	{
		visioChartView.zoomIn( );
	}

	public void zoomChartOut( )
	{
		visioChartView.zoomOut( );
	}

	public void moveChart( Point2D move )
	{
		visioChartView.moveCenter( move );
	}
}









