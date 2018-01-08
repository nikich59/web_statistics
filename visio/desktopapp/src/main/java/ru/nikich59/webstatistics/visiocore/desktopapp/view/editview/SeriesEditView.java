package ru.nikich59.webstatistics.visiocore.desktopapp.view.editview;

import javafx.scene.layout.StackPane;
import ru.nikich59.webstatistics.visiocore.model.series.Series;

import java.time.ZonedDateTime;

/**
 * Created by Nikita on 26.12.2017.
 */
public abstract class SeriesEditView extends StackPane
{
	public abstract Series< Number, Number > getSeries( );
}
