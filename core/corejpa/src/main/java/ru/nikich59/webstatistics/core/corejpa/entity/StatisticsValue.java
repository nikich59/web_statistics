package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "statistics_values" )
@NamedQueries( {
		@NamedQuery( name = "statistics_values.findByDataPoint",
				query = "SELECT v from StatisticsValue v WHERE v.dataPoint.id = :id ORDER BY v.valueDescription.name" )
} )
public class StatisticsValue
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	public StatisticsDataPoint getDataPoint( )
	{
		return dataPoint;
	}
	public void setDataPoint( StatisticsDataPoint dataPoint )
	{
		this.dataPoint = dataPoint;
	}
	@ManyToOne
	@JoinColumn( name = "data_point_id", nullable = false )
	private StatisticsDataPoint dataPoint;

	public StatisticsValueDescription getValueDescription( )
	{
		return valueDescription;
	}
	public void setValueDescription( StatisticsValueDescription valueDescription )
	{
		this.valueDescription = valueDescription;
	}
	@ManyToOne
	@JoinColumn( name = "value_description_id", nullable = false )
	private StatisticsValueDescription valueDescription;

	public String getValue( )
	{
		return value;
	}
	public void setValue( String value )
	{
		this.value = value;
	}
	@Column( name = "value", nullable = false )
	private String value;

	public static List < StatisticsValue > findValuesByDataPoint(
			EntityManager entityManager, StatisticsDataPoint dataPoint )
	{
		TypedQuery < StatisticsValue > query =
				entityManager.createNamedQuery( "statistics_values.findByDataPoint",
						StatisticsValue.class );
		query.setParameter( "id", dataPoint.getId( ) );

		return query.getResultList( );
	}
}
