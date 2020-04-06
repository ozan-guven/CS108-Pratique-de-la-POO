package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Horizontal coordinates system
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    //To create the octant name of the azimuth
    private final static Interval NORTH_1 = RightOpenInterval.of(292.5, 360);
    private final static Interval NORTH_2 = RightOpenInterval.of(0, 67.5);
    private final static Interval SOUTH = RightOpenInterval.of(112.5, 247.5);
    private final static Interval EAST = RightOpenInterval.of(22.5, 157.5);
    private final static Interval WEST = RightOpenInterval.of(202.5, 337.5);

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * Creates horizontal coordinates from radian values
     *
     * @param az  the azimuth in radians (must be in [0, 2*pi[)
     * @param alt the altitude in radians (must be in [-(pi/2), (pi/2)])
     * @return the horizontal coordinates
     * @throws IllegalArgumentException if angles are not in the interval
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(Preconditions.checkInInterval(INTERVAL_0_TO_TAU, az), Preconditions.checkInInterval(INTERVAL_SYM_PI, alt));
    }

    /**
     * Creates a horizontal coordinates from degree values
     *
     * @param azDeg  the azimuth in degrees (must be in [0, 360°[)
     * @param altDeg the altitude in radians (must be in [90°, 90°])
     * @return the horizontal coordinates
     * @throws IllegalArgumentException if angles are not in the interval
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Interval intervalOfAz = RightOpenInterval.of(0, 360);
        Interval intervalOfAlt = ClosedInterval.symmetric(180);

        return new HorizontalCoordinates(Angle.ofDeg(Preconditions.checkInInterval(intervalOfAz, azDeg)), Angle.ofDeg(Preconditions.checkInInterval(intervalOfAlt, altDeg)));
    }

    /**
     * Shows the octant in which the azimuth is
     *
     * @param n the north
     * @param e the east
     * @param s the south
     * @param w the west
     * @return the octant name in which the azimuth is
     */
    public String azOctantName(String n, String e, String s, String w) {
        StringBuilder builder = new StringBuilder();

        if (NORTH_1.contains(azDeg()) || NORTH_2.contains(azDeg())) {
            builder.append(n);
        }
        if (SOUTH.contains(azDeg())) {
            builder.append(s);
        }
        if (EAST.contains(azDeg())) {
            builder.append(e);
        }
        if (WEST.contains(azDeg())) {
            builder.append(w);
        }

        return builder.toString();
    }

    /**
     * Computes the angular distance between the coordinates and another given coordinates
     *
     * @param that the horizontal coordinates to calculate the distance to
     * @return the angular distance in radians
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt()) * Math.cos(that.alt()) * Math.cos(this.az() - that.az()));
    }

    /**
     * Returns the azimuth (in radians)
     *
     * @return the azimuth
     */
    public double az() {
        return lon();
    }

    /**
     * Returns the azimuth (in degrees)
     *
     * @return the azimuth in degrees
     */
    public double azDeg() {
        return lonDeg();
    }

    /**
     * Returns the altitude (in radians)
     *
     * @return the altitude
     */
    public double alt() {
        return lat();
    }

    /**
     * Returns the altitude (in degrees)
     *
     * @return the altitude in degrees
     */
    public double altDeg() {
        return latDeg();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(az=%.4f°, alt=%.4f°)",
                azDeg(),
                altDeg());
    }
}
