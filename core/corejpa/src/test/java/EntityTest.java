import org.junit.Test;
import ru.nikich59.webstatistics.core.corejpa.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikita on 13.01.2018.
 */


public class EntityTest
{
	@Test
	public void testAllEntities( )
	{
		javax.persistence.EntityManagerFactory entityManagerFactory = createTestEntityManagerFactory( );

		EntityManager entityManager = entityManagerFactory.createEntityManager( );

		ru.nikich59.webstatistics.core.corejpa.entity.Statistics.listAll( entityManager );

		entityManager.find( DictHeadlinePartMode.class, 0 );
		entityManager.find( HeadlinePart.class, 0 );
		entityManager.find( LinkExpirationCheckerToStatistics.class, 0 );
		entityManager.find( LinkHeadlinePartToSleuth.class, 0 );
		entityManager.find( Sleuth.class, 0 );
		entityManager.find( SleuthNewUrlAcquirer.class, 0 );
		entityManager.find( StatisticsDataPoint.class, 0 );
		entityManager.find( StatisticsExpirationChecker.class, 0 );
		entityManager.find( StatisticsValue.class, 0 );
		entityManager.find( StatisticsValueDataType.class, 0 );
		entityManager.find( StatisticsValueDescription.class, 0 );
		entityManager.find( WebDataType.class, 0 );
	}

	public static javax.persistence.EntityManagerFactory createTestEntityManagerFactory( )
	{
		Map < String, String > properties = new HashMap <>( );

		properties.put( "javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/statsdb" );
		properties.put( "javax.persistence.jdbc.driver", "org.postgresql.Driver" );
		properties.put( "javax.persistence.jdbc.user", "postgres" );
		properties.put( "javax.persistence.jdbc.password", "142434" );

		return Persistence.createEntityManagerFactory( "Statistics", properties );
	}
}
