package de.gsi.aco.commons;

import org.ossgang.commons.property.Property;

import cern.japc.core.Selector;
import cern.japc.core.factory.SimpleParameterValueFactory;
import cern.japc.value.MapParameterValue;

/**
 * @author Kajetan Fuchsberger
 */
public final class JapcProperties {

    private JapcProperties() {
        /* Only static methods */
    }

    public static Property<MapParameterValue> mapPvProperty(final String parameterName, final Selector selector) {
        return new JapcProperty<>(parameterName, selector, apv -> apv.getValue().castAsMap(), mpv -> mpv);
    }

    public static JapcProperty<Double> doublePropery(final String parameterName, final Selector selector) {
        return new JapcProperty<>(parameterName, selector, apv -> apv.getValue().castAsSimple().getDouble(),
                d -> SimpleParameterValueFactory.newSimpleParameterValue(d));
    }

}
