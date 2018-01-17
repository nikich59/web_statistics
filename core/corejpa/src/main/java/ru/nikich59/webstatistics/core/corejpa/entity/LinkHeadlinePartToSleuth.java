package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "link_headline_part_to_sleuth" )
@NamedQueries( {

} )
public class LinkHeadlinePartToSleuth
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	@OneToOne( optional = false )
	@JoinColumn( name = "headline_part_id", nullable = false )
	private HeadlinePart headlinePart;

	public HeadlinePart getHeadlinePart( )
	{
		return headlinePart;
	}
	public void setHeadlinePart( HeadlinePart headlinePart )
	{
		this.headlinePart = headlinePart;
	}
	public Sleuth getSleuth( )
	{
		return sleuth;
	}
	public void setSleuth( Sleuth sleuth )
	{
		this.sleuth = sleuth;
	}
	@OneToOne( optional = false )
	@JoinColumn( name = "sleuth_id", nullable = false )
	private Sleuth sleuth;
}
