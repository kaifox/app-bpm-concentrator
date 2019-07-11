package de.gsi.aco.commons;

import java.util.function.Function;

import org.ossgang.commons.property.Property;

import cern.japc.core.AcquiredParameterValue;
import cern.japc.core.ParameterException;
import cern.japc.core.Selector;
import cern.japc.value.ParameterValue;

/**
 * NOTE: ON_CHANGE option is ignored!
 *
 * @author Kajetan Fuchsberger
 */
public class JapcProperty<T> extends JapcObservableValue<T> implements Property<T> {

    private final Function<T, ParameterValue> toFesaConversion;

    public JapcProperty(final String parameterName, final Selector selector,
            final Function<AcquiredParameterValue, T> fromFesaConversion,
            final Function<T, ParameterValue> toFesaConversion) {
        super(parameterName, selector, fromFesaConversion);
        this.toFesaConversion = toFesaConversion;
    }

    @Override
    public void set(final T value) {
        final ParameterValue pv = toFesaConversion.apply(value);
        try {
            parameter().setValue(selector(), pv);
        } catch (final ParameterException e) {
            throw new RuntimeException(e);
        }
    }

}
