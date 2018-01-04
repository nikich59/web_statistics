package ru.nikich59.webstatistics.visio.view;

import com.sun.istack.internal.NotNull;
import javafx.scene.chart.NumberAxis;
import org.apache.commons.lang3.Range;

/**
 * Created by Nikita on 31.12.2017.
 */
public class ViewPort
{
	public void setxAxisMode( AxisMode xAxisMode )
	{
		this.xAxisMode = xAxisMode;
	}

	public void setyAxisMode( AxisMode yAxisMode )
	{
		this.yAxisMode = yAxisMode;
	}

	public static abstract class AxisMode
	{
		public abstract void prepareAxis( NumberAxis axis );

		public abstract void zoomIn( );

		public abstract void zoomOut( );

		public abstract void setZoomRate( float zoomRate );
	}

	public static class AxisModeAutoRange extends AxisMode
	{
		private boolean forceZeroInRange = false;

		public void prepareAxis( NumberAxis axis )
		{
			axis.setForceZeroInRange( forceZeroInRange );
			axis.setAutoRanging( true );
		}

		public void setForceZeroInRange( boolean forceZeroInRange )
		{
			this.forceZeroInRange = forceZeroInRange;
		}

		public void zoomIn( )
		{

		}

		public void zoomOut( )
		{

		}

		public void setZoomRate( float zoomRate )
		{

		}
	}

	public static class AxisModeFixedRange extends AxisMode
	{
		private Range < Float > scopedRange = Range.between( 0.0f, 1.0f );

		private float zoomRate = 1.1f;

		public void prepareAxis( NumberAxis axis )
		{
			axis.setAutoRanging( false );
			axis.setLowerBound( scopedRange.getMinimum( ) );
			axis.setUpperBound( scopedRange.getMaximum( ) );
		}

		public void setScopedRange( @NotNull Range < Float > scopedRange )
		{
			this.scopedRange = scopedRange;
		}

		public void zoomIn( )
		{
			float scopedRangeWidth = scopedRange.getMaximum( ) - scopedRange.getMinimum( );
			float scopedRangeCenter = ( scopedRange.getMinimum( ) + scopedRange.getMaximum( ) ) / 2.0f;

			scopedRange = Range.between(
					scopedRangeCenter - scopedRangeWidth / 2.0f / zoomRate,
					scopedRangeCenter + scopedRangeWidth / 2.0f / zoomRate );
		}

		public void zoomOut( )
		{
			float scopedRangeWidth = scopedRange.getMaximum( ) - scopedRange.getMinimum( );
			float scopedRangeCenter = ( scopedRange.getMinimum( ) + scopedRange.getMaximum( ) ) / 2.0f;

			scopedRange = Range.between(
					scopedRangeCenter - scopedRangeWidth / 2.0f * zoomRate,
					scopedRangeCenter + scopedRangeWidth / 2.0f * zoomRate );
		}

		public void setZoomRate( float zoomRate )
		{
			if ( zoomRate < 1.0f )
			{
				throw new UnsupportedOperationException( "Zoom rate is contracted to be not less than 1.0" );
			}

			this.zoomRate = zoomRate;
		}
	}

	private AxisMode xAxisMode = new AxisModeAutoRange( );
	private AxisMode yAxisMode = new AxisModeAutoRange( );

	public void prepareXAxis( NumberAxis xAxis )
	{
		xAxisMode.prepareAxis( xAxis );
	}

	public void prepareYAxis( NumberAxis yAxis )
	{
		yAxisMode.prepareAxis( yAxis );
	}

	public void setXZoomRate( float zoomRate )
	{
		xAxisMode.setZoomRate( zoomRate );
	}

	public void setYZoomRate( float zoomRate )
	{
		yAxisMode.setZoomRate( zoomRate );
	}

	public void zoomXAxisIn( )
	{
		xAxisMode.zoomIn( );
	}

	public void zoomXAxisOut( )
	{
		xAxisMode.zoomOut( );
	}

	public void zoomYAxisIn( )
	{
		yAxisMode.zoomIn( );
	}

	public void zoomYAxisOut( )
	{
		yAxisMode.zoomOut( );
	}
}










