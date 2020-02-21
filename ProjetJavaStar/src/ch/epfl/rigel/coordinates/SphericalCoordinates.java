package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {
    double longitude, latitude;

    SphericalCoordinates(double lon, double lat){
        longitude = lon;
        latitude = lat;
    }

    double lon(){
        return longitude;
    }

    double lonDeg(){
        return Angle.toDeg(longitude);
    }

    double lat(){
        return latitude;
    }

    double latDeg(){
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
