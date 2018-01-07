package ru.nikich59.webstatistics.visiocore.model.series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class RateSeries < X > extends Series < X, Number >
{
	private List < Number > yAxisAbove = new ArrayList <>( );
	private List < Number > yAxisBelow = new ArrayList <>( );

	public void setyAxisAbove( List < Number > yAxisAbove )
	{
		this.yAxisAbove = yAxisAbove;
	}

	public void setyAxisBelow( List < Number > yAxisBelow )
	{
		this.yAxisBelow = yAxisBelow;
	}

	@Override
	public List < Number > getData( )
	{
		List < Number > data = new ArrayList <>( );

		for ( int i = 0; i < Math.min( xAxis.size( ), Math.min( yAxisAbove.size( ), yAxisBelow.size( ) ) ); i += 1 )
		{
			if ( Math.abs( yAxisBelow.get( i ).floatValue( ) ) > 1.0e-6 )
			{
				data.add( yAxisAbove.get( i ).floatValue( ) / yAxisBelow.get( i ).floatValue( ) );
			}
			else
			{
				data.add( 0.0f );
			}
		}

		return data;
	}
}
