package de.gsi.aco.app.bpm.concentrator.views;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.ossgang.commons.observable.ObservableValue;

import cern.japc.value.MapParameterValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author Kajetan Fuchsberger
 */
public class AcquisitionView extends TabPane {

    private final ObservableValue<MapParameterValue> orbitProperty;

    public AcquisitionView(final ObservableValue<MapParameterValue> orbitProperty) {
        this.orbitProperty = Objects.requireNonNull(orbitProperty);
    }

    @PostConstruct
    public void init() {
        addTab("H/V", new HVChart(orbitProperty));
    }

    private void addTab(final String text, final Node content) {
        final Tab xyTab = new Tab(text, content);
        xyTab.setClosable(false);
        getTabs().add(xyTab);
    }

}
