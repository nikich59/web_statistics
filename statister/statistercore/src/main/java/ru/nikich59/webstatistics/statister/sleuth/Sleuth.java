package ru.nikich59.webstatistics.statister.sleuth;

import ru.nikich59.webstatistics.core.corebasics.stats.Statistics;
import ru.nikich59.webstatistics.core.corebasics.stats.WebDataType;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirer;
import ru.nikich59.webstatistics.statister.webdataacquirer.WebDataAcquirerFactory;

import java.util.*;
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
		public WebDataType dataType;
		public String description;

		public UrlDescriptor( Map < String, Object > configMap )
		{
			url = ( String ) configMap.get( "url" );
			newIdQuery = ( String ) configMap.get( "new_id_query" );
			targetUrlPrefix = ( String ) configMap.get( "target_url_prefix" );
			targetUrlPostfix = ( String ) configMap.get( "target_url_postfix" );
			dataType = WebDataType.fromString( ( String ) configMap.get( "data_type" ) );

			if ( configMap.get( "description" ) != null )
			{
				description = ( String ) configMap.get( "description" );
			}
			else
			{
				description = "";
			}
		}

		public Map < String, Object > getConfigMap( )
		{
			Map < String, Object > configMap = new HashMap <>( );
			configMap.put( "url", url );
			configMap.put( "new_id_query", newIdQuery );
			configMap.put( "target_url_prefix", targetUrlPrefix );
			configMap.put( "target_url_postfix", targetUrlPostfix );
			configMap.put( "data_type", dataType.toString( ) );
			configMap.put( "description", description );

			return configMap;
		}
	}

	enum HeadlinePartMode
	{
		SLEUTH,
		STATISTER,
		RAW;

		public static HeadlinePartMode fromString( String s )
		{
			for ( HeadlinePartMode source : values( ) )
			{
				if ( source.toString( ).equals( s ) )
				{
					return source;
				}
			}

			throw new UnsupportedOperationException( );
		}

		public String getHeadline( WebDataAcquirer sleuthDataAcquirer,
								   WebDataAcquirer statisterDataAcquirer,
								   String query )
		{
			switch ( this )
			{
				case SLEUTH:
					return sleuthDataAcquirer.getValue( query );
				case STATISTER:
					return statisterDataAcquirer.getValue( query );
				case RAW:
					return "";
				default:
					throw new UnsupportedOperationException( );
			}
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
				case RAW:
					return "raw";
				default:
					throw new UnsupportedOperationException( );
			}
		}
	}

	private class HeadlinePart
	{
		public HeadlinePart( Map < String, Object > configMap )
		{
			setMode( HeadlinePartMode.fromString( ( String )
					configMap.getOrDefault( "mode", HeadlinePartMode.RAW.toString( ) ) ) );
			setPrefix( ( String ) configMap.getOrDefault( "prefix", "" ) );
			setPostfix( ( String ) configMap.getOrDefault( "postfix", "" ) );
			setQuery( String.valueOf( configMap.getOrDefault( "query", "" ) ) );
		}

		private HeadlinePartMode mode;
		public HeadlinePartMode getMode( )
		{
			return mode;
		}
		public void setMode( HeadlinePartMode mode )
		{
			this.mode = mode;
		}
		public String getPrefix( )
		{
			return prefix;
		}
		public void setPrefix( String prefix )
		{
			this.prefix = prefix;
		}
		public String getPostfix( )
		{
			return postfix;
		}
		public void setPostfix( String postfix )
		{
			this.postfix = postfix;
		}
		private String prefix = "";
		private String postfix = "";
		public String getQuery( )
		{
			return query;
		}
		public void setQuery( String query )
		{
			this.query = query;
		}
		private String query = "";

		public String getHeadlinePart( WebDataAcquirer sleuthDataAcquirer, WebDataAcquirer statisterDataAcquirer )
		{
			return prefix + mode.getHeadline( sleuthDataAcquirer, statisterDataAcquirer, getQuery( ) ) + postfix;
		}
	}

	private class HeadlineAcquirer
	{

		//		private WebDataAcquirer sleuthAcquirer;
//		private WebDataAcquirer statisterDataAcquirer;
		private List < HeadlinePart > headlineParts = new ArrayList <>( );

		public HeadlineAcquirer(
				//WebDataAcquirer sleuthAcquirer,
//								 WebDataAcquirer statisterDataAcquirer,
				Map < String, Object > configMap )
		{
//			this.sleuthAcquirer = sleuthAcquirer;
//			this.statisterDataAcquirer = statisterDataAcquirer;
			List < Object > headlinePartList = ( List < Object > ) configMap.get( "parts" );
			headlineParts = new ArrayList <>( );
			for ( Object headlinePart : headlinePartList )
			{
				headlineParts.add( new HeadlinePart( ( Map < String, Object > ) headlinePart ) );
			}

		}
		/*
				public void setTitleParts( List < Pair < String, String > > titleParts )
				{
					this.titleParts = titleParts;
				}
		*/
		public String getHeadline( WebDataAcquirer statisterDataAcquirer )
				throws Exception
		{
			StringBuilder headlineBuider = new StringBuilder( );

			for ( HeadlinePart headlinePart : headlineParts )
			{
				try
				{
					headlineBuider.append( headlinePart.getHeadlinePart( dataAcquirer, statisterDataAcquirer ) );
				}
				catch ( Exception e )
				{
					// Ignoring.
				}
			}

			return headlineBuider.toString( );
		}

		public Map < String, Object > getConfigObject( )
		{
			Map < String, Object > configMap = new HashMap <>( );

//			configMap.put( "source", titleAcquiringSource.toString( ) );

			List < Object > parts = new ArrayList <>( );
//			JSONArray parts = new JSONArray( );
			for ( HeadlinePart headlinePart : headlineParts )
			{
//				JSONObject partObject = new JSONObject( );
				Map < String, Object > headlinePartMap = new HashMap <>( );

				headlinePartMap.put( "query", headlinePart.getQuery( ) );
				headlinePartMap.put( "prefix", headlinePart.getPrefix( ) );
				headlinePartMap.put( "postfix", headlinePart.getPostfix( ) );
				headlinePartMap.put( "mode", headlinePart.getMode( ).toString( ) );

				parts.add( headlinePartMap );
			}

			configMap.put( "parts", parts );

			return configMap;
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

	private HeadlineAcquirer headlineAcquirer;

	private WebDataAcquirer dataAcquirer;

//	private HeadlineAcquirer titleAcquiringSource;

//	private List < Pair < String, String > > titleParts = new ArrayList <>( );

	private long periodInMillis = 30000;
	public long getPeriodInMillis( )
	{
		return periodInMillis;
	}

	private Exception lastException;
	/*
		private SleuthController sleuthController;
		public void setSleuthController( SleuthController sleuthController )
		{
			this.sleuthController = sleuthController;
		}
		public Sleuth( SleuthController sleuthController )
		{
			this.sleuthController = sleuthController;
		}
	*/
	public Sleuth(
			//WebDataAcquirer sleuthDataAcquirer,
//				   WebDataAcquirer statisterDataAcquirer,
			Map < String, Object > configMap )
	{
		urlDescriptor = new UrlDescriptor( ( Map < String, Object > ) configMap.get( "description" ) );

		targetDescriptor =
				new Statistics.StatisticsHeader( ( Map < String, Object > ) configMap.get( "target_description" ) );

		Map < String, Object > titleDescription = ( Map < String, Object > ) configMap.get( "headline_description" );

		headlineAcquirer = new HeadlineAcquirer( titleDescription );

		dataAcquirer = WebDataAcquirerFactory.getDataAcquirer( urlDescriptor.dataType, urlDescriptor.url );

//		List< Object >  titleParts = ( List< Object > ) titleDescription.get( "parts" );

		periodInMillis = Long.parseLong( String.valueOf( configMap.get( "period_in_millis" ) ) );

/*		this.titleParts = new ArrayList <>( );
		for ( Object titlePart : titleParts )
		{
			JSONObject titlePartJson = ( JSONObject ) titlePart;

			this.titleParts.add( new Pair <>( ( String ) titlePartJson.get( "query" ),
					( String ) titlePartJson.get( "postfix" ) ) );
		}
		*/
	}

	public Map < String, Object > getConfigMap( )
	{
		Map < String, Object > configObject = new HashMap <>( );

		configObject.put( "description", urlDescriptor.getConfigMap( ) );
		configObject.put( "period_in_millis", periodInMillis );
		configObject.put( "headline_description", headlineAcquirer.getConfigObject( ) );
/*
		HeadlineAcquirer headlineAcquirer = new HeadlineAcquirer( null );
		titleAcquirer.setTitleParts( titleParts );

		configObject.put( "title_description", titleAcquirer.getConfigObject( ) );
*/
		configObject.put( "target_description", targetDescriptor.getConfigMap( ) );

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
						timerHandler( );
					}
				},
				0,
				periodInMillis
		);
	}

	private void timerHandler( )
	{
		lastException = null;
/*
		WebDataAcquirer acquirer =
				WebDataAcquirerFactory.getDataAcquirer( urlDescriptor.dataType, urlDescriptor.url );
*/
		try
		{
			dataAcquirer.acquireData( );
		}
		catch ( Exception e )
		{
			// TODO: Implement error handling.
			lastException = e;

			return;
		}

		String newUrl = urlDescriptor.targetUrlPrefix +
				dataAcquirer.getValue( urlDescriptor.newIdQuery ) + urlDescriptor.targetUrlPostfix;

		if ( ! lastUrl.equals( newUrl ) && ! newUrl.isEmpty( ) )
		{
			lastUrl = newUrl;

			Statistics.StatisticsHeader siteDescriptor =
					new Statistics.StatisticsHeader( targetDescriptor );
			siteDescriptor.setUrl( newUrl );
/*
			HeadlineAcquirer titleAcquirer = getTitleAcquirer( acquirer, newUrl );

			titleAcquirer.setTitleParts( titleParts );
*/
			WebDataAcquirer statisterDataAcquirer =
					WebDataAcquirerFactory.getDataAcquirer( targetDescriptor.getDataType( ), newUrl );
			try
			{
				siteDescriptor.setHeadline( headlineAcquirer.getHeadline( statisterDataAcquirer ) );
			}
			catch ( Exception e )
			{
				lastException = e;

				// TODO: Implement error handling.
			}


			siteDescriptorConsumer.accept( siteDescriptor );
		}
	}
}
