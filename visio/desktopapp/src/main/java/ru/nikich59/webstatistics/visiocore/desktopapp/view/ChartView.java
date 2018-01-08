package ru.nikich59.webstatistics.visiocore.desktopapp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import ru.nikich59.webstatistics.visiocore.desktopapp.controller.VisioChartController;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import ru.nikich59.webstatistics.visiocore.desktopapp.FXMLLoader;

/**
 * Created by Nikita on 30.12.2017.
 */


public class ChartView extends BasicVisioView
{
	public void setController( VisioChartController controller )
	{
		this.controller = controller;
	}


	@FXML
	private StackPane chartStackPane;


	private XYChart < Number, Number > chart;

	public VisioChartController getController( )
	{
		return controller;
	}

	private VisioChartController controller;


	public ChartView( )
			throws IOException
	{
		javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
				getClass( ).getResource( "visio_chart_view.fxml" ) );
		fxmlLoader.setRoot( this );
		fxmlLoader.setController( this );

		fxmlLoader.load( );
		/*
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_chart_view.fxml" ), this );*/

		initializeUI( );
	}

	private void initializeUI( )
	{
		setChartingMode( VisioChartController.ChartingMode.LINE_CHART );

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
	}

	public void setData( List < XYChart.Series < Number, Number > > seriesList )
	{
		chart.getData( ).setAll( seriesList );
	}

	public void setChartingMode( VisioChartController.ChartingMode chartingMode )
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
		if ( controller.allowXAxisZoom( ) )
		{
			zoomAxis( ( NumberAxis ) chart.getXAxis( ), 1.0 / 1.1 );

			if ( ! controller.allowYAxisZoom( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( controller.allowYAxisZoom( ) )
		{
			zoomAxis( ( NumberAxis ) chart.getYAxis( ), 1.0 / 1.1 );
		}
	}

	public void zoomOut( )
	{
		if ( controller.allowXAxisZoom( ) )
		{
			zoomAxis( ( NumberAxis ) chart.getXAxis( ), 1.1 );

			if ( ! controller.allowYAxisZoom( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( controller.allowYAxisZoom( ) )
		{
			zoomAxis( ( NumberAxis ) chart.getYAxis( ), 1.1 );
		}
	}

	public void moveCenter( Point2D moveVector )
	{
		if ( controller.allowXAxisZoom( ) )
		{
			moveAxisRelatively( ( NumberAxis ) chart.getXAxis( ), moveVector.getX( ) / chart.getWidth( ) );

			if ( ! controller.allowYAxisZoom( ) )
			{
				autoRangeYAxisInScope( );
			}
		}

		if ( controller.allowYAxisZoom( ) )
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














