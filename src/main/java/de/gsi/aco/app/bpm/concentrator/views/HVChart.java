package de.gsi.aco.app.bpm.concentrator.views;

import static java.util.Objects.requireNonNull;
import static org.ossgang.commons.observable.Observer.withErrorHandling;

import org.ossgang.commons.observable.ObservableValue;

import cern.japc.value.MapParameterValue;
import de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionData;
import de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionData.Plane;
import de.gsi.chart.XYChart;
import de.gsi.chart.axes.spi.DefaultNumericAxis;
import de.gsi.chart.plugins.DataPointTooltip;
import de.gsi.chart.plugins.EditAxis;
import de.gsi.chart.plugins.ParameterMeasurements;
import de.gsi.chart.plugins.TableViewer;
import de.gsi.chart.plugins.Zoomer;
import de.gsi.chart.renderer.ErrorStyle;
import de.gsi.chart.renderer.spi.ErrorDataSetRenderer;
import de.gsi.dataset.spi.DoubleDataSet;

/**
 * @author Kajetan Fuchsberger
 */
public class HVChart extends XYChart {
    private final ObservableValue<MapParameterValue> orbitProperty;

    private final DoubleDataSet hDataSet = new DoubleDataSet("H");
    private final DoubleDataSet vDataSet = new DoubleDataSet("V");

    public HVChart(final ObservableValue<MapParameterValue> orbitProperty) {
        super(new DefaultNumericAxis(), new DefaultNumericAxis());
        this.orbitProperty = requireNonNull(orbitProperty);
        init();
    }

    private void init() {

        legendVisibleProperty().set(true);
        getXAxis().setLabel("BPM");
        //chart.getXAxis().setAutoUnitScaling(true);

        getYAxis().setLabel("pos");
        getYAxis().setUnit("mm");

        //chart.getYAxis().setAutoUnitScaling(true);
        legendVisibleProperty().set(true);
        getPlugins().add(new ParameterMeasurements());
        getPlugins().add(new EditAxis());
        final Zoomer zoomer = new Zoomer();
        zoomer.setUpdateTickUnit(true);
        zoomer.setSliderVisible(false);
        zoomer.setAddButtonsToToolBar(false);
        getPlugins().add(zoomer);
        getPlugins().add(new DataPointTooltip());
        getPlugins().add(new TableViewer());

        // set them false to make the plot faster
        setAnimated(false);

        final ErrorDataSetRenderer errorRenderer = new ErrorDataSetRenderer();
        getRenderers().setAll(errorRenderer);
        errorRenderer.setErrorType(ErrorStyle.ERRORBARS);
        errorRenderer.setErrorType(ErrorStyle.ERRORCOMBO);
        errorRenderer.setDrawMarker(true);
        errorRenderer.setMarkerSize(1.0);

        getDatasets().add(hDataSet);
        getDatasets().add(vDataSet);

        orbitProperty.subscribe(withErrorHandling(this::update, System.out::println));
    }

    private void update(final MapParameterValue mpv) {
        final AcquisitionData decoder = new AcquisitionData(mpv);

        final double[] h = decoder.orbitSample(Plane.H, 0);
        final double[] v = decoder.orbitSample(Plane.V, 0);
        final double[] x = decoder.bpmIndizes();

        hDataSet.set(x, h);
        vDataSet.set(x, v);

    }

}
