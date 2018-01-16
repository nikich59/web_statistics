package ru.nikich59.webstatistics.core.corejpa;

import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikita on 13.01.2018.
 */
public class EntityManagerFactory
{
	public static javax.persistence.EntityManagerFactory createEntityManagerFactory( String connectionString )
	{
		Map < String, String > properties = new HashMap <>( );

//		properties.put( "hibernate.connection.datasource", connectionString );


		return Persistence.createEntityManagerFactory( "Statistics", properties );
	}
}
