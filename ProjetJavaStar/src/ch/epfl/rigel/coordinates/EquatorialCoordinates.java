package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Equatorial representation of coordinates
 *
 * @author Robin Goumaz (301420)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private EquatorialCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Method to construct equatorial coordinates from the right ascension and the declination
     *
     * @param ra  The right ascension in hour
     * @param dec The declination in degrees
     * @return The coordinates in equatorial representation
     * @throws IllegalArgumentException
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        Interval intervalRa = RightOpenInterval.of(0, Angle.TAU);
        Interval intervalDec = ClosedInterval.symmetric(Angle.TAU/2);

        return new EquatorialCoordinates(Preconditions.checkInInterval(intervalRa, ra), Preconditions.checkInInterval(intervalDec, dec));
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

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(ra=%.4fh, dec=%.4f°)",
                raHr(),
                latDeg()
        );
    }
}
