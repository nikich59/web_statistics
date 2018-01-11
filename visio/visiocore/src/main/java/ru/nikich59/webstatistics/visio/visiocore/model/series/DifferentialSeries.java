package ru.nikich59.webstatistics.visio.visiocore.model.series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class DifferentialSeries< X > extends Series< X, Number >
{
	private List< Number > yAxis = new ArrayList<>( );

	public void setyAxis( List< Number > yAxis )
	{
		this.yAxis = yAxis;
	}

	@Override
	public List < Number > getData( )
	{
		List < Number > data = new ArrayList <>( );

		data.add( 0.0f );
		for ( int i = 1; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			data.add( yAxis.get( i ).floatValue( ) - yAxis.get( i - 1 ).floatValue( ) );
		}

		return data;
	}
}
