package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.math.Angle.*;

/**
 * Horizontal coordinates system
 *
 * @author Ozan Güven (297076)
 */
public class HorizontalCoordinates extends SphericalCoordinates {

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * Creates a horizontal coordinates from radian values
     *
     * @param az  the azimuth in radians (must be in [0, 2*pi[)
     * @param alt the altitude in radians (must be in [-(pi/2), (pi/2)])
     * @return the horizontal coordinates
     * @throws IllegalArgumentException
     */
    public static HorizontalCoordinates of(double az, double alt) {
        Interval intervalOfAz = RightOpenInterval.of(0, TAU);
        Interval intervalOfAlt = ClosedInterval.symmetric(TAU / 4);

        return new HorizontalCoordinates(Preconditions.checkInInterval(intervalOfAz, Angle.toDeg(az)), Preconditions.checkInInterval(intervalOfAlt, Angle.toDeg(alt)));
    }

    /**
     * Creates a horizonzal coordinates from degree values
     *
     * @param az  the azimute in degrees (must be in [0, 360°[)
     * @param alt the altitude in radians (must be in [90°, 90°])
     * @return the horizonal coordinates
     * @throws IllegalArgumentException
     */
    public static HorizontalCoordinates ofDeg(double az, double alt) {
        Interval intervalOfAz = RightOpenInterval.of(0, 360);
        Interval intervalOfAlt = ClosedInterval.symmetric(90);

        return new HorizontalCoordinates(Preconditions.checkInInterval(intervalOfAz, az), Preconditions.checkInInterval(intervalOfAlt, az));
    }

    /**
     * @return the azimuth
     */
    public double az() {
        return lon();
    }

    /**
     * @return the azimuth in degrees
     */
    public double azDeg() {
        return lonDeg();
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
        //TODO
        int index = 0;
        StringBuilder builder = null;
        builder.append(n);

        /*
        switch (index) {
            case 0:
                builder.append(n);
            case 1:
                builder.append(e);
            case 2:
                builder.append(s);
            case 3:
                builder.append(w);
        }*/

        return null;
    }

    /**
     * @return the altitude
     */
    public double alt() {
        return lat();
    }

    /**
     * @return the altitude in degrees
     */
    public double altDeg() {
        return latDeg();
    }

    /**
     * Computes the angular distance between the coordinates and another given coordinates
     *
     * @param that the horizontal coordinates to calculate the distance to
     * @return the angular distance in radians
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double delta = 0;

        delta = Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt()) * Math.cos(that.alt()) * Math.cos(this.az() - that.az()));

        return delta;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(az=%.4f°, alt=%.4f°)",
                az(),
                alt());
    }

    @Override
    double lon() {
        return super.longitude;
    }

    @Override
    double lonDeg() {
        return Angle.ofDeg(super.longitude);
    }

    @Override
    double lat() {
        return super.latitude;
    }

    @Override
    double latDeg() {
        return Angle.toDeg(super.latitude);
    }
}
