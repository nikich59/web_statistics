package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "dict_web_data_types" )
@NamedQueries( {
		@NamedQuery( name = "dict_web_data_types.findByName",
				query = "SELECT t from WebDataType t WHERE t.name = :name" )
} )
public class WebDataType
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

	public static WebDataType findByName( EntityManager em, String name )
	{
		TypedQuery < WebDataType > query = em.createNamedQuery( "dict_web_data_types.findByName",
				WebDataType.class );
		query.setParameter( "name", name );

		return query.getSingleResult( );
	}
}
