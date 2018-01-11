package ru.nikich59.webstatistics.statister.sleuth;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirer;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirerFactory;
import stats.DataType;
import stats.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Created by Nikita on 27.12.2017.
 */
public class Sleuth
{
	public static class UrlDescriptor
	{
		public String url;
		public String newIdQuery;
		public String targetUrlPrefix;
		public String targetUrlPostfix;
		public DataType dataType;
		public String description;

		public UrlDescriptor( JSONObject configJson )
		{
			url = ( String ) configJson.get( "url" );
			newIdQuery = ( String ) configJson.get( "new_id_query" );
			targetUrlPrefix = ( String ) configJson.get( "target_url_prefix" );
			targetUrlPostfix = ( String ) configJson.get( "target_url_postfix" );
			dataType = DataType.fromString( ( String ) configJson.get( "data_type" ) );

			if ( configJson.get( "description" ) != null )
			{
				description = ( String ) configJson.get( "description" );
			}
			else
			{
				description = "";
			}
		}

		public JSONObject getConfigObject( )
		{
			JSONObject configObject = new JSONObject( );
			configObject.put( "url", url );
			configObject.put( "new_id_query", newIdQuery );
			configObject.put( "target_url_prefix", targetUrlPrefix );
			configObject.put( "target_url_postfix", targetUrlPostfix );
			configObject.put( "data_type", dataType.toString( ) );
			configObject.put( "description", description );

			return configObject;
		}
	}

	private class TitleAcquirer
	{
		private WebDataAcquirer acquirer;
		private List < Pair < String, String > > titleParts = new ArrayList <>( );

		public TitleAcquirer( WebDataAcquirer acquirer )
		{
			this.acquirer = acquirer;
		}

		public void setTitleParts( List < Pair < String, String > > titleParts )
		{
			this.titleParts = titleParts;
		}

		public String getTitle( )
				throws Exception
		{
			String title = "";

			for ( Pair < String, String > titlePart : titleParts )
			{
				try
				{
					title += acquirer.getValue( titlePart.getKey( ) ) + titlePart.getValue( );
				}
				catch ( Exception e )
				{
					// Ignoring.
				}
			}

			return title;
		}

		public JSONObject getConfigObject( )
		{
			JSONObject configObject = new JSONObject( );

			configObject.put( "source", titleAcquiringSource.toString( ) );

			JSONArray parts = new JSONArray( );
			for ( Pair < String, String > titlePart : titleParts )
			{
				JSONObject partObject = new JSONObject( );

				partObject.put( "query", titlePart.getKey( ) );
				partObject.put( "postfix", titlePart.getValue( ) );

				parts.add( partObject );
			}

			configObject.put( "parts", parts );

			return configObject;
		}
	}

	private enum TitleAcquiringSource
	{
		SLEUTH,
		STATISTER;

		public static TitleAcquiringSource fromString( String s )
		{
			for ( TitleAcquiringSource source : values( ) )
			{
				if ( source.toString( ).equals( s ) )
				{
					return source;
				}
			}

			throw new UnsupportedOperationException( );
		}

		@Override
		public String toString( )
		{
			switch ( this )
			{
				case SLEUTH:
					return "sleuth";
				case STATISTER:
					return "statister";
				default:
					throw new UnsupportedOperationException( );
			}
		}
	}

	public interface UrlSupplier
	{
		String getUrl( Sleuth.UrlDescriptor urlDescriptor, WebDataAcquirer acquirer );
	}

	private Statistics.StatisticsHeader targetDescriptor;

	private UrlDescriptor urlDescriptor;

	private Consumer < Statistics.StatisticsHeader > siteDescriptorConsumer;

	private Timer timer;

	private String lastUrl = "";

	private TitleAcquiringSource titleAcquiringSource;

	private List < Pair < String, String > > titleParts = new ArrayList <>( );

	private int periodInMillis = 30000;
	public int getPeriodInMillis( )
	{
		return periodInMillis;
	}

	private Exception lastException;

	private SleuthController sleuthController;
	public void setSleuthController( SleuthController sleuthController )
	{
		this.sleuthController = sleuthController;
	}
	public Sleuth( SleuthController sleuthController )
	{
		this.sleuthController = sleuthController;
	}

