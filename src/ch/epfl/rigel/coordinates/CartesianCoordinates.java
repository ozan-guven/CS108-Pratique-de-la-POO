package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Interval;

import java.util.Locale;

/**
 * Cartesian coordinates system
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class CartesianCoordinates {
    private final double x, y;

    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates cartesian coordinates with given x and y values
     *
     * @param x the abscissa coordinate
     * @param y the ordinate coordinate
     * @return the cartesian coordinates for the given values
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * Returns the x coordinate (abscissa)
     *
     * @return the x coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Returns the y coordinate (ordinate)
     *
     * @return the y coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Checks if this point is contained in the part of the plane defined by
     * two intervals in x and y
     *
     * @param intervalX the interval along the x axis
     * @param intervalY the interval along the y axis
     * @return true if the point is in the given part
     */
    public boolean inPartOfPlane(Interval intervalX, Interval intervalY) {
        return intervalX.contains(x) && intervalY.contains(y);
    }

    /**
     * Returns the squared euclidean distance to the given point
     *
     * @param toThat the point to compute the distance to
     * @return the squared euclidean distance
     */
    public double distanceToSquared(CartesianCoordinates toThat) {
        return (x - toThat.x()) * (x - toThat.x()) + (y - toThat.y()) * (y - toThat.y());
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(x=%.4f, y=%.4f)",
                x,
                y);
    }
}
