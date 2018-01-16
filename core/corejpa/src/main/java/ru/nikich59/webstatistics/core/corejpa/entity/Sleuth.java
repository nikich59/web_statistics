package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;

@Entity
@Table( name = "sleuths" )
@NamedQueries( {

} )
public class Sleuth
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private int id;

	public String getUrl( )
	{
		return url;
	}
	public void setUrl( String url )
	{
		this.url = url;
	}
	@Column( name = "url", nullable = false )
	private String url;

	@OneToOne( optional = false )
	@JoinColumn( name = "new_url_acquirer_id", nullable = false )
	private SleuthNewUrlAcquirer newUrlAcquirer;

	@OneToOne( optional = false )
	@JoinColumn( name = "web_data_type_id", nullable = false )
	private WebDataType webDataType;

	@OneToOne( optional = false )
	@JoinColumn( name = "target_prototype_statistics_id", nullable = false )
	private Statistics targetStatisticsPrototype;

	@Column( name = "description", nullable = true )
	private String description;

	@Column( name = "period_in_millis", nullable = false )
	private long periodInMillis;
}
