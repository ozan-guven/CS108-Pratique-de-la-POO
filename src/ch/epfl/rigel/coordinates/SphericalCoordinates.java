package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Basis spherical representation of coordinates
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
abstract class SphericalCoordinates {

    //To check if the values are valid for the interval, only used by the subclasses
    /**
     * Constant to check in the interval [0, TAU[
     */
    protected static final Interval INTERVAL_0_TO_TAU = RightOpenInterval.of(0, Angle.TAU);
    /**
     * Constant to check in the interval [- pi / 2, pi / 2]
     */
    protected static final Interval INTERVAL_SYM_PI = ClosedInterval.symmetric(Angle.TAU / 2);

    private final double longitude, latitude;

    SphericalCoordinates(double lon, double lat) {
        longitude = lon;
        latitude = lat;
    }

    /**
     * Returns the longitude in radians
     *
     * @return longitude
     */
    double lon() {
        return longitude;
    }

    /**
     * Returns the longitude in degrees
     *
     * @return longitude
     */
    double lonDeg() {
        return Angle.toDeg(longitude);
    }

    /**
     * Returns the latitude in radians
     *
     * @return latitude
     */
    double lat() {
        return latitude;
    }

    /**
     * Returns the latitude in degrees
     *
     * @return latitude
     */
    double latDeg() {
        return Angle.toDeg(latitude);
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
