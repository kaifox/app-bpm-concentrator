package de.gsi.aco.app.bpm.concentrator.views;

import org.ossgang.commons.observable.Observable;

import de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionData;
import de.gsi.chart.XYChart;
import de.gsi.chart.axes.spi.DefaultNumericAxis;
import de.gsi.chart.plugins.EditAxis;
import de.gsi.chart.plugins.Panner;
import de.gsi.chart.plugins.Zoomer;
import de.gsi.chart.renderer.spi.MountainRangeRenderer;
import de.gsi.dataset.spi.DoubleDataSet3D;

/**
 * @author Kajetan Fuchsberger
 */
public class MountainRangeView extends XYChart {

    private final DoubleDataSet3D hDataSet = new DoubleDataSet3D("H Evolution");
    private final DoubleDataSet3D yDataSet = new DoubleDataSet3D("Y Evolution");

    public MountainRangeView(final Observable<AcquisitionData> observable) {
        super(xAxis(), yAxis());
        observable.subscribe(this::update);
    }

    void init() {

        setTitle("Test data");
        setAnimated(false);
        // chart.rendererList().clear();
        getRenderers().addAll(new MountainRangeRenderer(), new MountainRangeRenderer());
        // mountainRangeRenderer.axesList().addAll(xAxis, yAxis);
        // mountainRangeRenderer.getDatasets().add(readImage());
        getDatasets().setAll(hDataSet, yDataSet);

        // chart.getZAxis().setAutoRanging(false);
        // chart.getZAxis().setUpperBound(1500);
        // chart.getZAxis().setLowerBound(0);
        // chart.getZAxis().setTickUnit(100);

        // chart.setData(readImage());
        setLegendVisible(true);
        //        chart.setLegendSide(Side.RIGHT);
        getPlugins().add(new Zoomer());
        getPlugins().add(new Panner());
        getPlugins().add(new EditAxis());
    }

    private static final DefaultNumericAxis yAxis() {
        final DefaultNumericAxis yAxis = new DefaultNumericAxis();
        yAxis.setAnimated(false);
        yAxis.setAutoRangeRounding(false);
        yAxis.setLabel("Y Position");
        return yAxis;
    }

    private static final DefaultNumericAxis xAxis() {
        final DefaultNumericAxis xAxis = new DefaultNumericAxis();
        xAxis.setAnimated(false);
        xAxis.setAutoRangeRounding(false);
        xAxis.setLabel("X Position");
        return xAxis;
    }

    private void update(final AcquisitionData data) {
        // dataSet.set
    }
}
