package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nikich59.webstatistics.visio.desktopapp.controller.SeriesListController;
import ru.nikich59.webstatistics.visio.desktopapp.errohandler.ErrorHandler;
import ru.nikich59.webstatistics.visio.desktopapp.view.dialog.AddSeriesDialog;
import ru.nikich59.webstatistics.visio.model.VisioModel;
import ru.nikich59.webstatistics.visio.model.series.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nikich59.webstatistics.visio.desktopapp.FXMLLoader.loadFxmlInto;

/**
 * Created by Nikita on 29.12.2017.
 */
public class SeriesView extends BasicVisioView
{
	@FXML
	private VBox seriesViewBox;


	private SeriesListController controller;
	private List < VisioModel.StatisticsSeries > statisticsSeries;

	public SeriesView( )
			throws IOException
	{
		loadFxmlInto( getClass( ).getResource( "series_view.fxml" ), this );
	}

	public void update( List < VisioModel.StatisticsSeries > statisticsSeries )
	{
		this.statisticsSeries = statisticsSeries;

		seriesViewBox.getChildren( ).clear( );
		for ( VisioModel.StatisticsSeries series : statisticsSeries )
		{
			try
			{
				seriesViewBox.getChildren( ).add( new SeriesListItem( series, controller ) );
			}
			catch ( Exception e )
			{
				handleException( e );
			}
		}
	}

	public void setController( SeriesListController controller )
	{
		this.controller = controller;
	}

	@FXML
	public void addSeriesButtonClicked( )
	{
		List < Series < Number, Number > > seriesList = new ArrayList <>( );

		for ( VisioModel.StatisticsSeries statisticsSeries : statisticsSeries )
		{
			seriesList.add( statisticsSeries.series );
		}

		AddSeriesDialog addSeriesDialog = new AddSeriesDialog( seriesList );

		Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

		result.ifPresent( ( series ) ->
				{
					VisioModel.StatisticsSeries statisticsSeries = new VisioModel.StatisticsSeries( );
					statisticsSeries.series = series;
					controller.addSeries( statisticsSeries );
				}
		);
	}
}











