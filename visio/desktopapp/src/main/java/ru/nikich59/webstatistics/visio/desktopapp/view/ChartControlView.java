package ru.nikich59.webstatistics.visio.desktopapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import ru.nikich59.webstatistics.visio.desktopapp.controller.ChartControlViewController;
import ru.nikich59.webstatistics.visio.desktopcore.FXMLLoader;
import ru.nikich59.webstatistics.visio.desktopcore.view.View;

import java.io.IOException;

/**
 * Created by Nikita on 08.01.2018.
 */
public class ChartControlView extends View< ChartControlViewController >
{
	@FXML
	private CheckBox autoRangeYAxisCheckBox;


	public ChartControlView( )
			throws IOException
	{
		FXMLLoader.loadFxmlInto( getClass( ).getResource( "chart_control_view.fxml" ), this );
	}

	@Override
	public void setController( ChartControlViewController controller )
	{
		super.setController( controller );

		initializeUI( );
	}

	private void initializeUI( )
	{
		autoRangeYAxisCheckBox.setSelected( ! getController( ).allowYAxisZoom( ) );
	}


	@FXML
	private void autoRange( )
	{
		getController( ).autoRangeChart( );
	}

	@FXML
	private void forceYAxisZeroInRange( )
	{
		getController( ).forceYZeroInRange( );
	}

	@FXML
	private void onAutoRangeYAxisCheckBoxAction( )
	{
		getController( ).setAllowYAxisControl( ! autoRangeYAxisCheckBox.isSelected( ) );
	}
}
