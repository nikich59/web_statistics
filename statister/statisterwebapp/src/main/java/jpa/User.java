package jpa;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nikita on 13.01.2018.
 */

@Entity
@Table( name = "users" )
@NamedQueries( {
		@NamedQuery( name = "User.findAll", query = "SELECT s FROM User s" )
}
)
public class User
{
	public int getId( )
	{
		return id;
	}
	public void setId( int id )
	{
		this.id = id;
	}
	@Id
	private int id;

	public String getUsername( )
	{
		return username;
	}
	public void setUsername( String username )
	{
		this.username = username;
	}
	@Column( name = "username", nullable = false )
	private String username;

	public String getPassword( )
	{
		return password;
	}
	public void setPassword( String password )
	{
		this.password = password;
	}
	@Column( name = "password", nullable = false )
	private String password;


	@Override
	public String toString( )
	{
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

	public static List < User > listAll( EntityManager em )
	{
		TypedQuery < User > query = em.createNamedQuery( "User.findAll", User.class );

		return query.getResultList( );
	}
}











