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

    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Interval lonInterval = RightOpenInterval.symmetric(180);
        Interval latInterval = ClosedInterval.symmetric(90);

        System.out.println(lonInterval.high()+"   " + lonInterval.low());
        System.out.println(latInterval.high()+"    " + latInterval.low());

        return new GeographicCoordinates(Angle.ofDeg(Preconditions.checkInInterval(lonInterval, lonDeg)), Angle.ofDeg(Preconditions.checkInInterval(latInterval, latDeg)));
    }

    public static boolean isValidLonDeg(double lonDeg) {
        Interval lonInterval = RightOpenInterval.symmetric(180);

        return lonInterval.contains(lonDeg);
    }

    public static boolean isValidLatDeg(double latDeg) {
        Interval latInterval = ClosedInterval.symmetric(90);

        return latInterval.contains(latDeg);
    }

    @Override
    public double lon() {
        return super.longitude;
    }

    @Override
    public double lonDeg() {
        return Angle.toDeg(super.longitude);
    }

    @Override
    public double lat() {
        return super.latitude;
    }

    @Override
    public double latDeg() {
        return Angle.toDeg(super.latitude);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(lon=%.4f, lat=%.4f)",
                lonDeg(),
                latDeg()
        );
    }
}
