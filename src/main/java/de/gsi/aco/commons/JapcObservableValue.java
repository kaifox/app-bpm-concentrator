package de.gsi.aco.commons;

import static java.util.Collections.newSetFromMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.ossgang.commons.observable.DispatchingObservable;
import org.ossgang.commons.observable.ObservableValue;

import cern.japc.core.AcquiredParameterValue;
import cern.japc.core.Parameter;
import cern.japc.core.ParameterException;
import cern.japc.core.ParameterValueListener;
import cern.japc.core.Selector;
import cern.japc.core.SubscriptionHandle;
import cern.japc.core.factory.ParameterFactory;

/**
 * NOTE: ON_CHANGE option is ignored!
 *
 * @author Kajetan Fuchsberger
 */
public class JapcObservableValue<T> extends DispatchingObservable<T> implements ObservableValue<T> {

    private static final ParameterFactory PARAMETER_FACTORY = ParameterFactory.newInstance();

    private static final Set<JapcObservableValue<?>> REFS = newSetFromMap(new ConcurrentHashMap<>());

    private final Parameter parameter;
    private final Selector selector;
    private final Function<AcquiredParameterValue, T> fromFesaConversion;

    @SuppressWarnings("unused")
    private final SubscriptionHandle japcSubscriptionHandle;

    private final ParameterValueListener listener = new ParameterValueListener() {

        @Override
        public void valueReceived(final String parameterName, final AcquiredParameterValue value) {
            dispatchValue(fromFesaConversion.apply(value));
        }

        @Override
        public void exceptionOccured(final String parameterName, final String description,
                final ParameterException exception) {
            dispatchException(exception);
        }
    };

    public JapcObservableValue(final String parameterName, final Selector selector,
            final Function<AcquiredParameterValue, T> fromFesaConversion) {
        parameter = newParameter(parameterName);
        REFS.add(this);

        this.fromFesaConversion = fromFesaConversion;
        this.selector = selector;

        this.japcSubscriptionHandle = startSubscription();
    }

    private SubscriptionHandle startSubscription() {
        final SubscriptionHandle s = parameter.createSubscription(selector, listener);
        try {
            s.startMonitoring();
        } catch (final ParameterException e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    private static final Parameter newParameter(final String parameterName) {
        try {
            return PARAMETER_FACTORY.newParameter(parameterName);
        } catch (final ParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get() {
        return fromFesaConversion.apply(getValue());
    }

    private AcquiredParameterValue getValue() {
        try {
            return parameter.getValue(selector);
        } catch (final ParameterException e) {
            throw new RuntimeException(e);
        }
    }

    protected Selector selector() {
        return this.selector;
    }

    protected Parameter parameter() {
        return this.parameter;
    }

}