	public Sleuth( JSONObject configObject )
	{
		urlDescriptor = new UrlDescriptor( ( JSONObject ) configObject.get( "description" ) );

		targetDescriptor =
				new Statistics.StatisticsHeader( ( JSONObject ) configObject.get( "target_description" ) );

		JSONObject titleDescription = ( JSONObject ) configObject.get( "title_description" );

		titleAcquiringSource = TitleAcquiringSource.fromString( ( String ) titleDescription.get( "source" ) );
		JSONArray titleParts = ( JSONArray ) titleDescription.get( "parts" );

		periodInMillis = ( int ) ( long ) configObject.get( "period_in_millis" );

		this.titleParts = new ArrayList <>( );
		for ( Object titlePart : titleParts )
		{
			JSONObject titlePartJson = ( JSONObject ) titlePart;

			this.titleParts.add( new Pair <>( ( String ) titlePartJson.get( "query" ),
					( String ) titlePartJson.get( "postfix" ) ) );
		}
	}

	public JSONObject getConfigObject( )
	{
		JSONObject configObject = new JSONObject( );

		configObject.put( "description", urlDescriptor.getConfigObject( ) );
		configObject.put( "period_in_millis", periodInMillis );
		TitleAcquirer titleAcquirer = new TitleAcquirer( null );
		titleAcquirer.setTitleParts( titleParts );

		configObject.put( "title_description", titleAcquirer.getConfigObject( ) );

		configObject.put( "target_description", targetDescriptor.getConfigObject( ) );

		return configObject;
	}

	public String getFileName( )
	{
		return urlDescriptor.url.
				replace( " ", "" ).replace( "?", "" ).
				replace( ".", "" ).replace( "\\", "" ).
				replace( "/", "" ).replace( ":", "" );
	}

	public String getDescription( )
	{
		return urlDescriptor.description;
	}

	public String getId( )
	{
		return urlDescriptor.url;
	}

	public void setSiteDescriptorConsumer( Consumer < Statistics.StatisticsHeader > siteDescriptorConsumer )
	{
		this.siteDescriptorConsumer = siteDescriptorConsumer;
	}

	public String getUrl( )
	{
		return urlDescriptor.url;
	}

	public Exception getLastException( )
	{
		return lastException;
	}

	public void testConnection( )
			throws WebDataAcquirer.AcquiringException
	{
		WebDataAcquirer acquirer =
				WebDataAcquirerFactory.getDataAcquirer( urlDescriptor.dataType, urlDescriptor.url );

		acquirer.acquireData( );
	}

	private TitleAcquirer getTitleAcquirer( WebDataAcquirer acquirer, String newUrl )
	{
		TitleAcquirer titleAcquirer = new TitleAcquirer( acquirer );

		if ( titleAcquiringSource == TitleAcquiringSource.SLEUTH )
		{
			titleAcquirer = new TitleAcquirer( acquirer );
		}
		else if ( titleAcquiringSource == TitleAcquiringSource.STATISTER )
		{
			try
			{
				titleAcquirer = new TitleAcquirer(
						WebDataAcquirerFactory.getDataAcquirer(
								targetDescriptor.getDataType( ),
								newUrl ).acquireData( ) );
			}
			catch ( Exception e )
			{
				lastException = e;

				// TODO: Implement error handling.
			}
		}
		else
		{
			throw new UnsupportedOperationException( );
		}

		return titleAcquirer;
	}

	public void stop( )
	{
		if ( timer != null )
		{
			timer.cancel( );
		}
	}

	public void start( )
	{
		if ( timer != null )
		{
			timer.cancel( );
		}

		timer = new Timer( );

		timer.scheduleAtFixedRate(
				new TimerTask( )
				{
					@Override
					public void run( )
					{
						lastException = null;

						WebDataAcquirer acquirer =
								WebDataAcquirerFactory.getDataAcquirer( urlDescriptor.dataType, urlDescriptor.url );

						try
						{
							acquirer.acquireData( );
						}
						catch ( Exception e )
						{
							// TODO: Implement error handling.
							lastException = e;

							return;
						}

						String newUrl = urlDescriptor.targetUrlPrefix +
								acquirer.getValue( urlDescriptor.newIdQuery ) + urlDescriptor.targetUrlPostfix;

						if ( ! lastUrl.equals( newUrl ) && ! newUrl.isEmpty( ) )
						{
							lastUrl = newUrl;

							Statistics.StatisticsHeader siteDescriptor =
									new Statistics.StatisticsHeader( targetDescriptor );
							siteDescriptor.setUrl( newUrl );

							TitleAcquirer titleAcquirer = getTitleAcquirer( acquirer, newUrl );

							titleAcquirer.setTitleParts( titleParts );

							try
							{
								siteDescriptor.setHeadline( titleAcquirer.getTitle( ) );
							}
							catch ( Exception e )
							{
								lastException = e;

								// TODO: Implement error handling.
							}


							siteDescriptorConsumer.accept( siteDescriptor );
						}
					}
				},
				0,
				periodInMillis
		);
	}
}
