package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Class that represents the Sun
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Sun extends CelestialObject {

    private static final String SUN_NAME = "Soleil"; //As the name of the Sun does not change from instances to another
    private static final float SUN_MAGNITUDE = -26.7f; //As the magnitude of the Sun does not change form instances to another

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Constructor of the Sun
     *
     * @param eclipticPos   the ecliptic position of the Sun
     * @param equatorialPos the equatorial position of the Sun
     * @param angularSize   the angular size of the Sun
     * @param meanAnomaly   the mean anomaly of the Sun
     * @throws IllegalArgumentException if the angularSize is negative
     * @throws NullPointerException     if the eclipticPos is null
     * @throws NullPointerException     if the equatorialPos is null
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super(SUN_NAME, equatorialPos, angularSize, SUN_MAGNITUDE);

        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Gets the ecliptic coordinates of the Sun
     *
     * @return the ecliptic coordinates of the Sun
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Gets the mean anomaly of the Sun
     *
     * @return the mean anomaly of the Sun
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}
