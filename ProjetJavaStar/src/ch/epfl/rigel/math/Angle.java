package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * A tool to manipulate angles
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Angle {

    /**
     * Number that is two times <i>pi</i>
     *
     * @see Math#PI
     */
    public static final double TAU = Math.PI * 2;
    private static final RightOpenInterval FOR_MIN_SEC = RightOpenInterval.of(0, 60);
    private static final double RAD_PER_SEC = TAU / (3600 * 360);
    private static final double RAD_PER_MIN = TAU / (60 * 360);
    private static final double RAD_PER_HOUR = TAU / 24;
    private static final double HOUR_PER_RAD = 24 / TAU;

    private Angle() {
    }

    /**
     * Normalizes the angle between 0 and 2*<i>pi</i>
     *
     * @param rad the angle
     * @return the normalized angle
     */
    public static double normalizePositive(double rad) {
        //TODO : ici ?
        RightOpenInterval interval = RightOpenInterval.of(0, TAU);
        return interval.reduce(rad);
    }

    /**
     * Gives the radian value of a given angle in seconds
     *
     * @param sec the angle (in seconds)
     * @return the translated angle in radians
     */
    public static double ofArcsec(double sec) {
        return sec * RAD_PER_SEC;
    }

    /**
     * Gives the radian value of an angle given in degrees, minutes, seconds <b>(DMS)</b>
     *
     * @param deg the degrees
     * @param min the minutes
     * @param sec the seconds
     * @return the translated angle in radians
     * @throws IllegalArgumentException if the degrees are negative
     * @throws IllegalArgumentException if the minutes or the seconds are not contained in [0, 60]
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(deg >= 0);
        return checkInInterval(FOR_MIN_SEC, sec) * RAD_PER_SEC + checkInInterval(FOR_MIN_SEC, min) * RAD_PER_MIN + Math.toRadians(deg);
    }

    /**
     * Gives the radian value of a given angle in degrees
     *
     * @param deg the angle in degrees
     * @return the translated angle in radians
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Gives the degree value of a given angle in radians
     *
     * @param rad the angle in radians
     * @return the translated angle in degrees
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Gives the radian value of a given angle in hours
     *
     * @param hr the angle in hours
     * @return the translated angle in radians
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HOUR;
    }

    /**
     * Gives the hour value of a given angle in radian
     *
     * @param rad the angle in hours
     * @return the translated angle in hours
     */
    public static double toHr(double rad) {
        return rad * HOUR_PER_RAD;
    }
}
