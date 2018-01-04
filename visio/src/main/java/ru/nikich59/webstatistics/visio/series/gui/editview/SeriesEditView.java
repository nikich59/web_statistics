package ru.nikich59.webstatistics.visio.series.gui.editview;

import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visio.model.series.Series;

/**
 * Created by Nikita on 26.12.2017.
 */
public abstract class SeriesEditView extends StackPane
{
	public abstract Series< Number, Number > getSeries( );
}
