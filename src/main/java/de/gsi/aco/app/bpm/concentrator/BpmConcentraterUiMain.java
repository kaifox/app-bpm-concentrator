package de.gsi.aco.app.bpm.concentrator;

import static cern.japc.core.factory.MapParameterValueFactory.newMapParameterValue;
import static cern.japc.core.factory.SimpleParameterValueFactory.newSimpleParameterValue;
import static de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionMode.FULL_SEQUENCE;
import static de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionMode.STREAMING;
import static de.gsi.aco.app.bpm.concentrator.decoding.DecimationFrequency.FREQ_100_HZ;

import org.minifx.fxcommons.MiniFxSceneBuilder;
import org.minifx.workbench.MiniFx;
import org.minifx.workbench.annotations.Name;
import org.minifx.workbench.annotations.View;
import org.ossgang.commons.observable.ObservableValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cern.japc.core.factory.SelectorFactory;
import cern.japc.value.MapParameterValue;
import de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionMode;
import de.gsi.aco.app.bpm.concentrator.decoding.DecimationFrequency;
import de.gsi.aco.app.bpm.concentrator.views.AcquisitionView;
import de.gsi.aco.commons.JapcProperties;
import de.gsi.cs.co.ap.common.dependencies.config.PropertyConfig;

/**
 * @author Kajetan Fuchsberger
 */
@Configuration
public class BpmConcentraterUiMain {

    public static void main(final String[] args) {
        PropertyConfig.loadDefaultProperties();
        MiniFx.launcher(BpmConcentraterUiMain.class).launch(args);
    }

    @Bean
    public MiniFxSceneBuilder miniFxSceneBuilder() {
        return MiniFxSceneBuilder.miniFxSceneBuilder().withSize(640, 380);
    }

    @Bean
    @View
    @Name("FULL_SEQUENCE")
    public AcquisitionView sequenceView() {
        return new AcquisitionView(acquisitionProperty(FULL_SEQUENCE, FREQ_100_HZ));
    }

    @Bean
    @View
    @Name("STREAMING")
    public AcquisitionView orbitView() {
        return new AcquisitionView(acquisitionProperty(STREAMING, FREQ_100_HZ));
    }

    private ObservableValue<MapParameterValue> acquisitionProperty(final AcquisitionMode acquisitionMode,
            final DecimationFrequency decimationFrequency) {
        final MapParameterValue mpv = newMapParameterValue();
        mpv.put("acquisitionModeFilter", newSimpleParameterValue(acquisitionMode.ordinal() + 1));
        mpv.put("decimationFrequencyFilter", newSimpleParameterValue(decimationFrequency.ordinal() + 1));
        return JapcProperties.mapPvProperty("Concentrator/Acquisition", SelectorFactory.newSelector("", mpv));
    }

}
