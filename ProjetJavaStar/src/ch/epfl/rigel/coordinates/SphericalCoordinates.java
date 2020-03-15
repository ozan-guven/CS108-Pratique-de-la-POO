package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Basis spherical representation of coordinates
 *
 * @author Robin Goumaz (301420)
 */
abstract class SphericalCoordinates {
    final double LONGITUDE, LATITUDE;

    SphericalCoordinates(double lon, double lat) {
        LONGITUDE = lon;
        LATITUDE = lat;
    }

    /**
     * Returns the longitude in radians
     *
     * @return longitude
     */
    double lon() {
        return LONGITUDE;
    }

    /**
     * Returns the longitude in degrees
     *
     * @return longitude
     */
    double lonDeg() {
        return Angle.toDeg(LONGITUDE);
    }

    /**
     * Returns the latitude in radians
     *
     * @return latitude
     */
    double lat() {
        return LATITUDE;
    }

    /**
     * Returns the latitude in degrees
     *
     * @return latitude
     */
    double latDeg() {
        return Angle.toDeg(LATITUDE);
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
