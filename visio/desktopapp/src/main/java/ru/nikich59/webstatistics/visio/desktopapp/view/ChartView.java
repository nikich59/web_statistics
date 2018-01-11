package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visio.desktopapp.controller.ChartViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 30.12.2017.
 */


public class ChartView extends View< ChartViewController >
{
	@FXML
	private StackPane chartStackPane;


	public XYChart < Number, Number > getChart( )
	{
		return chart;
	}

	private XYChart < Number, Number > chart;

	@Override
	public void setController( ChartViewController controller )
	{
		super.setController( controller );

		controller.setChartingMode( ChartViewController.ChartingMode.LINE_CHART );

		setMouseEventHandlers( );
	}

	private void setMouseEventHandlers( )
	{
		chart.setOnScroll( getController( )::onScrollEvent );

		chart.setOnMousePressed( getController( )::onMousePressEvent );

		chart.setOnMouseReleased( getController( )::onMouseReleaseEvent );

		chart.setOnMouseDragged( getController( )::onMouseMoveEvent );
	}


	public ChartView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_chart_view.fxml" ), this );
	}

	public void setChart( XYChart < Number, Number > chart )
	{
		this.chart = chart;

		chartStackPane.getChildren( ).setAll( chart );
	}
}














