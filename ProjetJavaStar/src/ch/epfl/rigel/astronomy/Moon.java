package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * Class that represents the Moon
 *
 * @author Ozan Güven (297076)
 */
public final class Moon extends CelestialObject {

    private final static String MOON_NAME = "Lune"; //As the name of the Moon does not change from one instances to another

    private final float phase;

    /**
     * Constructor of the Moon
     *
     * @param equatorialPos the equatorial position of the Moon
     * @param angularSize   the angular size of the Moon
     * @param magnitude     the magnitude of the Moon
     * @param phase         the phase of the Moon (must be in [0, 1])
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws IllegalArgumentException if the phase is not in the interval [0, 1]
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super(MOON_NAME, equatorialPos, angularSize, magnitude);

        ClosedInterval interval = ClosedInterval.of(0, 1);

        this.phase = (float) Preconditions.checkInInterval(interval, phase);
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT,
                "Lune (%.1f%%)", //We need to put %% to print % and not to see it as a format
                phase * 100);
    }
}
