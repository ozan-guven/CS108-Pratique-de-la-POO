package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Geographic representation of coordinates
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    //To check if the values are valid for the intervals
    private static final Interval LON_DEG_INTERVAL = RightOpenInterval.symmetric(360);
    private static final Interval LAT_DEG_INTERVAL = ClosedInterval.symmetric(180);

    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Method to create geographic coordinates for longitude and latitude in degrees
     *
     * @param lonDeg The longitude in degrees (must be in [-180°, 180°[)
     * @param latDeg The latitude in degrees (must be in [-90°, 90°])
     * @return The coordinates in geographic representation
     * @throws IllegalArgumentException if angles are not in the interval
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        return new GeographicCoordinates(
                Angle.ofDeg(Preconditions.checkInInterval(LON_DEG_INTERVAL, lonDeg)),
                Angle.ofDeg(Preconditions.checkInInterval(LAT_DEG_INTERVAL, latDeg)));
    }

    /**
     * Checks if the longitude is valid
     *
     * @param lonDeg The longitude in degrees
     * @return boolean value of the check
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return LON_DEG_INTERVAL.contains(lonDeg);
    }

    /**
     * Checks if the latitude is valid
     *
     * @param latDeg The latitude in degrees
     * @return boolean value of the check
     */
    public static boolean isValidLatDeg(double latDeg) {
        return LAT_DEG_INTERVAL.contains(latDeg);
    }

    /**
     * Getter for the longitude
     *
     * @return the longitude in radians
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * Getter for the longitude
     *
     * @return the longitude in degrees
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * Getter for the latitude
     *
     * @return the latitude in radians
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * Getter for the latitude
     *
     * @return the latitude in degrees
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(lon=%.4f°, lat=%.4f°)",
                lonDeg(),
                latDeg()
        );
    }
}
