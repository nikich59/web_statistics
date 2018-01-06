package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import ru.nikich59.webstatistics.visio.desktopapp.controller.VisioChartController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import static ru.nikich59.webstatistics.visio.desktopapp.FXMLLoader.loadFxmlInto;

/**
 * Created by Nikita on 30.12.2017.
 */


public class VisioChartView extends BasicVisioView
{
	public void setController( VisioChartController controller )
	{
		this.controller = controller;
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


	@FXML
	private StackPane chartStackPane;
/*
	private Line verticalLine = new Line( );
	private Line horizontalLine = new Line( );
*/
	private Rectangle rectangle = new Rectangle( 0, 0 );


	private XYChart < Number, Number > chart;
	private VisioChartController controller;

	private boolean doAllowXAxisZoom = true;
	private boolean doAllowYAxisZoom = false;


	public VisioChartView( )
			throws IOException
	{
		loadFxmlInto( getClass( ).getResource( "visio_chart_view.fxml" ), this );

		initializeUI( );
	}

	private void initializeUI( )
	{
		setChartingMode( ChartingMode.LINE_CHART );

		setOnScroll( ( event ) ->
				controller.onScrollEvent( event )
		);

		chart.setOnMousePressed( ( event ) ->
				controller.onMousePressEvent( event )
		);

		chart.setOnMouseReleased( ( event ) ->
				controller.onMouseReleaseEvent( event )
		);

		chart.setOnMouseDragged( ( event ) ->
				controller.onMouseMoveEvent( event )
		);

		chart.setOnMouseMoved( ( event ) ->
				{
					double x = event.getX( );
//					System.out.println( x );

					Line verticalLine = new Line( x, x, x + 100, x - 100 );
					Line horizontalLine = new Line( x + 200, x + 200, x - 100, x + 100 );
/*
					verticalLine.setStartX( x );
					verticalLine.setStartY( x );

					verticalLine.setEndX( x + 100 );
					verticalLine.setEndY( x + 100 );
*/
/*
					chartStackPane.requestLayout( );
					requestLayout( );

					chartStackPane.requestLayout( );
					requestLayout( );
					chartStackPane.getChildren( ).add( rectangle );
					chartStackPane.requestLayout( );
					requestLayout( );
					chartStackPane.getChildren( ).remove( rectangle );
					chartStackPane.requestLayout( );
					requestLayout( );
					chartStackPane.getChildren( ).setAll( chart, verticalLine, horizontalLine );

					requestLayout( );
					chartStackPane.requestLayout( );

					System.out.println( verticalLine );*/
				}
		);
	}

	public void setChartingMode( ChartingMode chartingMode )
	{
		ObservableList < XYChart.Series < Number, Number > > oldData = FXCollections.observableArrayList( );
		NumberAxis oldXAxis = new NumberAxis( );
		NumberAxis oldYAxis = new NumberAxis( );
		if ( chart != null )
		{
			oldData = chart.getData( );
			oldXAxis = ( NumberAxis ) chart.getXAxis( );
			oldYAxis = ( NumberAxis ) chart.getYAxis( );
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
		}

		chart.setData( oldData );

		chartStackPane.getChildren( ).setAll( chart/*, horizontalLine, verticalLine*/ );
	}

	public NumberAxis getChartXAxis( )
	{
		return ( NumberAxis ) chart.getXAxis( );
	}

	public NumberAxis getChartYAxis( )
	{
		return ( NumberAxis ) chart.getYAxis( );
	}

	public ObservableList < XYChart.Series < Number, Number > > getChartData( )
	{
		return chart.getData( );
	}

	public void setXAxisForceZeroInRange( boolean forceZeroInRange )
	{
		setAxisForceZeroInRange( ( NumberAxis ) chart.getXAxis( ), forceZeroInRange );
	}

	public void setYAxisForceZeroInRange( boolean forceZeroInRange )
	{
		setAxisForceZeroInRange( ( NumberAxis ) chart.getYAxis( ), forceZeroInRange );
	}

	public void zoomIn( )
	{
		if ( doAllowXAxisZoom )
		{
			zoomAxis( ( NumberAxis ) chart.getXAxis( ), 1.0 / 1.1 );

			if ( ! doAllowYAxisZoom )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( doAllowYAxisZoom )
		{
			zoomAxis( ( NumberAxis ) chart.getYAxis( ), 1.0 / 1.1 );
		}
	}

	public void zoomOut( )
	{
		if ( doAllowXAxisZoom )
		{
			zoomAxis( ( NumberAxis ) chart.getXAxis( ), 1.1 );

			if ( ! doAllowYAxisZoom )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( doAllowYAxisZoom )
		{
			zoomAxis( ( NumberAxis ) chart.getYAxis( ), 1.1 );
		}
	}

	public void moveCenter( Point2D moveVector )
	{
		if ( doAllowXAxisZoom )
		{
			moveAxisRelatively( ( NumberAxis ) chart.getXAxis( ), moveVector.getX( ) / chart.getWidth( ) );

			if ( ! doAllowYAxisZoom )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( doAllowYAxisZoom )
		{
			moveAxisRelatively( ( NumberAxis ) chart.getYAxis( ), moveVector.getY( ) / chart.getHeight( ) );
		}
	}

	private void autoRangeYAxisInScope( )
	{
		List < XYChart.Series < Number, Number > > data = chart.getData( );

		if ( data.isEmpty( ) )
		{
			return;
		}

		NumberAxis yAxis = ( NumberAxis ) chart.getYAxis( );
		NumberAxis xAxis = ( NumberAxis ) chart.getXAxis( );
		setAxisAutoRanging( yAxis, false );

		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;

		double scopeLeftValue = xAxis.getLowerBound( );
		double scopeRightValue = xAxis.getUpperBound( );


		double scopeHalfWidth = ( scopeRightValue - scopeLeftValue ) / 2.0;
		double scopeXCenter = ( scopeRightValue + scopeLeftValue ) / 2.0;

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
				}
			}
		}
/*
		boolean forceZeroInRange = false;

		if ( yAxis.forceZeroInRangeProperty( ).get( ) )
		{
			if ( minValue < 0.0 && maxValue < 0.0 )
			{
				maxValue = 0.0;

				forceZeroInRange = true;
			}
			else if ( minValue > 0.0 && maxValue > 0.0 )
			{
				minValue = 0.0;

				forceZeroInRange = true;
			}
		}
*/
		double newScopeHalfHeight = ( maxValue - minValue ) / 2.0;
		double newScopeYCenter = ( maxValue + minValue ) / 2.0;

		yAxis.setUpperBound( newScopeYCenter + newScopeHalfHeight * 1.1 );
		yAxis.setLowerBound( newScopeYCenter - newScopeHalfHeight * 1.1 );

		normalizeAxis( yAxis );
/*
		if ( forceZeroInRange )
		{
			yAxis.setForceZeroInRange( true );
		}*/
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
		setAxisAutoRanging( ( NumberAxis ) chart.getXAxis( ), true );
	}

	public void autoRangeYAxis( )
	{
		setAxisAutoRanging( ( NumberAxis ) chart.getYAxis( ), true );
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
}














