package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

/**
 * Tools to represent Stereographic Projections of Horizontal Coordinates
 *
 * @author Ozan Güven (297076)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double cosPhiCenter; //The cosine of the latitude (altitude) of the center point
    private final double sinPhiCenter; //The sine of the latitude (altitude) of the center point

    private final double lambdaZero; //The longitude (azimuth) of the center point

    private final String centerCoord; //The coordinates of the center of the projection

    /**
     * Constructs the stereographic projection centered at the
     * horizontal coordinate center
     *
     * @param center horizontal coordinate of the center of the projection
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosPhiCenter = Math.cos(center.alt());
        sinPhiCenter = Math.sin(center.alt());

        lambdaZero = center.az();

        centerCoord = center.toString();
    }

    /**
     * Gets the cartesian coordinates of the circle corresponding to the projection
     * of the parallel passing through the point hor (Horizontal Coordinates)
     *
     * @param hor horizontal coordinates where the parallel is passing through
     * @return the coordinates of the center of the circle
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double centerY = cosPhiCenter / (Math.sin(hor.alt()) + sinPhiCenter);

        return CartesianCoordinates.of(0, centerY);
    }

    /**
     * Gets the radius of the circle corresponding to the projection of the parallel
     * passing through the point parallel (Horizontal Coordinates).
     * May return infinity if both the latitude of the point and the center are the same
     *
     * @param parallel horizontal coordinates where the parallel is passing through
     * @return the radius of the circle (may be infinity)
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinPhiCenter);
    }

    /**
     * Gets the projected diameter of a sphere with angular size rad
     * centered at the projection
     *
     * @param rad angular size of the sphere (in radians)
     * @return the projected diameter of the sphere
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad / 4);
    }


    /**
     * Computes and returns the cartesian coordinates of the stereographic projection
     * for the given horizontal coordinate
     *
     * @param azAlt the horizontal coordinates to be projected
     * @return the cartesian coordinates of the projection
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double cosPhi = Math.cos(azAlt.alt());
        double sinPhi = Math.sin(azAlt.alt());

        double lambdaDelta = azAlt.az() - lambdaZero;

        double cosLambda = Math.cos(lambdaDelta);
        double sinLambda = Math.sin(lambdaDelta);

        double d = 1.0 / (1.0 + sinPhi * sinPhiCenter + cosPhi * cosPhiCenter * cosLambda);

        double x = d * cosPhi * sinLambda;
        double y = d * (sinPhi * cosPhiCenter - cosPhi * sinPhiCenter * cosLambda);

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Computes and returns the horizontal coordinates for the point whose
     * projection is given by the cartesian coordinates xy
     *
     * @param xy the cartesian coordinate to get its horizontal coordinates
     * @return the horizontal coordinates for the given cartesian coordinates
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {

        double rho = Math.sqrt(xy.x() * xy.x() + xy.y() * xy.y());
        double rhoSquared = rho * rho;
        double sinC = 2 * rho / (rhoSquared + 1);
        double cosC = (1 - rhoSquared) / (rhoSquared + 1);

        double lambda = Math.atan2(xy.x() * sinC, (rho * cosPhiCenter * cosC) - (xy.y() * sinPhiCenter * sinC)) + lambdaZero;
        double phi = Math.asin(cosC * sinPhiCenter + ((xy.y() * sinC * cosPhiCenter) / rho));

        return HorizontalCoordinates.of(lambda, phi);
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
                "StereographicProjection centered at horizontal coordinates %s",
                centerCoord);
    }
}
