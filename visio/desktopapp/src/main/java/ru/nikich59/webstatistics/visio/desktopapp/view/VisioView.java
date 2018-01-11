package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import ru.nikich59.webstatistics.visio.desktopapp.controller.*;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the top view of desktop application.
 */

public class VisioView extends View < VisioViewController >
{
	/**
	 * This view contains list of all series that can be exhibited.
	 */
	@FXML
	private SeriesListView seriesListView;


	/**
	 * This is the view that contains the chart exhibiting all the selected series.
	 */
	@FXML
	private ChartView chartView;


	@FXML
	private TopMenuView menuView;

	@FXML
	private ChartControlView chartControlView;


	/**
	 * These are series that can be exhibited.
	 */
	private List < Model.StatisticsSeries > statisticsSeries = new ArrayList <>( );

	public VisioView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "visio_view.fxml" ), this );
	}

	@Override
	public void setController( VisioViewController controller )
	{
		super.setController( controller );

		menuView.setController( new TopMenuViewController( menuView, controller ) );

		seriesListView.setController( new SeriesListViewController( seriesListView, controller ) );

		ChartViewController chartViewController = new ChartViewController( chartView, controller );

		chartView.setController( chartViewController );

		chartControlView.setController( new ChartControlViewController( chartControlView, chartViewController ) );
	}
}









