package ch.epfl.rigel.math;

/**
 * A mathematical interval
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public abstract class Interval {
    private final double lowBound;
    private final double upBound;

    /**
     * Constructor of the interval
     *
     * @param lowBound the lower bound
     * @param upBound  the upper bound
     */
    protected Interval(double lowBound, double upBound) {
        this.lowBound = lowBound;
        this.upBound = upBound;
    }

    /**
     * Gets the lower bound of the interval
     *
     * @return the lower bound
     */
    public double low() {
        return lowBound;
    }

    /**
     * Gets the upper bound of the interval
     *
     * @return the upper bound
     */
    public double high() {
        return upBound;
    }

    /**
     * Gets the size of the interval
     *
     * @return the size of the interval
     */
    public double size() {
        return upBound - lowBound;
    }

    /**
     * Checks if a given value is contained in the interval
     *
     * @param v the value that needs to be checked
     * @return true if v is in the interval and false otherwise
     */
    public abstract boolean contains(double v);

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
