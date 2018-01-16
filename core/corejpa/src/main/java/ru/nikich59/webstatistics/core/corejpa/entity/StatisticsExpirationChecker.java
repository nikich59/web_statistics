package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "statistics_expiration_checkers" )
@NamedQueries( {

} )
public class StatisticsExpirationChecker
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	public String getTargetValue( )
	{
		return targetValue;
	}
	public void setTargetValue( String targetValue )
	{
		this.targetValue = targetValue;
	}
	public String getTargetValueDescription( )
	{
		return targetValueDescription;
	}
	public void setTargetValueDescription( String targetValueDescription )
	{
		this.targetValueDescription = targetValueDescription;
	}
	@Column( name = "target_value", nullable = false )
	private String targetValue;

	@Column( name = "target_value_description", nullable = false )
	private String targetValueDescription;

	public long getTargetPeriodInMinutes( )
	{
		return targetPeriodInMinutes;
	}
	public void setTargetPeriodInMinutes( long targetPeriodInMinutes )
	{
		this.targetPeriodInMinutes = targetPeriodInMinutes;
	}
	@Column( name = "target_period_in_minutes", nullable = false )
	private long targetPeriodInMinutes;
}
