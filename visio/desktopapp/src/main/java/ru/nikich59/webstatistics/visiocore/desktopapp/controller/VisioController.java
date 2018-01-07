package ru.nikich59.webstatistics.visiocore.desktopapp.controller;

//import com.sun.istack.internal.NotNull;
//import com.sun.istack.internal.NotNull;
import javafx.geometry.Point2D;
import javafx.scene.input.ScrollEvent;
import ru.nikich59.webstatistics.visiocore.model.Model;
import ru.nikich59.webstatistics.visiocore.adapter.ApplicationAdapter;
import stats.Statistics;
import stats.controller.StatsController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Nikita on 30.12.2017.
 */
public class VisioController
{
	private Model model;
	private List < StatsController > statsControllers = new ArrayList <>( );
	private ApplicationAdapter modelToAppBridge;
	private Consumer < ScrollEvent > onChartScrollEventHandler = ( event ) ->
	{
	}; // Doing nothing by default.
	private Consumer < Point2D > onMouseMoveEventHandler = ( event ) ->
	{
	};
	private String statisticsDirectory = "";

	private Point2D oldMousePosition = new Point2D( 0.0, 0.0 );


	public void setModel( Model visioModel )
	{
		this.model = visioModel;
	}

	public void clearStatsControllers( )
	{
		statsControllers.clear( );
	}

	public void addStatsController( StatsController statsController )
	{
		statsControllers.add( statsController );

		updateModel( );
	}

	public void setModelToAppBridge( ApplicationAdapter modelToAppBridge )
	{
		this.modelToAppBridge = modelToAppBridge;
	}

	public VisioController( Model model, ApplicationAdapter modelToAppBridge )
	{
		setModel( model );
		setModelToAppBridge( modelToAppBridge );

		setOnChartScrollEventHandler( ( event ) ->
				{
					if ( event.getDeltaY( ) > 0.0 )
					{
						modelToAppBridge.zoomChartIn( );

//						modelToAppBridge.updateView( );
					}
					else
					{
						modelToAppBridge.zoomChartOut( );

//						modelToAppBridge.updateView( );
					}
				}
		);

		setOnChartMouseMoveEventHandler( ( event ) ->
				modelToAppBridge.moveChart( event )
		);


	}

	public void setSeriesEnabled( String seriesId, boolean enabled )
	{
		model.setSeriesEnabled( seriesId, enabled );

		modelToAppBridge.updateView( );
	}

	public void updateModel( )
	{
		model.clearSeries( );

		for ( StatsController statsController : statsControllers )
		{
			try
			{
				statsController.loadStatistics( );
				Statistics statistics = statsController.getStatistics( );

				model.addStatistics( statistics );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
		}

		modelToAppBridge.updateView( );
	}

	public void setOnChartScrollEventHandler( Consumer < ScrollEvent > onChartScrollEventHandler )
	{
		this.onChartScrollEventHandler = onChartScrollEventHandler;
	}

	void onChartScrollEvent( ScrollEvent scrollEvent )
	{
		onChartScrollEventHandler.accept( scrollEvent );
	}

	public void setOnChartMouseMoveEventHandler( Consumer < Point2D > onMouseMoveEventHandler )
	{
		this.onMouseMoveEventHandler = onMouseMoveEventHandler;
	}

	void onChartMouseMoveEvent( Point2D moveVector )
	{
		onMouseMoveEventHandler.accept( moveVector );
	}

	public void setStatisticsDirectory( String statisticsDirectory )
	{
		this.statisticsDirectory = statisticsDirectory;
	}

	public String getStatisticsDirectory( )
	{
		return statisticsDirectory;
	}

	public void addSeries( Model.StatisticsSeries series )
	{
		model.addSeries( series );

		modelToAppBridge.updateView( );
	}
}
