package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "headline_parts" )
@NamedQueries( {

} )
public class HeadlinePart
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	@ManyToOne
	@JoinColumn( name = "mode_id", nullable = false )
	private DictHeadlinePartMode mode;

	@Column( name = "prefix" )
	private String prefix;

	public DictHeadlinePartMode getMode( )
	{
		return mode;
	}
	public void setMode( DictHeadlinePartMode mode )
	{
		this.mode = mode;
	}
	public String getPrefix( )
	{
		return prefix;
	}
	public void setPrefix( String prefix )
	{
		this.prefix = prefix;
	}
	public String getPostfix( )
	{
		return postfix;
	}
	public void setPostfix( String postfix )
	{
		this.postfix = postfix;
	}
	@Column( name = "postfix" )
	private String postfix;
}
