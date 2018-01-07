package ru.nikich59.webstatistics.visiocore.desktopapp.model.bridge;

import javafx.geometry.Point2D;
import ru.nikich59.webstatistics.visiocore.desktopapp.view.VisioView;
import ru.nikich59.webstatistics.visiocore.model.Model;
import ru.nikich59.webstatistics.visiocore.adapter.ApplicationAdapter;

/**
 * Created by Nikita on 30.12.2017.
 */

public class ModelToDesktopAppBridge extends ApplicationAdapter
{
	private VisioView view;
	private Model model;

	public ModelToDesktopAppBridge( VisioView view, Model model )
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
