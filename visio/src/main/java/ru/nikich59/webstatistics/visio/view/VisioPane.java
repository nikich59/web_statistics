package ru.nikich59.webstatistics.visio.view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.StringConverter;
import ru.nikich59.webstatistics.visio.model.series.Series;
import ru.nikich59.webstatistics.visio.series.gui.AddSeriesDialog;
import stats.Statistics;
import stats.StatsFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nikita on 25.12.2017.
 */
public class VisioPane extends StackPane
{
	private class ItemSelectList extends StackPane
	{
		private class Item extends StackPane
		{
			private HBox hBox;
			private CheckBox enableCheckBox;
			private Label nameLabel;
			private Button editButton;
			private Series < Number, Number > series;

			public Item( Series < Number, Number > series )
			{
				this.series = series;

				Item.this.update( );
			}

			private void update( )
			{
				nameLabel = new Label( series.getName( ) );

				enableCheckBox = new CheckBox( "enable" );
				/*
				enableCheckBox.setSelected( series.isEnabled( ) );
				enableCheckBox.setOnAction( ( e ) ->
						{
							series.setEnabled( enableCheckBox.isSelected( ) );
							VisioPane.this.update( );
						}
				);*/

				editButton = new Button( "edit" );

				hBox = new HBox( nameLabel, editButton, enableCheckBox );
				hBox.setAlignment( Pos.CENTER );
				hBox.setSpacing( 10.0 );

				getChildren( ).setAll( hBox );
			}
		}

		private ListView < Item > selectItemList = new ListView <>( );
		private VBox vBox;
		private Button addSeriesButton;

		public ItemSelectList( )
		{
			update( );
		}

		public void update( )
		{
			selectItemList = new ListView <>( );
			selectItemList.setOrientation( Orientation.VERTICAL );
			selectItemList.setPrefHeight( 1.0e6 );

			for ( Series < Number, Number > series : exposedSeries )
			{
				selectItemList.getItems( ).add( new Item( series ) );
			}

			addSeriesButton = new Button( "Add series" );
			addSeriesButton.setOnMouseClicked( ( e ) ->
					{
						AddSeriesDialog addSeriesDialog = new AddSeriesDialog( exposedSeries );

						Optional < Series < Number, Number > > result = addSeriesDialog.showAndWait( );

						if ( result.isPresent( ) )
						{
							VisioPane.this.addExposedSeries( result.get( ) );
						}

						VisioPane.this.update( );
					}
			);

			vBox = new VBox( addSeriesButton, selectItemList );
			vBox.setAlignment( Pos.TOP_CENTER );
			vBox.setSpacing( 10.0 );

			getChildren( ).setAll( vBox );
			ItemSelectList.this.setPadding( new Insets( 10.0 ) );

			setMinWidth( 300.0f );
		}
	}



	private class SettingsView extends VBox
	{
		private CheckBox isDayMode;
		private CheckBox isVerticalZeroIncluded;

		public SettingsView( )
		{
			isDayMode = new CheckBox( "day mode" );
			isDayMode.setSelected( VisioPane.this.isDaysModeEnabled );
			isDayMode.setOnAction( ( e ) ->
					{
						VisioPane.this.isDaysModeEnabled = SettingsView.this.isDayMode.isSelected( );
						VisioPane.this.updateChart( );
					}
			);

			isVerticalZeroIncluded = new CheckBox( "include vertical zero" );
			isVerticalZeroIncluded.setSelected( VisioPane.this.isVerticalZeroIncluded );
			isVerticalZeroIncluded.setOnAction( ( e ) ->
					{
						VisioPane.this.isVerticalZeroIncluded = SettingsView.this.isVerticalZeroIncluded.isSelected( );
						VisioPane.this.updateChart( );
					}
			);

			getChildren( ).setAll( isDayMode, isVerticalZeroIncluded );

			setMinWidth( 150.0f );
		}
	}

	HBox hBox;
	LineChart < Number, Number > lineChart;
	StackPane lineChartStackPane;
	ItemSelectList itemSelectList;
	private SettingsView settingsView;

	private List < Statistics.DataPoint > dataPoints = new ArrayList <>( );
	private List < String > dataValueNames = new ArrayList <>( );

