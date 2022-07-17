package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Ecliptic coordinates system
 *
 * @author Robin Goumaz (301420)
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
     * @throws IllegalArgumentException if angles are not in the interval
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(
                Preconditions.checkInInterval(INTERVAL_0_TO_TAU, lon),
                Preconditions.checkInInterval(INTERVAL_SYM_PI, lat));
    }

    /**
     * Gets the ecliptic longitude in radians
     *
     * @return the ecliptic longitude in radians
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * Gets the ecliptic longitude in degrees
     *
     * @return the ecliptic longitude in degrees
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * Gets the ecliptic latitude in radians
     *
     * @return the ecliptic latitude in radians
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * Gets the ecliptic latitude in degrees
     *
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
