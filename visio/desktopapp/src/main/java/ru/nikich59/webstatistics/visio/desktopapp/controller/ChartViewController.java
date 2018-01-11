package ru.nikich59.webstatistics.visio.desktopapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.StringConverter;
import ru.nikich59.webstatistics.visio.desktopapp.ViewController;
import ru.nikich59.webstatistics.visio.desktopapp.view.ChartView;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 31.12.2017.
 */
public class ChartViewController extends ViewController
{
	private class EventHandler extends ru.nikich59.webstatistics.visio.desktopapp.EventHandler
	{
		@Override
		public void onSeriesSetChangedEvent( )
		{
			updateView( );
		}

		@Override
		public void onSeriesEnableSetEvent( )
		{
			updateView( );
		}
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

	private boolean allowXAxisControl = true;
	public void setAllowXAxisControl( boolean allowXAxisControl )
	{
		this.allowXAxisControl = allowXAxisControl;
	}
	public boolean allowXAxisControl( )
	{
		return allowXAxisControl;
	}

	private boolean allowYAxisControl = false;
	public boolean allowYAxisControl( )
	{
		return allowYAxisControl;
	}
	public void setAllowYAxisControl( boolean allowYAxisControl )
	{
		this.allowYAxisControl = allowYAxisControl;
		if ( ! allowYAxisControl )
		{
			autoRangeYAxisInScope( );
		}
	}


	private ChartView view;


	private Point2D oldMousePos = new Point2D( 0.0, 0.0 );
	private boolean isMousePressed = false;

	private MouseButton moveMouseButton = MouseButton.PRIMARY;


	public ChartViewController( ChartView view, ViewController otherController )
	{
		super( otherController );

		subscribeToEvents( new EventHandler( ) );

		this.view = view;
	}

	public void setChartingMode( ChartViewController.ChartingMode chartingMode )
	{
		ObservableList < XYChart.Series < Number, Number > > oldData = FXCollections.observableArrayList( );
		NumberAxis oldXAxis = new NumberAxis( );
		NumberAxis oldYAxis = new NumberAxis( );
		if ( view.getChart( ) != null )
		{
			oldData = view.getChart( ).getData( );
			oldXAxis = ( NumberAxis ) view.getChart( ).getXAxis( );
			oldYAxis = ( NumberAxis ) view.getChart( ).getYAxis( );
		}

		oldXAxis.setForceZeroInRange( false );
		oldYAxis.setForceZeroInRange( false );

		oldXAxis.setTickLabelFormatter(
				new StringConverter < Number >( )
				{
					@Override
					public String toString( Number object )
					{
						return LocalDateTime.ofEpochSecond( object.longValue( ), 0, OffsetDateTime.now( )
								.getOffset( ) ).toString( );
					}

					@Override
					public Number fromString( String string )
					{
						return null;
					}
				}
		);

		XYChart < Number, Number > chart;

		switch ( chartingMode )
		{
			case LINE_CHART:
				LineChart < Number, Number > lineChart = new LineChart <>( oldXAxis, oldYAxis );
				lineChart.setCreateSymbols( false );
				chart = lineChart;

				break;
			case AREA_CHART:
				AreaChart < Number, Number > areaChart = new AreaChart <>( oldXAxis, oldYAxis );
				areaChart.setCreateSymbols( false );
				chart = areaChart;

				break;
			default:
				handleException( new UnsupportedOperationException( "Charting mode: \'" +
						chartingMode.toString( ) + "\' is not supported" ) );
				return;
		}

		chart.setData( oldData );

		view.setChart( chart );
	}

	public void onScrollEvent( ScrollEvent scrollEvent )
	{
		if ( scrollEvent.getDeltaY( ) < 0 )
		{
			zoomOut( );
		}
		else
		{
			zoomIn( );
		}
	}

	public void autoRange( )
	{
		autoRangeXAxis( );
		autoRangeYAxis( );
	}

	public void updateView( )
	{
		List < XYChart.Series < Number, Number > > drawableSeriesList = new ArrayList <>( );
		for ( Model.StatisticsSeries statisticsSeries : getModel( ).getData( ) )
		{
			if ( statisticsSeries.isEnabled )
			{
				drawableSeriesList.add( statisticsSeries.series.getSeries( ) );
			}
		}

		view.getChart( ).getData( ).setAll( drawableSeriesList );
	}

	public void setXAxisForceZeroInRange( boolean forceZeroInRange )
	{
		setAxisForceZeroInRange( ( NumberAxis ) view.getChart( ).getXAxis( ), forceZeroInRange );
	}

	public void setYAxisForceZeroInRange( boolean forceZeroInRange )
	{
		setAxisForceZeroInRange( ( NumberAxis ) view.getChart( ).getYAxis( ), forceZeroInRange );
	}

