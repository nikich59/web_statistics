package ru.nikich59.webstatistics.visio.desktopcore.view;

import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visio.desktopcore.controller.ViewController;

/**
 * Created by Nikita on 09.01.2018.
 */
public class View < Controller extends ViewController > extends StackPane
{
	private Controller controller;

	public void setController( Controller controller )
	{
		this.controller = controller;
	}

	protected final Controller getController( )
	{
		return controller;
	}
}
