package ru.nikich59.webstatistics.visiocore.model.series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 26.12.2017.
 */
public class MultipliedSeries< X > extends Series< X, Number >
{
	private List< Number > yAxis = new ArrayList<>( );
	private float ratio = 1.0f;

	public void setyAxis( List< Number > yAxis )
	{
		this.yAxis = yAxis;
	}

	public void setRatio( float ratio )
	{
		this.ratio = ratio;
	}

	@Override
	public List < Number > getData( )
	{
		List < Number > data = new ArrayList <>( );

		for ( int i = 0; i < Math.min( xAxis.size( ), yAxis.size( ) ); i += 1 )
		{
			data.add( yAxis.get( i ).floatValue( ) * ratio );
		}

		return data;
	}
}
