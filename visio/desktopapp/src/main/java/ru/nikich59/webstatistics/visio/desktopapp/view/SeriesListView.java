package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;
import ru.nikich59.webstatistics.visio.visiocore.model.Model;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 29.12.2017.
 */
public class SeriesListView extends View< SeriesListViewController >
{
	@FXML
	private VBox seriesViewBox;


	private List < Model.StatisticsSeries > statisticsSeries;

	public SeriesListView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "series_list_view.fxml" ), this );
	}

	public void update( List < Model.StatisticsSeries > statisticsSeries )
	{
		this.statisticsSeries = statisticsSeries;

		seriesViewBox.getChildren( ).clear( );
		for ( Model.StatisticsSeries series : statisticsSeries )
		{
			try
			{
				seriesViewBox.getChildren( ).add( new SeriesListItem( series, getController( ) ) );
			}
			catch ( Exception e )
			{
				getController( ).handleException( e );
			}
		}
	}

	@FXML
	public void addSeriesButtonClicked( )
	{
		getController( ).onAddSeriesEvent( );
	}

	@FXML
	public void clearSeriesButtonClicked( )
	{
		getController( ).onClearSeriesEvent( );
	}
}











