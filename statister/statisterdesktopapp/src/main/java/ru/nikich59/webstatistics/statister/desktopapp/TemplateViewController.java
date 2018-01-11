package ru.nikich59.webstatistics.statister.desktopapp;

import ru.nikich59.webstatistics.statister.Template;
import ru.nikich59.webstatistics.statister.desktopapp.controller.StatisterViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.TemplateView;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateViewController extends ViewController
{
	private TemplateView view;
	public Template getTemplate( )
	{
		return template;
	}
	private Template template;

	public TemplateViewController( StatisterViewController statisterViewController,
								   TemplateView view,
								   Template template )
	{
		super( statisterViewController );

		this.view = view;
		this.template = template;
	}
}
