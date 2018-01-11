package ru.nikich59.webstatistics.statister.desktopapp.controller;

import ru.nikich59.webstatistics.statister.TemplateParameter;
import ru.nikich59.webstatistics.statister.desktopapp.TemplateViewController;
import ru.nikich59.webstatistics.statister.desktopapp.ViewController;
import ru.nikich59.webstatistics.statister.desktopapp.view.TemplateParameterView;

/**
 * Created by Nikita on 10.01.2018.
 */
public class TemplateParameterViewController extends ViewController
{
	private TemplateParameterView view;
	private TemplateParameter templateParameter;


	public TemplateParameterViewController( TemplateViewController templateViewController,
											TemplateParameterView view,
											TemplateParameter templateParameter )
	{
		super( templateViewController );

		this.templateParameter = templateParameter;
		this.view = view;
	}

	public void setValue( String value )
	{
		templateParameter.setValue( value );
	}

	public TemplateParameter getTemplateParameter( )
	{
		return templateParameter;
	}
}
