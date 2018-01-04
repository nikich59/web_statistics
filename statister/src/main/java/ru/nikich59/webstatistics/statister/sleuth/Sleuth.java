package ru.nikich59.webstatistics.statister.sleuth;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.nikich59.webstatistics.statister.*;
import stats.DataType;

import java.io.FileReader;
import java.io.IOException;
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

		public UrlDescriptor( JSONObject configJson )
		{
			url = ( String ) configJson.get( "url" );
			newIdQuery = ( String ) configJson.get( "new_id_query" );
			targetUrlPrefix = ( String ) configJson.get( "target_url_prefix" );
			targetUrlPostfix = ( String ) configJson.get( "target_url_postfix" );
			dataType = DataType.fromString( ( String ) configJson.get( "data_type" ) );
		}
	}

	public static final String TITLE_RAW_PART_PREFIX = "$raw$";

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
	}

	private enum TitleAcquiringSource
	{
		SLEUTH,
		STATISTER;

		public static TitleAcquiringSource fromString( String s )
		{
			switch ( s )
			{
				case "sleuth":
					return SLEUTH;
				case "statister":
					return STATISTER;
			}

			throw new UnsupportedOperationException( );
		}
	}

	public interface UrlSupplier
	{
		String getUrl( Sleuth.UrlDescriptor urlDescriptor, WebDataAcquirer acquirer );
	}

	private SiteStatisticsAcquirer.SiteDescriptor targetDescriptor;

	private UrlDescriptor urlDescriptor;
	private Consumer < SiteStatisticsAcquirer.SiteDescriptor > siteDescriptorConsumer;
	private Timer timer;
	private String lastUrl = "";

	private TitleAcquiringSource titleAcquiringSource;
	private List < Pair < String, String > > titleParts = new ArrayList <>( );

	private String configFilePath;

	private int periodInMillis = 30000;

	private Exception lastException;

	public Sleuth( String configFilePath )
			throws IOException, ParseException
	{
		if ( configFilePath == null || configFilePath.isEmpty( ) )
		{
			return;
		}

		this.configFilePath = configFilePath;

		JSONObject configJson = ( JSONObject ) new JSONParser( ).parse( new FileReader( configFilePath ) );

		urlDescriptor = new UrlDescriptor( ( JSONObject ) configJson.get( "description" ) );

		targetDescriptor =
				new SiteStatisticsAcquirer.SiteDescriptor( ( JSONObject ) configJson.get( "target_description" ) );

		JSONObject titleDescription = ( JSONObject ) configJson.get( "title_description" );

		titleAcquiringSource = TitleAcquiringSource.fromString( ( String ) titleDescription.get( "source" ) );
		JSONArray titleParts = ( JSONArray ) titleDescription.get( "parts" );

		periodInMillis = ( int ) ( long ) configJson.get( "period_in_millis" );

		this.titleParts = new ArrayList <>( );
		for ( Object titlePart : titleParts )
		{
			JSONObject titlePartJson = ( JSONObject ) titlePart;

			this.titleParts.add( new Pair <>( ( String ) titlePartJson.get( "query" ),
					( String ) titlePartJson.get( "postfix" ) ) );
		}


		start( );
	}

	public String getConfigFilePath( )
	{
		return configFilePath;
	}

	public void setSiteDescriptorConsumer( Consumer < SiteStatisticsAcquirer.SiteDescriptor > siteDescriptorConsumer )
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

						WebDataAcquirer acquirer;
						switch ( urlDescriptor.dataType )
						{
							case JSON:
								acquirer = new WebDataAcquirerJSON( urlDescriptor.url );
								break;
							case XML:
								acquirer = new WebDataAcquirerXML( urlDescriptor.url );
								break;
							default:
								throw new UnsupportedOperationException( );
						}
						try
						{
							acquirer.acquireData( );
						}
						catch ( Exception e )
						{
							lastException = e;

//							e.printStackTrace( );

							return;
						}

						String newUrl = urlDescriptor.targetUrlPrefix +
								acquirer.getValue( urlDescriptor.newIdQuery ) + urlDescriptor.targetUrlPostfix;

						if ( ! lastUrl.equals( newUrl ) && ! newUrl.isEmpty( ) )
						{
							lastUrl = newUrl;

							SiteStatisticsAcquirer.SiteDescriptor siteDescriptor =
									new SiteStatisticsAcquirer.SiteDescriptor( targetDescriptor );
							siteDescriptor.setUrl( newUrl );

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

//									e.printStackTrace( );
								}
							}
							else
							{
								throw new UnsupportedOperationException( );
							}

							titleAcquirer.setTitleParts( titleParts );

							try
							{
								siteDescriptor.setHeadline( titleAcquirer.getTitle( ) );
							}
							catch ( Exception e )
							{
								lastException = e;

//								e.printStackTrace( );
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
