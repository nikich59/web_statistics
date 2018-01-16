package ru.nikich59.webstatistics.core.corejpa.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Nikita on 13.01.2018.
 */

@Entity
@Table( name = "statistics" )
@NamedQueries( {
		@NamedQuery( name = "statistics.findAll", query = "SELECT s from Statistics s" ),
		@NamedQuery( name = "statistics.findByUrl", query = "SELECT s from Statistics s WHERE s.url = :url" )
} )
public class Statistics
{
	public int getId( )
	{
		return id;
	}
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
	@Column( name = "url", nullable = false, unique = true )
	private String url;

	public ZonedDateTime getInitialDateTime( )
	{
		return ZonedDateTime.of( initialDateTime, ZoneOffset.ofTotalSeconds( initialDateTimeOffset ) );
	}
	public void setInitialDateTime( ZonedDateTime dateTime )
	{
		initialDateTime = dateTime.toLocalDateTime( );
		initialDateTimeOffset = dateTime.getOffset( ).getTotalSeconds( );
	}

	@Column( name = "initial_date_time", nullable = false )
	private LocalDateTime initialDateTime;

	@Column( name = "initial_date_time_offset", nullable = false )
	private int initialDateTimeOffset;

	public String getHeadline( )
	{
		return headline;
	}
	public void setHeadline( String headline )
	{
		this.headline = headline;
	}
	@Column( name = "headline" )
	private String headline;

	public long getPeriodInMillis( )
	{
		return periodInMillis;
	}
	public void setPeriodInMillis( long periodInMillis )
	{
		this.periodInMillis = periodInMillis;
	}
	@Column( name = "period_in_millis", nullable = false )
	private long periodInMillis;


	public WebDataType getWebDataType( )
	{
		return webDataType;
	}
	public void setWebDataType( WebDataType webDataType )
	{
		this.webDataType = webDataType;
	}
	@OneToOne( optional = false )
	@JoinColumn( name = "web_data_type_id", nullable = false )
	private WebDataType webDataType;

/*
	public int getWebDataTypeId( )
	{
		return webDataTypeId;
	}
	public void setWebDataTypeId( int webDataTypeId )
	{
		this.webDataTypeId = webDataTypeId;
	}
	@Column( name = "web_data_type_id" )
	private int webDataTypeId;
*/

	public long getExpirationPeriodInMinutes( )
	{
		return expirationPeriodInMinutes;
	}
	public void setExpirationPeriodInMinutes( long expirationPeriodInMinutes )
	{
		this.expirationPeriodInMinutes = expirationPeriodInMinutes;
	}
	@Column( name = "expiration_period_in_minutes", nullable = false )
	private long expirationPeriodInMinutes;

	public boolean isFinished( )
	{
		return isFinished;
	}
	public void setFinished( boolean finished )
	{
		isFinished = finished;
	}
	@Column( name = "is_finished", nullable = false )
	private boolean isFinished;

	public static List < Statistics > listAll( EntityManager em )
	{
		TypedQuery < Statistics > query = em.createNamedQuery( "statistics.findAll", Statistics.class );

		return query.getResultList( );
	}

	public static Statistics findByUrl( EntityManager em, String url )
	{
		TypedQuery < Statistics > query = em.createNamedQuery( "statistics.findByUrl", Statistics.class );

		return query.getSingleResult( );
	}
}