	private List < Series < Number, Number > > exposedSeries = new ArrayList <>( );

	String fileName = "";

	private String chartTitle = "";

	private boolean isDaysModeEnabled = false;
	private boolean isVerticalZeroIncluded = true;

//	List< String > values = new ArrayList<>( );

	public VisioPane( )
	{
//		initializeUi( );
	}

	public void setFileName( String fileName )
	{
		this.fileName = fileName;

		initializeUi( );
	}

	public void update( )
	{
		itemSelectList.update( );

		updateChart( );
	}

	public void addExposedSeries( Series < Number, Number > series )
	{
		exposedSeries.add( series );
	}

	private void initializeUi( )
	{
		hBox = new HBox( );

		exposedSeries = new ArrayList <>( );

		readData( );


		List < String > xAxisLabels = getXAxisLabels( );
		List < Number > values = new ArrayList <>( );
		List < Number > valuesFloat = new ArrayList <>( );

		List < Number > xAxis = new ArrayList <>( );
/*
		for ( int i = 0; i < dataPoints.size( ); i += 1 )
		{
			values.add( dataPoints.get( i ).data[ 0 ] );
			valuesFloat.add( dataPoints.get( i ).data[ 1 ] );
			xAxis.add( dataPoints.get( i ).dateTime.toEpochSecond( ) -
					dataPoints.get( 0 ).dateTime.toEpochSecond( ) );
		}

		for ( int i = 0; i < dataPoints.get( 0 ).data.length; i += 1 )
		{
			List < Number > data = new ArrayList <>( );

			for ( int j = 0; j < dataPoints.size( ); j += 1 )
			{
				data.add( dataPoints.get( j ).data[ i ] );
			}

			PlainSeries < Number, Number > series = new PlainSeries <>( );
			series.setxAxis( xAxis );
			series.setyAxis( data );

			series.setName( dataValueNames.get( i + 1 ) );
			series.setEnabled( false );

			exposedSeries.add( series );
		}
*/
		int base = 10;


		itemSelectList = new ItemSelectList( );
		lineChartStackPane = new StackPane( );

		settingsView = new SettingsView( );

		hBox.getChildren( ).setAll( itemSelectList, lineChartStackPane, settingsView );
//		itemSelectList.setMinWidth( 200.0f );

		getChildren( ).setAll( hBox );

		updateChart( );
	}

	private void readData( )
	{
		File file = new File( fileName );
		if ( file.exists( ) )
		{
			//
		}
		else
		{
			System.out.println( "FILE DOES NOT EXIST" );
			return;
		}

		try ( FileReader fileReader = new FileReader( file );
			  BufferedReader bufferedReader = new BufferedReader( fileReader ) )
		{
			String initialTimeStamp = bufferedReader.readLine( );
			String link = bufferedReader.readLine( );
			String headline = bufferedReader.readLine( );
			String columnNames = bufferedReader.readLine( );
			String cssQueries = bufferedReader.readLine( );

			String[] columnNameArray = columnNames.split( " " );

			chartTitle = headline;

			dataValueNames = Arrays.asList( columnNameArray );

			// Removing timestamp.
			//dataValueNames.remove( 0 );

			String line = "";
			dataPoints = new ArrayList <>( );
			/*
			while ( ( line = bufferedReader.readLine( ) ) != null )
			{
				String[] values = line.split( " " );

				StatsFile.DataPoint dataPoint = new StatsFile.DataPoint( );
				dataPoint.dateTime = ZonedDateTime.parse( values[ 0 ] );
//				dataPoint.data = new float[ values.length - 1 ];

				for ( int i = 1; i < values.length; i += 1 )
				{
//					dataPoint.data[ i - 1 ] = Float.parseFloat( values[ i ] );
				}

				dataPoints.add( dataPoint );
			}*/
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}

		exposedSeries = new ArrayList <>( );
	}