	public void zoomIn( )
	{
		if ( allowXAxisControl( ) )
		{
			zoomAxis( ( NumberAxis ) view.getChart( ).getXAxis( ), 1.0 / 1.1 );

			if ( ! allowYAxisControl( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( allowYAxisControl( ) )
		{
			zoomAxis( ( NumberAxis ) view.getChart( ).getYAxis( ), 1.0 / 1.1 );
		}
	}

	public void zoomOut( )
	{
		if ( allowXAxisControl( ) )
		{
			zoomAxis( ( NumberAxis ) view.getChart( ).getXAxis( ), 1.1 );

			if ( ! allowYAxisControl( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( allowYAxisControl( ) )
		{
			zoomAxis( ( NumberAxis ) view.getChart( ).getYAxis( ), 1.1 );
		}
	}

	public void moveCenter( Point2D moveVector )
	{
		if ( allowXAxisControl( ) )
		{
			moveAxisRelatively( ( NumberAxis ) view.getChart( ).getXAxis( ),
					moveVector.getX( ) / view.getChart( ).getWidth( ) );

			if ( ! allowYAxisControl( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( allowYAxisControl( ) )
		{
			moveAxisRelatively( ( NumberAxis ) view.getChart( ).getYAxis( ),
					moveVector.getY( ) / view.getChart( ).getHeight( ) );
		}
	}


	/**
	 * Automatically ranges Y axis so that all the points in X range lay in viewport.
	 */
	private void autoRangeYAxisInScope( )
	{
		List < XYChart.Series < Number, Number > > data = view.getChart( ).getData( );

		if ( data.isEmpty( ) )
		{
			return;
		}

		NumberAxis yAxis = ( NumberAxis ) view.getChart( ).getYAxis( );
		NumberAxis xAxis = ( NumberAxis ) view.getChart( ).getXAxis( );

		setAxisAutoRanging( yAxis, false );

		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;

		double scopeLeftValue = xAxis.getLowerBound( );
		double scopeRightValue = xAxis.getUpperBound( );


		double scopeHalfWidth = ( scopeRightValue - scopeLeftValue ) / 2.0;
		double scopeXCenter = ( scopeRightValue + scopeLeftValue ) / 2.0;

		int pointInXRangeCount = 0;

		for ( XYChart.Series < Number, Number > series : data )
		{
			if ( series.getData( ).isEmpty( ) )
			{
				return;
			}

			for ( XYChart.Data < Number, Number > dataPoint : series.getData( ) )
			{
				if ( dataPoint.getXValue( ).doubleValue( ) > scopeLeftValue &&
						dataPoint.getXValue( ).doubleValue( ) < scopeRightValue )
				{
					if ( dataPoint.getYValue( ).doubleValue( ) < minValue )
					{
						minValue = dataPoint.getYValue( ).doubleValue( );
					}

					if ( dataPoint.getYValue( ).doubleValue( ) > maxValue )
					{
						maxValue = dataPoint.getYValue( ).doubleValue( );
					}

					pointInXRangeCount += 1;
				}
			}
		}

		if ( pointInXRangeCount < 2 )
		{
			return;
		}

		double newScopeHalfHeight = ( maxValue - minValue ) / 2.0;
		double newScopeYCenter = ( maxValue + minValue ) / 2.0;

		yAxis.setUpperBound( newScopeYCenter + newScopeHalfHeight * 1.1 );
		yAxis.setLowerBound( newScopeYCenter - newScopeHalfHeight * 1.1 );

		normalizeAxis( yAxis );
	}

	private void moveAxisRelatively( NumberAxis axis, double relativeShift )
	{
		setAxisAutoRanging( axis, false );

		double lowerBound = axis.getLowerBound( );
		double upperBound = axis.getUpperBound( );
		double width = upperBound - lowerBound;

		axis.setLowerBound( lowerBound + width * relativeShift );
		axis.setUpperBound( upperBound + width * relativeShift );
	}

	public void autoRangeXAxis( )
	{
		setAxisAutoRanging( ( NumberAxis ) view.getChart( ).getXAxis( ), true );
	}

	public void autoRangeYAxis( )
	{
		setAxisAutoRanging( ( NumberAxis ) view.getChart( ).getYAxis( ), true );
	}

	private void setAxisForceZeroInRange( NumberAxis axis, boolean forceZeroInRange )
	{
		if ( forceZeroInRange )
		{
			axis.setAutoRanging( true );
		}

		axis.setForceZeroInRange( forceZeroInRange );
	}

	private void setAxisAutoRanging( NumberAxis axis, boolean autoRanging )
	{
		axis.setAutoRanging( autoRanging );

		if ( ! autoRanging )
		{
			setAxisForceZeroInRange( axis, false );
		}
	}

	private void zoomAxis( NumberAxis axis, double zoomRate )
	{
		setAxisAutoRanging( axis, false );

		double upperBound = axis.getUpperBound( );
		double lowerBound = axis.getLowerBound( );
		double center = ( upperBound + lowerBound ) / 2.0f;
		double halfWidth = ( upperBound - lowerBound ) / 2.0f;

		axis.setUpperBound( center + halfWidth * zoomRate );
		axis.setLowerBound( center - halfWidth * zoomRate );

		normalizeAxis( axis );
	}

	private void normalizeAxis( NumberAxis axis )
	{
		axis.setTickUnit( ( axis.getUpperBound( ) - axis.getLowerBound( ) ) / 10.0 );
	}

	public void onMouseMoveEvent( MouseEvent mouseEvent )
	{
		if ( isMousePressed )
		{
			Point2D moveVector = new Point2D(
					- mouseEvent.getX( ) + oldMousePos.getX( ),
					mouseEvent.getY( ) - oldMousePos.getY( ) );

			moveCenter( moveVector );
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
