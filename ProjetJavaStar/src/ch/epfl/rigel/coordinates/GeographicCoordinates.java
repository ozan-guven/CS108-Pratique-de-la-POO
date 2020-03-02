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
 * @author Robin Goumaz (301420)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Method to create geographic coordinates for longitude and latitude in degrees
     *
     * @param lonDeg The longitude in degrees (must be in [0°, 360°[)
     * @param latDeg The latitude in degrees (must be in [-90°, 90°])
     * @return The coordinates in geographic representation
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Interval lonInterval = RightOpenInterval.symmetric(360);
        Interval latInterval = ClosedInterval.symmetric(180);

        return new GeographicCoordinates(Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)), Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)));
    }

    /**
     * Checks if the longitude is valid
     *
     * @param lonDeg The longitude in degrees
     * @return boolean value of the check
     */
    public static boolean isValidLonDeg(double lonDeg) {
        Interval lonInterval = RightOpenInterval.symmetric(360);

        return lonInterval.contains(lonDeg);
    }

    /**
     * Checks if the latitude is valid
     *
     * @param latDeg The latitude in degrees
     * @return boolean value of the check
     */
    public static boolean isValidLatDeg(double latDeg) {
        Interval latInterval = ClosedInterval.symmetric(180);

        return latInterval.contains(latDeg);
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

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(lon=%.4f°, lat=%.4f°)",
                lonDeg(),
                latDeg()
        );
    }
}
