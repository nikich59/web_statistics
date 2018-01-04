package ru.nikich59.webstatistics.visio.model.bridge;

import javafx.geometry.Point2D;
import ru.nikich59.webstatistics.visio.model.VisioModel;
import ru.nikich59.webstatistics.visio.view.VisioView;

/**
 * Created by Nikita on 30.12.2017.
 */

public class ModelToDesktopAppBridge extends ModelToAppBridge
{
	private VisioView view;
	private VisioModel model;

	public ModelToDesktopAppBridge( VisioView view, VisioModel model )
	{
		this.view = view;
		this.model = model;
	}

	@Override
	public void updateView( )
	{
		view.setData( model.getData( ) );
	}

	@Override
	public void moveChart( Point2D moveVector )
	{
		view.moveChart( moveVector );
	}

	@Override
	public void zoomChartIn( )
	{
		view.zoomChartIn( );
	}

	@Override
	public void zoomChartOut( )
	{
		view.zoomChartOut( );
	}
}
