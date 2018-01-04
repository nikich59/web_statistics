package ru.nikich59.webstatistics.visio.model.bridge;

import javafx.geometry.Point2D;

/**
 * Created by Nikita on 30.12.2017.
 */
public abstract class ModelToAppBridge
{
	public abstract void updateView( );

	public abstract void zoomChartIn( );

	public abstract void zoomChartOut( );

	public abstract void moveChart( Point2D moveVector );
}
