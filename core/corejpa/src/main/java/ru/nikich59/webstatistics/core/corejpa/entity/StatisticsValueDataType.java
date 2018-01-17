package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "dict_statistics_value_data_types" )
@NamedQueries( {

} )
public class StatisticsValueDataType
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
	@Column( name = "name", nullable = false, unique = true )
	private String name;
}
