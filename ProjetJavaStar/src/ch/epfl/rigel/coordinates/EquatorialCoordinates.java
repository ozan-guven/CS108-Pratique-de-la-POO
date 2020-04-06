package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;

import java.util.Locale;

/**
 * Equatorial representation of coordinates
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private EquatorialCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Method to construct equatorial coordinates from the right ascension and the declination
     *
     * @param ra  The right ascension in degrees (must be in [0, 2*pi[)
     * @param dec The declination in degrees (must be in [-(pi/2), (pi/2)])
     * @return The coordinates in equatorial representation
     * @throws IllegalArgumentException if angles are not in the interval
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(Preconditions.checkInInterval(INTERVAL_0_TO_TAU, ra), Preconditions.checkInInterval(INTERVAL_SYM_PI, dec));
    }

    /**
     * Getter for the right ascension
     *
     * @return the right ascension in radian
     */
    public double ra() {
        return lon();
    }

    /**
     * Getter for the right ascension
     *
     * @return the right ascension in degree
     */
    public double raDeg() {
        return lonDeg();
    }

    /**
     * Getter for the right ascension
     *
     * @return the right ascension in hour
     */
    public double raHr() {
        return Angle.toHr(lon());
    }

    /**
     * Getter for the declination
     *
     * @return the declination in radian
     */
    public double dec() {
        return lat();
    }

    /**
     * Getter for the declination
     *
     * @return the declination in degrees
     */
    public double decDeg() {
        return latDeg();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(ra=%.4fh, dec=%.4f°)",
                raHr(),
                latDeg()
        );
    }
}
