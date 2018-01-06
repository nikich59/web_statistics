package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioChartController;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioController;
import ru.nikich59.webstatistics.visio.desktopapp.view.dialog.AddStatisticsDialog;
import ru.nikich59.webstatistics.visio.model.VisioModel;
import stats.controller.StatsController;
import stats.controller.StatsFileController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nikich59.webstatistics.visio.desktopapp.FXMLLoader.loadFxmlInto;

/**
 * Created by Nikita on 29.12.2017.
 */

public class VisioView extends BasicVisioView
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
			throws IOException
	{
		loadFxmlInto( getClass( ).getResource( "visio_view.fxml" ), this );

		seriesView.setErrorHandler( getErrorHandler( ) );
		visioChartView.setErrorHandler( getErrorHandler( ) );

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









