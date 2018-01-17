package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "statistics_data_points" )
@NamedQueries( {
		@NamedQuery( name = "statistics_data_points.findByStatistics",
				query = "SELECT dp from StatisticsDataPoint dp where dp.statistics.id = :id" )
} )
public class StatisticsDataPoint
{
	public int getId( )
	{
		return id;
	}
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	public ZonedDateTime getTimeStamp( )
	{
		return ZonedDateTime.of( timeStamp, ZoneOffset.ofTotalSeconds( timeStampOffset ) );
	}
	public void setTimeStamp( ZonedDateTime dateTime )
	{
		timeStamp = dateTime.toLocalDateTime( );
		timeStampOffset = dateTime.getOffset( ).getTotalSeconds( );
	}

	@Column( name = "time_stamp", nullable = false )
	private LocalDateTime timeStamp;

	@Column( name = "time_stamp_offset", nullable = false )
	private int timeStampOffset;

	public Statistics getStatistics( )
	{
		return statistics;
	}
	public void setStatistics( Statistics statistics )
	{
		this.statistics = statistics;
	}
	@ManyToOne
	@JoinColumn( name = "statistics_id", nullable = false )
	private Statistics statistics;

	public static List < ru.nikich59.webstatistics.core.corebasics.stats.Statistics.DataPoint >
	findValuesByDataPoint( EntityManager entityManager, Statistics statistics )
	{
		TypedQuery < StatisticsDataPoint > query =
				entityManager.createNamedQuery( "statistics_data_points.findByStatistics",
						StatisticsDataPoint.class );
		query.setParameter( "id", statistics.getId( ) );

		List < StatisticsDataPoint > dataPointsDB = query.getResultList( );
		List < ru.nikich59.webstatistics.core.corebasics.stats.Statistics.DataPoint > dataPoints = new ArrayList <>( );

		for ( StatisticsDataPoint dataPointDB : dataPointsDB )
		{
			List < StatisticsValue > dataPointValues = StatisticsValue.findValuesByDataPoint( entityManager, dataPointDB );

			ru.nikich59.webstatistics.core.corebasics.stats.Statistics.DataPoint dataPoint = new
					ru.nikich59.webstatistics.core.corebasics.stats.Statistics.DataPoint(
					dataPointDB.getTimeStamp( ), new ArrayList <>( ) );

			for ( StatisticsValue statisticsValue : dataPointValues )
			{
				dataPoint.getData( ).add( statisticsValue.getValue( ) );
			}

			dataPoints.add( dataPoint );
		}

		return dataPoints;
	}
}
