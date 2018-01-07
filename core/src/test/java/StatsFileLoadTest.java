import org.junit.Test;
import stats.controller.StatsFileController;

/**
 * Created by Nikita on 06.01.2018.
 */
public class StatsFileLoadTest
{
	@Test
	public void testLoad( )
			throws Exception
	{
		StatsFileController statsFileController = new StatsFileController( );

		statsFileController.setStatisticsDirectory(
				"C:\\main\\projects\\webstatistics\\web_statistics\\statister\\stats2\\" +
						"statisters\\httpswwwgoogleapiscomyoutubev3videospart=" +
						"statistics&id=SUMFmOpFw7w&key=AIzaSyD1yAAiGOS2fygCzie7d5dvWz9pU4EwPlM.stats" );

		statsFileController.loadStatistics( );

		for ( String columnName : statsFileController.getStatistics( ).getColumnNames( ) )
		{
			System.out.println( columnName );
		}

//		assert false;

	}
}
