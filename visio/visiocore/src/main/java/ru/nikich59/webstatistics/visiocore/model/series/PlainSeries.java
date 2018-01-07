package ru.nikich59.webstatistics.visiocore.model.series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class PlainSeries< X, Y > extends Series< X, Y >
{
	private List< Y > yAxis = new ArrayList<>( );

	public void setyAxis( List< Y > yAxis )
	{
		this.yAxis = yAxis;
	}

	@Override
	public List < Y > getData( )
	{
		List < Y > data = new ArrayList <>( );

		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			data.add( yAxis.get( i ) );
		}

		return data;
	}
}
