package ch.epfl.rigel.coordinates;

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
    abstract double lon();

    /**
     * Returns the longitude in degrees
     *
     * @return longitude
     */
    abstract double lonDeg();

    /**
     * Returns the latitude in radians
     *
     * @return latitude
     */
    abstract double lat();

    /**
     * Returns the latitude in degrees
     *
     * @return latitude
     */
    abstract double latDeg();

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
