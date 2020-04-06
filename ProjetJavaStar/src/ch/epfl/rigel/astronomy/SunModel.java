package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Model representing the sun
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN();

    private final double lonJ2010 = Angle.ofDeg(279.557208);
    private final double lonPer = Angle.ofDeg(283.112438);
    private final double exSunEarth = 0.016705;
    private static final double AVERAGE_ANGULAR_SPEED = Angle.TAU / 365.242191;

    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = (AVERAGE_ANGULAR_SPEED * daysSinceJ2010) + lonJ2010 - lonPer;
        double trueAnomaly = meanAnomaly + (2 * exSunEarth * Math.sin(meanAnomaly));

        double lonEcliptic = trueAnomaly + lonPer;
        double latEcliptic = 0;

        EclipticCoordinates coord1 = EclipticCoordinates.of(Angle.normalizePositive(lonEcliptic), latEcliptic);

        EquatorialCoordinates coord2 = eclipticToEquatorialConversion.apply(coord1);

        double angularSizeZero = Angle.ofDeg(0.533128);
        double angularSize = angularSizeZero * ((1 + exSunEarth * Math.cos(trueAnomaly)) / (1 - Math.pow(exSunEarth, 2)));

        return new Sun(coord1, coord2, (float) angularSize, (float) meanAnomaly);
    }
}
