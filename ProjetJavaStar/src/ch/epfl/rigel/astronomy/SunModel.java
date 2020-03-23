package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Model representing the sun
 *
 * @author Robin Goumaz (301420)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN(279.557208, 283.112438, 0.016705);

    private final double lonJ2010;
    private final double lonPer;
    private final double exSunEarth;
    private static final double AVERAGE_ANGULAR_SPEED = 360/365.242191;

    /**
     * Constructor of the sun model
     *
     * @param lonJ2010 The longitude of the sun at J2010
     * @param lonPer The longitude of the sun at the perigee
     * @param exSunEarth The eccentricity of the orbit Sun/Earth
     */
    SunModel(double lonJ2010, double lonPer, double exSunEarth){
        this.lonJ2010 = Angle.ofDeg(lonJ2010);
        this.lonPer = Angle.ofDeg(lonPer);
        this.exSunEarth = Angle.ofArcsec(exSunEarth);
    }

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = AVERAGE_ANGULAR_SPEED * daysSinceJ2010 + lonJ2010 - lonPer;
        double trueAnomaly = meanAnomaly + 2*exSunEarth*Math.sin(meanAnomaly);

        double lonEcliptic = trueAnomaly + lonPer;
        double latEcliptic = 0;

        EclipticCoordinates coord1 = EclipticCoordinates.of(lonEcliptic, latEcliptic);

        EquatorialCoordinates coord2 = eclipticToEquatorialConversion.apply(coord1);

        double angularSizeZero = 0.533128;
        double angularSize = angularSizeZero *((1 + exSunEarth * Math.cos(trueAnomaly)) / (1 - Math.pow(exSunEarth, 2)));

        return new Sun(coord1, coord2, (float)Angle.ofDeg(angularSize), (float) meanAnomaly);
    }
}
