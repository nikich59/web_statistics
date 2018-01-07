package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Created by Nikita on 31.12.2017.
 */
public class VisioChartController
{
	private VisioController visioController;

	private Point2D oldMousePos = new Point2D( 0.0, 0.0 );
	private boolean isMousePressed = false;

	private MouseButton moveMouseButton = MouseButton.PRIMARY;

	public VisioChartController( VisioController visioController )
	{
		this.visioController = visioController;
	}

	public void onScrollEvent( ScrollEvent scrollEvent )
	{
		visioController.onChartScrollEvent( scrollEvent );
	}

	public void onMouseMoveEvent( MouseEvent mouseEvent )
	{
		if ( isMousePressed )
		{
			Point2D moveVector = new Point2D(
					- mouseEvent.getX( ) + oldMousePos.getX( ),
					mouseEvent.getY( ) - oldMousePos.getY( ) );

			visioController.onChartMouseMoveEvent( moveVector );
		}

		oldMousePos = new Point2D( mouseEvent.getX( ), mouseEvent.getY( ) );
	}

	public void onMousePressEvent( MouseEvent mouseEvent )
	{
		if ( mouseEvent.getButton( ) != moveMouseButton )
		{
			return;
		}

		isMousePressed = true;

		oldMousePos = new Point2D( mouseEvent.getX( ), mouseEvent.getY( ) );
	}

	public void onMouseReleaseEvent( MouseEvent mouseEvent )
	{
		if ( mouseEvent.getButton( ) != moveMouseButton )
		{
			return;
		}

		isMousePressed = false;
	}
}
