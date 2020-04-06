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

    private static final double AVERAGE_ANGULAR_SPEED = Angle.TAU / 365.242191;
    private static final double LON_J2010 = Angle.ofDeg(279.557208);
    private static final double LON_PER = Angle.ofDeg(283.112438);
    private static final double EX_SUN_EARTH = 0.016705;
    private static final double ANGULAR_SIZE_ZERO = Angle.ofDeg(0.533128);

    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = (AVERAGE_ANGULAR_SPEED * daysSinceJ2010) + LON_J2010 - LON_PER;
        double trueAnomaly = meanAnomaly + (2 * EX_SUN_EARTH * Math.sin(meanAnomaly));

        double lonEcliptic = trueAnomaly + LON_PER;
        double latEcliptic = 0;

        EclipticCoordinates coord1 = EclipticCoordinates.of(Angle.normalizePositive(lonEcliptic), latEcliptic);

        EquatorialCoordinates coord2 = eclipticToEquatorialConversion.apply(coord1);

        double angularSize = ANGULAR_SIZE_ZERO * ((1 + EX_SUN_EARTH * Math.cos(trueAnomaly)) / (1 - Math.pow(EX_SUN_EARTH, 2)));

        return new Sun(coord1, coord2, (float) angularSize, (float) meanAnomaly);
    }
}
