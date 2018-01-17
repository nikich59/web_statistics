package ru.nikich59.webstatistics.statister.webapp;

import ru.nikich59.webstatistics.core.corejpa.entity.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nikita on 13.01.2018.
 */

@WebServlet(
		urlPatterns = { "/test/*" }
)
public class TestServlet extends HttpServlet
{
	private Exception ex = null;


	@Override
	public void init( ServletConfig config )
			throws ServletException
	{

	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		try
		{
//			Logger log = Logger.getLogger( "org.hibernate.SQL" );
//			log.setLevel( Level.OFF );

//			Map < String, String > properties = new HashMap < String, String >( );
/*
			properties.put( "javax.persistence.jdbc.user", "admin" );
			properties.put( "javax.persistence.jdbc.password", "admin" );
			*/

//			properties.put( "hibernate.connection.datasource", "java:comp/env/statsdb" );


			EntityManagerFactory ENTITY_MANAGER_FACTORY =
					ru.nikich59.webstatistics.core.corejpa.EntityManagerFactory.
							createEntityManagerFactory( "java:comp/env/statsdb" );

			EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager( );

//			em.getTransaction( ).begin( );

			List < ru.nikich59.webstatistics.core.corejpa.entity.Statistics > statisticsList =
					ru.nikich59.webstatistics.core.corejpa.entity.Statistics.listAll( em );

			for ( Statistics statistics : statisticsList )
			{
				response.getWriter( )
						.write( statistics.getHeadline( ) + " " + statistics.getWebDataType( )
								.getName( ) + " " + statistics.getInitialDateTime( ).toString( ) + " " + "\n" );
			}

			em.close( );
/*
			ENTITY_MANAGER_FACTORY.close( );

			if ( true )
			{
				return;
			}

			Class.forName( "org.postgresql.Driver" );*/
		}
		catch ( Exception e )
		{
			ex = e;
		}

		if ( ex != null )
		{
			ex.printStackTrace( );

			ex.printStackTrace( response.getWriter( ) );

			return;
		}
/*
		try
		{
			String url = "java:/comp/env/jdbc/postgres";

			InitialContext cxt = new InitialContext( );
			if ( cxt == null )
			{
				throw new Exception( "Uh oh -- no context!" );
			}

			DataSource ds = ( DataSource ) cxt.lookup( url );

			if ( ds == null )
			{
				throw new Exception( "Data source not found!" );
			}

			Connection conn = ds.getConnection( );
			Statement stmt = conn.createStatement( );
			ResultSet rs;

			rs = stmt.executeQuery( "SELECT * FROM users" );
			while ( rs.next( ) )
			{
				String lastName = rs.getString( "username" );
				response.getWriter( ).write( lastName + "\n" );
			}
			conn.close( );
		}
		catch ( Exception e )
		{
			response.getWriter( ).write( "Got an exception! \n" );
			response.getWriter( ).write( e.getMessage( ) );
		}*/
	}
}
