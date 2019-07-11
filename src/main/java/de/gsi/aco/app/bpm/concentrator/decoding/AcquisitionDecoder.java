package de.gsi.aco.app.bpm.concentrator.decoding;

import static de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionDecoder.OrbitDimension.BPM;
import static de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionDecoder.OrbitDimension.PLANE;
import static de.gsi.aco.app.bpm.concentrator.decoding.AcquisitionDecoder.OrbitDimension.SAMPLE;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.NoSuchElementException;

import cern.japc.value.MapParameterValue;

/**
 * @author Kajetan Fuchsberger
 */
public class AcquisitionDecoder {

    private final MapParameterValue mpv;

    public AcquisitionDecoder(final MapParameterValue mpv) {
        this.mpv = requireNonNull(mpv, "mapParameterValue must not be null");
    }

    public double[] orbitSample(final Plane plane, final int sampleIndex) {
        final int planeIndex = plane.ordinal();

        final double[] toReturn = new double[sizeOf(BPM)];
        for (int bpmIndex = 0; bpmIndex < sizeOf(BPM); bpmIndex++) {
            final int index = (bpmIndex * sizeOf(PLANE) * sizeOf(SAMPLE)) + (planeIndex * sizeOf(SAMPLE)) + sampleIndex;
            toReturn[bpmIndex] = orbitValueAtIndex(index);
        }

        return toReturn;
    }

    public double[] bpmIndizes() {
        final double[] toReturn = new double[sizeOf(BPM)];

        for (int i = 0; i < sizeOf(BPM); i++) {
            toReturn[i] = i;
        }
        return toReturn;
    }

    public enum Plane {
        H,
        V;
    }

    public enum OrbitDimension {
        BPM("bpms"),
        PLANE("properties"),
        SAMPLE("samples");

        private final String dimensionLabel;

        OrbitDimension(final String dimensionLabel) {
            this.dimensionLabel = dimensionLabel;
        }
    }

    private int sizeOf(final OrbitDimension dimension) {
        final int[] sizes = mpv.getInts("beamPosition_dimensions");
        return sizes[indexOf(dimension)];
    }

    private int indexOf(final OrbitDimension dimension) {
        final String[] dimensionLabels = mpv.getStrings("beamPosition_labels");
        final int index = Arrays.asList(dimensionLabels).indexOf(dimension.dimensionLabel);
        if (index < 0) {
            throw new NoSuchElementException("no dimension of name '" + dimension.dimensionLabel + "' does exist");
        }
        return index;
    }

    private double orbitValueAtIndex(final int index) {
        return mpv.getDoubles("beamPosition")[index];
    }
}
