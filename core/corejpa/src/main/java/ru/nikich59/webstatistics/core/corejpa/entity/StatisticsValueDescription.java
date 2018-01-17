package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "statistics_value_descriptions" )
@NamedQueries( {
		@NamedQuery( name = "statistics_value_descriptions.findByStatistics",
				query = "SELECT vd from StatisticsValueDescription vd WHERE vd.statistics.id = :id order by vd.name" )
} )
public class StatisticsValueDescription
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	public String getName( )
	{
		return name;
	}
	public void setName( String name )
	{
		this.name = name;
	}
	@Column( name = "name", nullable = false )
	private String name;

	public String getQuery( )
	{
		return query;
	}
	public void setQuery( String query )
	{
		this.query = query;
	}
	@Column( name = "query", nullable = false )
	private String query;

	public StatisticsValueDataType getStatisticsValueDataType( )
	{
		return statisticsValueDataType;
	}
	public void setStatisticsValueDataType( StatisticsValueDataType statisticsValueDataType )
	{
		this.statisticsValueDataType = statisticsValueDataType;
	}
	@OneToOne( optional = false )
	@JoinColumn( name = "value_data_type_id", nullable = false )
	private StatisticsValueDataType statisticsValueDataType;

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

	public static List < StatisticsValueDescription > findByStatistics( EntityManager em, Statistics statistics )
	{
		TypedQuery < StatisticsValueDescription > query =
				em.createNamedQuery( "statistics_value_descriptions.findByStatistics",
						StatisticsValueDescription.class );
		query.setParameter( "id", statistics.getId( ) );

		return query.getResultList( );
	}
}














