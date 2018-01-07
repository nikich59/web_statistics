package ru.nikich59.webstatistics.visiocore.controller;

import ru.nikich59.webstatistics.visiocore.adapter.ApplicationAdapter;
import ru.nikich59.webstatistics.visiocore.model.Model;

/**
 * Created by Nikita on 07.01.2018.
 */
public abstract class Controller
{
	private Model model;
	private ApplicationAdapter applicationAdapter;

	protected final void updateView( )
	{
		applicationAdapter.updateView( );
	}
}
