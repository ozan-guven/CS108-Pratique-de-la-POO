package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Cartesian coordinates system
 *
 * @author Ozan Güven (297076)
 */
public final class CartesianCoordinates {
    private final double X, Y;

    private CartesianCoordinates(double x, double y) {
        X = x;
        Y = y;
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
        return X;
    }

    /**
     * Returns the y coordinate (ordinate)
     *
     * @return the y coordinate
     */
    public double y() {
        return Y;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(x=%.4f, y=%.4f)",
                X,
                Y);
    }
}
