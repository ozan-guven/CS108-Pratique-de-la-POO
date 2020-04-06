package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A polynomial function
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Polynomial {

    private final double[] polynomialCoefficients;

    private Polynomial(double coefficientN, double... coefficients) {
        polynomialCoefficients = new double[coefficients.length + 1];
        polynomialCoefficients[0] = coefficientN;
        System.arraycopy(coefficients, 0, polynomialCoefficients, 1, coefficients.length);
    }


    /**
     * Creates a polynomial
     *
     * @param coefficientN highest degree's coefficient
     * @param coefficients list of other decreasing degrees' coefficient
     * @return a polynomial with the given coefficients
     * @throws IllegalArgumentException if the n-th coefficient is 0
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * Evaluates the polynomial at the value x (f(x))
     *
     * @param x the value where we want to evaluate the polynomial
     * @return the evaluated result
     */
    public double at(double x) {
        double total = polynomialCoefficients[0];
        for (int i = 1; i < polynomialCoefficients.length; i++) {
            total = total * x + polynomialCoefficients[i];
        }
        return total;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < polynomialCoefficients.length; i++) {
            if (polynomialCoefficients[i] != 0) {
                if (i > 0 && polynomialCoefficients[i] > 0) {
                    builder.append("+");
                }
                if (Math.abs(polynomialCoefficients[i]) != 1) {
                    builder.append(polynomialCoefficients[i]);
                } else if (polynomialCoefficients[i] == -1) {
                    builder.append("-");
                }
                switch (polynomialCoefficients.length - 1 - i) {
                    case 1:
                        builder.append("x");
                    case 0:
                        break;
                    default:
                        builder.append("x^");
                        builder.append(polynomialCoefficients.length - 1 - i);
                }
            }
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
