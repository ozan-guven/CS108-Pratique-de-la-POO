package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Basis spherical representation of coordinates
 *
 * @author Robin Goumaz (301420)
 */
abstract class SphericalCoordinates {
    double longitude, latitude;

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
