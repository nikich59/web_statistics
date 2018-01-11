package ru.nikich59.webstatistics.statister.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import ru.nikich59.webstatistics.statister.SiteStatisticsAcquirer;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterItemViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 10.01.2018.
 */
public class StatisterItemView extends View < StatisterItemViewController >
{
	@FXML
	private Label urlLabel;

	@FXML
	private Label periodLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Label errorLabel;


	private SiteStatisticsAcquirer statisticsAcquirer;

	public StatisterItemView( SiteStatisticsAcquirer statisticsAcquirer )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "statister_item_view.fxml" ), this );

		this.statisticsAcquirer = statisticsAcquirer;

		initializeUI( );
	}

	private void initializeUI( )
	{
		urlLabel.setText( statisticsAcquirer.getUrl( ) );
		urlLabel.setTextFill( getColorForSleuth( statisticsAcquirer ) );

		if ( statisticsAcquirer.getLastException( ) != null )
		{
			errorLabel.setText( statisticsAcquirer.getLastException( ).getMessage( ) );
			errorLabel.setVisible( true );
		}
		else
		{
			errorLabel.setVisible( false );
		}

		descriptionLabel.setText( statisticsAcquirer.getStatisticsHeader( ).getHeadline( ) );

		periodLabel.setText( String.valueOf(
				( ( float ) statisticsAcquirer.getAcquiringPeriod( ) ) / 1000.0f ) + " sec" );
	}

	private Color getColorForSleuth( SiteStatisticsAcquirer statisticsAcquirer )
	{
		if ( statisticsAcquirer.getLastException( ) == null )
		{
			return Color.GREEN;
		}
		else
		{
			return Color.RED;
		}
	}
}
