package ru.nikich59.webstatistics.statister.webapp;

import ru.nikich59.webstatistics.statister.model.Model;
import ru.nikich59.webstatistics.statister.statistercli.Controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Nikita on 10.01.2018.
 */

public abstract class Servlet extends HttpServlet
{
	protected Model getModel( )
	{
		return model;
	}
	private Model model;

	protected String getSleuthTemplateDirectory( )
	{
		return sleuthTemplateDirectory;
	}
	private String sleuthTemplateDirectory = "";

	protected String getStatisterTemplateDirectoy( )
	{
		return statisterTemplateDirectoy;
	}
	private String statisterTemplateDirectoy = "";

	protected Controller getController( )
	{
		return controller;
	}
	private Controller controller;

	@Override
	public void init( ServletConfig config )
			throws ServletException
	{
		super.init( config );

		model = ( Model ) config.getServletContext( ).getAttribute( "model" );

		controller = ( Controller ) config.getServletContext( ).getAttribute( "controller" );

		sleuthTemplateDirectory =
				( String ) config.getServletContext( ).getAttribute( "sleuth_template_directory" );

		statisterTemplateDirectoy =
				( String ) config.getServletContext( ).getAttribute( "statister_template_directory" );
	}
}
