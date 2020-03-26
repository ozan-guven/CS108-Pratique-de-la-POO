package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
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
     * @param lon the ecliptic longitude in radians (must be in [0, 2*pi[)
     * @param lat the ecliptic latitude in radians (must be in [-(pi/2), (pi/2)])
     * @return the ecliptic coordinates
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Interval intervalOfLon = RightOpenInterval.of(0, TAU);
        Interval intervalOfLat = ClosedInterval.symmetric(TAU / 2);

        return new EclipticCoordinates(Preconditions.checkInInterval(intervalOfLon, lon), Preconditions.checkInInterval(intervalOfLat, lat));
    }

    /**
     * @return the ecliptic longitude in radians
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @return the ecliptic longitude in degrees
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return the ecliptic latitude in radians
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @return the ecliptic latitude in degrees
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
                "(λ=%.4f°, β=%.4f°)",
                lonDeg(),
                latDeg());
    }
}
