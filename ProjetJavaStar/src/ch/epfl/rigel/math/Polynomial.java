package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.*;

/**
 * A polynomial function
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class Polynomial {

    private final double[] POLYNOMIAL_COEFFICIENTS;

    private Polynomial(double coefficientN, double... coefficients) {
        POLYNOMIAL_COEFFICIENTS = new double[coefficients.length + 1];
        POLYNOMIAL_COEFFICIENTS[0] = coefficientN;
        System.arraycopy(coefficients, 0, POLYNOMIAL_COEFFICIENTS, 1, coefficients.length);
    }


    /**
     * Creates a polynomial
     *
     * @param coefficientN highest degree's coefficient
     * @param coefficients list of other decreasing degrees' coefficient
     * @return a polynomial with the given coefficients
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
        double total = POLYNOMIAL_COEFFICIENTS[0];
        for (int i = 1; i < POLYNOMIAL_COEFFICIENTS.length; i++) {
            total = total * x + POLYNOMIAL_COEFFICIENTS[i];
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        /*if(polynomialCoefficients.length > 1) {
            builder.append(polynomialCoefficients[0]);
            builder.append("x^");
            builder.append(polynomialCoefficients.length - 1);
            for (int i = 1; i < polynomialCoefficients.length - 2; i++) {
                if (polynomialCoefficients[i] != 0) {
                    if (polynomialCoefficients[i] > 0) {
                        builder.append("+");
                    }
                    builder.append(polynomialCoefficients[i]);
                    builder.append("x^");
                    builder.append(polynomialCoefficients.length - 1 - i);
                }
            }
        }
        builder.append(polynomialCoefficients[polynomialCoefficients.length-1]);*/

        for (int i = 0; i < POLYNOMIAL_COEFFICIENTS.length; i++) {
            if (POLYNOMIAL_COEFFICIENTS[i] != 0) {
                if (i > 0 && POLYNOMIAL_COEFFICIENTS[i] > 0) {
                    builder.append("+");
                }
                if (Math.abs(POLYNOMIAL_COEFFICIENTS[i]) != 1) {
                    builder.append(POLYNOMIAL_COEFFICIENTS[i]);
                } else if(POLYNOMIAL_COEFFICIENTS[i] == -1){
                    builder.append("-");
                }
                switch (POLYNOMIAL_COEFFICIENTS.length - 1 - i) {
                    case 1:
                        builder.append("x");
                    case 0:
                        break;
                    default:
                        builder.append("x^");
                        builder.append(POLYNOMIAL_COEFFICIENTS.length - 1 - i);
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
