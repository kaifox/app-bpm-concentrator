package de.gsi.aco.app.bpm.concentrator;

import cern.japc.core.AcquiredParameterValue;
import cern.japc.core.Parameter;
import cern.japc.core.ParameterException;
import cern.japc.core.ParameterValueListener;
import cern.japc.core.SubscriptionHandle;
import cern.japc.core.factory.ParameterFactory;
import cern.japc.core.factory.SelectorFactory;
import de.gsi.cs.co.ap.common.dependencies.config.PropertyConfig;

/**
 * @author Kajetan Fuchsberger
 */
public class TestMain {

    private static Parameter param;

    private final static ParameterValueListener listener = new ParameterValueListener() {

        @Override
        public void valueReceived(final String parameterName, final AcquiredParameterValue value) {
            System.out.print(value);

        }

        @Override
        public void exceptionOccured(final String parameterName, final String description,
                final ParameterException exception) {
            exception.printStackTrace();
        }
    };

    /**
     * @param args
     * @throws ParameterException
     * @throws InterruptedException
     */
    public static void main(final String[] args) throws ParameterException, InterruptedException {
        PropertyConfig.loadDefaultProperties();

        // cmw.directory.client.serverList=....

        param = ParameterFactory.newInstance().newParameter("Concentrator/Status");

        final SubscriptionHandle s = param.createSubscription(SelectorFactory.newSelector(""), listener);
        s.startMonitoring();
        Thread.sleep(20000);
    }
}