	private List < String > getXAxisLabels( )
	{
		List < String > xAxisLabels = new ArrayList <>( );

		for ( int i = 0; i < dataPoints.size( ); i += 1 )
		{
			LocalDateTime dateTime = dataPoints.get( i ).dateTime.toLocalDateTime( );

			xAxisLabels.add( String.valueOf( dateTime.getDayOfMonth( ) ) + "-" +
					String.valueOf( dateTime.getHour( ) ) + "-" +
					String.valueOf( dateTime.getMinute( ) ) + "-" +
					String.valueOf( dateTime.getSecond( ) ) );

//			xAxisLabels.add( dateTime.toString( ) );
		}

		return xAxisLabels;
	}

	private void updateChart( )
	{
		NumberAxis xAxis = new NumberAxis( );
//		xAxis.setCenterShape( false );
		xAxis.setForceZeroInRange( false );


		if ( isDaysModeEnabled )
		{
			xAxis.setAutoRanging( false );
			LocalDateTime startDateTime = LocalDateTime.of(
					dataPoints.get( 0 ).dateTime.toLocalDate( ),
					LocalTime.MIN );
			LocalDateTime endDateTime = LocalDateTime.of(
					dataPoints.get( dataPoints.size( ) - 1 ).dateTime.toLocalDate( ),
					LocalTime.MAX );
			xAxis.setLowerBound( Duration.between( dataPoints.get( 0 ).dateTime.toLocalDateTime( ), startDateTime ).
					toNanos( ) / 1000000000L );
			xAxis.setUpperBound( Duration.between( dataPoints.get( 0 ).dateTime.toLocalDateTime( ), endDateTime ).
					toNanos( ) / 1000000000L );

			xAxis.setTickUnit( ( xAxis.getUpperBound( ) - xAxis.getLowerBound( ) ) / 25.0 );

			List < Stop > stops = new ArrayList <>( );
			long daysCount = Duration.between( startDateTime, endDateTime ).toDays( ) + 1;
			for ( int i = 0; i < daysCount; i += 1 )
			{
				stops.add( new Stop( ( double ) ( i + 0.00 ) / ( double ) daysCount, Color.GRAY ) );
				stops.add( new Stop( ( double ) ( i + 0.25 ) / ( double ) daysCount, Color.GRAY ) );
				stops.add( new Stop( ( double ) ( i + 0.50 ) / ( double ) daysCount, Color.WHITE ) );
				stops.add( new Stop( ( double ) ( i + 0.75 ) / ( double ) daysCount, Color.WHITE ) );
			}
			stops.add( new Stop( 1.0, Color.GRAY ) );

			LinearGradient linearGradient = new LinearGradient( 0.0, 0.0,
					1.0, 0.0,
					true, null, stops );

			BackgroundFill backgroundFill = new BackgroundFill( linearGradient, null, null );
			xAxis.setBackground( new Background( backgroundFill ) );
		}
		else
		{
			xAxis.setAutoRanging( true );
		}

		xAxis.setTickLabelFormatter( new StringConverter < Number >( )
		{
			@Override
			public String toString( Number object )
			{
//				if(true)return object.toString( );
				ZonedDateTime initialDateTime = dataPoints.get( 0 ).dateTime;
				long timeDiff = object.longValue( );

				LocalDateTime now = initialDateTime.plusSeconds( timeDiff ).toLocalDateTime( );

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy.MM.dd HH.mm" );

				return now.format( formatter );
			}

			@Override
			public Number fromString( String string )
			{
				return null;
			}
		} );


		NumberAxis yAxis = new NumberAxis( );
		yAxis.setForceZeroInRange( false );
		yAxis.setForceZeroInRange( isVerticalZeroIncluded );

//		xAxis.setCategories( FXCollections.observableArrayList( getXAxisLabels( ) ) );

		lineChart = new LineChart <>( xAxis, yAxis );
		lineChart.setTitle( "Statistics" );
		lineChart.setTitle( chartTitle );
		lineChart.setPrefSize( 1.0e6, 1.0e6 );

		lineChart.getData( ).clear( );

		/*
		for ( Series < Number, Number > series : exposedSeries )
		{
			if ( series.isEnabled( ) )
			{
				lineChart.getData( ).add( series.getSeries( ) );
			}
		}*/

		lineChart.setCreateSymbols( false );


		lineChartStackPane.getChildren( ).setAll( lineChart );
	}


}
