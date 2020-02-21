package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.math.Angle.*;

/**
 * Ecliptic coordinates system
 *
 * @author Ozan Güven (297076)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private EclipticCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Creates ecliptic coordinates
     *
     * @param lon the ecliptic longitude in radians
     * @param lat the ecliptic latitude in radians
     * @return the ecliptic coordinates
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Interval intervalOfLon = RightOpenInterval.of(0, TAU);
        Interval intervalOfLat = RightOpenInterval.symmetric(Math.PI);

        return new EclipticCoordinates(Preconditions.checkInInterval(intervalOfLon, lon), Preconditions.checkInInterval(intervalOfLat, lat));
    }

    /**
     * @return the ecliptic longitude in radians
     */
    @Override
    public double lon() {
        return super.longitude;
    }

    /**
     * @return the ecliptic longitude in degrees
     */
    @Override
    public double lonDeg() {
        return Angle.toDeg(super.longitude);
    }

    /**
     * @return the ecliptic latitude in radians
     */
    @Override
    public double lat() {
        return super.latitude;
    }

    /**
     * @return the ecliptic latitude in degrees
     */
    @Override
    public double latDeg() {
        return Angle.toDeg(super.latitude);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(λ=%.4f°, β=%.4f°)",
                lonDeg(),
                latDeg());
    }
}
