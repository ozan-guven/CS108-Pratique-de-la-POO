package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {
    double longitude, latitude;

    SphericalCoordinates(double lon, double lat) {
        longitude = lon;
        latitude = lat;
    }

    abstract double lon();

    abstract double lonDeg();

    abstract double lat();

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
