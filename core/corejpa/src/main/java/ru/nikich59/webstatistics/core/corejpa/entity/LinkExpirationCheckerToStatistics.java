package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "link_expiration_checker_to_statistics" )
@NamedQueries( {
		@NamedQuery( name = "link_expiration_checker_to_statistics.findByStatistics",
				query = "SELECT link from LinkExpirationCheckerToStatistics link WHERE link.statistics.id = :id" )
} )
public class LinkExpirationCheckerToStatistics
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	@OneToOne( optional = false )
	@JoinColumn( name = "expiration_checker_id", nullable = false )
	private StatisticsExpirationChecker expirationChecker;

	public StatisticsExpirationChecker getExpirationChecker( )
	{
		return expirationChecker;
	}
	public void setExpirationChecker( StatisticsExpirationChecker expirationChecker )
	{
		this.expirationChecker = expirationChecker;
	}
	public Statistics getStatistics( )
	{
		return statistics;
	}
	public void setStatistics( Statistics statistics )
	{
		this.statistics = statistics;
	}
	@OneToOne( optional = false )
	@JoinColumn( name = "statistics_id", nullable = false )
	private Statistics statistics;

	public static List < LinkExpirationCheckerToStatistics > listStatisticsExpirationCheckersByStatistics(
			EntityManager em, Statistics statistics )
	{
		TypedQuery < LinkExpirationCheckerToStatistics > query = em.createNamedQuery(
				"link_expiration_checker_to_statistics.findByStatistics", LinkExpirationCheckerToStatistics.class );
		query.setParameter( "id", statistics.getId( ) );

		return query.getResultList( );
	}

	public static LinkExpirationCheckerToStatistics findStatisticsExpirationCheckerByStatistics(
			EntityManager em, Statistics statistics )
	{
		TypedQuery < LinkExpirationCheckerToStatistics > query = em.createNamedQuery(
				"link_expiration_checker_to_statistics.findByStatistics", LinkExpirationCheckerToStatistics.class );
		query.setParameter( "id", statistics.getId( ) );

		List < LinkExpirationCheckerToStatistics > resultList = query.getResultList( );
		if ( resultList.size( ) > 0 )
		{
			return query.getResultList( ).get( 0 );
		}
		else
		{
			return null;
		}
	}
}
