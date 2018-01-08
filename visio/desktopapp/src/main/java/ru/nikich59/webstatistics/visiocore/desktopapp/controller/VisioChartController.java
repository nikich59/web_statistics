package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.ChartView;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visiocore.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visiocore.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 31.12.2017.
 */
public class VisioChartController
{
	public void setAllowXAxisZoom( boolean allowXAxisZoom )
	{
		this.allowXAxisZoom = allowXAxisZoom;
	}

	public boolean allowXAxisZoom( )
	{
		return allowXAxisZoom;
	}

	public boolean allowYAxisZoom( )
	{
		return allowYAxisZoom;
	}

	public void setAllowYAxisZoom( boolean allowYAxisZoom )
	{
		this.allowYAxisZoom = allowYAxisZoom;
	}

	public enum ChartingMode
	{
		LINE_CHART,
		AREA_CHART;

		private static final String LINE_CHART_STRING = "Line chart";
		private static final String AREA_CHART_STRING = "Area chart";

		public static ChartingMode fromString( String s )
		{
			for ( ChartingMode mode : ChartingMode.values( ) )
			{
				if ( mode.toString( ).equals( s ) )
				{
					return mode;
				}
			}

			return null;
		}

		@Override
		public String toString( )
		{
			switch ( this )
			{
				case LINE_CHART:
					return LINE_CHART_STRING;
				case AREA_CHART:
					return AREA_CHART_STRING;
			}

			return super.toString( );
		}
	}

	private boolean allowXAxisZoom = true;
	private boolean allowYAxisZoom = false;


	private Model model;
	private ChartView view;
	private ErrorHandler errorHandler;
	private VisioController visioController;


	private Point2D oldMousePos = new Point2D( 0.0, 0.0 );
	private boolean isMousePressed = false;

	private MouseButton moveMouseButton = MouseButton.PRIMARY;

	public VisioChartController( Model model, ChartView view,
								 ErrorHandler errorHandler, VisioController visioController )
	{
		this.model = model;
		this.view = view;
		this.errorHandler = errorHandler;
		this.visioController = visioController;
	}

	public void onScrollEvent( ScrollEvent scrollEvent )
	{
		if ( scrollEvent.getDeltaY( ) < 0 )
		{
			view.zoomOut( );
		}
		else
		{
			view.zoomIn( );
		}
	}

	public void updateView( )
	{
		List < XYChart.Series < Number, Number > > drawableSeriesList = new ArrayList <>( );
		for ( Model.StatisticsSeries statisticsSeries : model.getData( ) )
		{
			if ( statisticsSeries.isEnabled )
			{
				drawableSeriesList.add( statisticsSeries.series.getSeries( ) );
			}
		}

		view.setData( drawableSeriesList );
	}

	public void setChartingMode( ChartingMode chartingMode )
	{
		view.setChartingMode( chartingMode );
	}

	public void onMouseMoveEvent( MouseEvent mouseEvent )
	{
		if ( isMousePressed )
		{
			Point2D moveVector = new Point2D(
					- mouseEvent.getX( ) + oldMousePos.getX( ),
					mouseEvent.getY( ) - oldMousePos.getY( ) );

			view.moveCenter( moveVector );
		}

		oldMousePos = new Point2D( mouseEvent.getX( ), mouseEvent.getY( ) );
	}

	public void onMousePressEvent( MouseEvent mouseEvent )
	{
		if ( mouseEvent.getButton( ) != moveMouseButton )
		{
			return;
		}

		isMousePressed = true;

		oldMousePos = new Point2D( mouseEvent.getX( ), mouseEvent.getY( ) );
	}

	public void onMouseReleaseEvent( MouseEvent mouseEvent )
	{
		if ( mouseEvent.getButton( ) != moveMouseButton )
		{
			return;
		}

		isMousePressed = false;
	}
}
