package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "sleuth_new_url_acquirers" )
@NamedQueries( {

} )
public class SleuthNewUrlAcquirer
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	@Column( name = "query", nullable = false )
	private String query;

	@Column( name = "prefix", nullable = false )
	private String prefix;

	@Column( name = "postfix", nullable = false )
	private String postfix;

	public String getQuery( )
	{
		return query;
	}
	public void setQuery( String query )
	{
		this.query = query;
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
	public String getSelectRegex( )
	{
		return selectRegex;
	}
	public void setSelectRegex( String selectRegex )
	{
		this.selectRegex = selectRegex;
	}
	@Column( name = "select_regex", nullable = false )
	private String selectRegex;
}
