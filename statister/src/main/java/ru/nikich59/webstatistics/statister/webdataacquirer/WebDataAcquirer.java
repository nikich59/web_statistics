package ru.nikich59.webstatistics.statister.webdataacquirer;

/**
 * Created by Nikita on 25.12.2017.
 */
public interface WebDataAcquirer
{
	WebDataAcquirer acquireData( )
			throws Exception;

	String getValue( String query );
}
