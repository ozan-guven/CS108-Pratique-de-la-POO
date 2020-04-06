package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Model representing the moon
 *
 * @author Ozan Güven (297076)
 * @author Robin Goumaz (301420)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON();

    private final double avgLon = Angle.ofDeg(91.929336);
    private final double avgLonPer = Angle.ofDeg(130.143076);
    private final double lonNot = Angle.ofDeg(291.682547);
    private final double orbIncl = Angle.ofDeg(5.145396);
    private final double exOrb = 0.0549;

    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double lonSun = sun.eclipticPos().lon();
        double anomalySun = sun.meanAnomaly();

        // Calculations for the true orbital longitude
        double avgLonOrb = Angle.ofDeg(13.1763966) * daysSinceJ2010 + avgLon;
        double meanAno = avgLonOrb - Angle.ofDeg(0.1114041) * daysSinceJ2010 - avgLonPer;

        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (avgLonOrb - lonSun) - meanAno);
        double corrEA = Angle.ofDeg(0.1858) * Math.sin(anomalySun);
        double corr3 = Angle.ofDeg(0.37) * Math.sin(anomalySun);

        double trueAno = meanAno + evection - corrEA - corr3;
        double corrEC = Angle.ofDeg(6.2886) * Math.sin(trueAno);
        double corr4 = Angle.ofDeg(0.214) * Math.sin(2 * trueAno);

        double corrLonOrb = avgLonOrb + evection + corrEC - corrEA + corr4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (corrLonOrb - lonSun));
        double trueLonOrb = corrLonOrb + variation;

        // Calculations for the ecliptic position
        double meanLonNot = lonNot - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double corrLonNot = meanLonNot - Angle.ofDeg(0.16) * Math.sin(anomalySun);

        double eclLon = Math.atan2(Math.sin(trueLonOrb - corrLonNot)* Math.cos(orbIncl), Math.cos(trueLonOrb - corrLonNot)) + corrLonNot;
        double eclLat = Math.asin(Math.sin(trueLonOrb-corrLonNot)*Math.sin(orbIncl));

        // Calculations for the phase of the moon
        double phase = ((1 - Math.cos(trueLonOrb - lonSun)) / 2);

        // Calculations for the angular size of the moon
        double earthMoonDistance = (1-Math.pow(exOrb, 2))/(1 + exOrb * Math.cos(trueAno + corrEC));
        double angularSize = Angle.ofDeg(0.5181)/earthMoonDistance;

        // Transformation of the coordinates

        EquatorialCoordinates coord = eclipticToEquatorialConversion.apply(EclipticCoordinates.of(Angle.normalizePositive(eclLon), eclLat));

        // Return of the moon
        return new Moon(coord, (float)angularSize, 0, (float)phase);
    }
}
