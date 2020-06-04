package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Tools to represent Stereographic Projections of Horizontal Coordinates
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double cosPhiCenter; //The cosine of the latitude (altitude) of the center point
    private final double sinPhiCenter; //The sine of the latitude (altitude) of the center point
    private final double tanPhiCenter; //The tangent of the longitude (azimuth) of the center point
    private final double azCenter; //The longitude (azimuth) of the center point

    private final HorizontalCoordinates centerCoord; //The coordinates of the center of the projection

    /**
     * Constructs the stereographic projection centered at the
     * horizontal coordinate center
     *
     * @param center horizontal coordinate of the center of the projection
     */
    public StereographicProjection(HorizontalCoordinates center) {
        cosPhiCenter = Math.cos(center.alt());
        sinPhiCenter = Math.sin(center.alt());
        tanPhiCenter = Math.tan(center.alt());
        azCenter = center.az();

        centerCoord = center;
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
        return Math.abs(Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinPhiCenter));
    }

    /**
     * Gets the cartesian coordinates of the circle corresponding to the projection
     * of the meridian passing through the point hor (Horizontal Coordinates)
     *
     * @param hor horizontal coordinates where the meridian is passing through
     * @return the coordinates of the center of the circle
     */
    public CartesianCoordinates circleCenterForMeridian(HorizontalCoordinates hor) {
        double centerX = - 1d / (cosPhiCenter * Math.tan(hor.az() - azCenter));
        double centerY = - tanPhiCenter;
        return CartesianCoordinates.of(centerX, centerY);
    }

    /**
     * Gets the radius of the circle corresponding to the projection of the meridian
     * passing through the point meridian (Horizontal Coordinates).
     * May return infinity if both the latitude of the point and the center are the same
     *
     * @param meridian horizontal coordinates where the meridian is passing through
     * @return the radius of the circle (may be infinity)
     */
    public double circleRadiusForMeridian(HorizontalCoordinates meridian) {
        return Math.abs(1 / (cosPhiCenter * Math.sin(meridian.az() - azCenter)));
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

        double lambdaDelta = azAlt.az() - centerCoord.az();

        double cosPhiCosLambda = cosPhi * Math.cos(lambdaDelta);

        double d = 1.0 / (1.0 + sinPhi * sinPhiCenter + cosPhiCosLambda * cosPhiCenter);

        double x = d * cosPhi * Math.sin(lambdaDelta);
        double y = d * (sinPhi * cosPhiCenter - cosPhiCosLambda * sinPhiCenter);

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
        if (xy.x() == 0 && xy.y() == 0) {
            return HorizontalCoordinates.of(centerCoord.az(), centerCoord.alt());
        } else {
            double rho = Math.sqrt(xy.x() * xy.x() + xy.y() * xy.y());
            double rhoSquared = rho * rho;
            double sinC = 2 * rho / (rhoSquared + 1);
            double cosC = (1 - rhoSquared) / (rhoSquared + 1);

            double ySinC = xy.y() * sinC;
            double lambda = Math.atan2(xy.x() * sinC, (rho * cosPhiCenter * cosC) - (ySinC * sinPhiCenter)) + centerCoord.az();
            double phi = Math.asin(cosC * sinPhiCenter + ((ySinC * cosPhiCenter) / rho));

            return HorizontalCoordinates.of(Angle.normalizePositive(lambda), phi);
        }
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
                "StereographicProjection centered at horizontal coordinates %s",
                centerCoord.toString());
    }
}
