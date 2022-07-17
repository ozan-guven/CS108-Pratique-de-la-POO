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

    //Constants of the Moon
    private static final double AVG_LON = Angle.ofDeg(91.929336);
    private static final double AVG_LON_PER = Angle.ofDeg(130.143076);
    private static final double LON_NOT = Angle.ofDeg(291.682547);
    private static final double ORB_INCL = Angle.ofDeg(5.145396);
    private static final double EX_ORB = 0.0549;
    private static final double COS_ORB_INCL = Math.cos(ORB_INCL);
    private static final double SIN_ORB_INCL = Math.sin(ORB_INCL);
    private static final double EXC_SQUARED = (1 - EX_ORB * EX_ORB);

    //Constants are named after the variable in which they are used
    private static final double MEAN_ORB_LON = Angle.ofDeg(13.1763966);
    private static final double MEAN_ANO = Angle.ofDeg(0.1114041);
    private static final double EVICTION = Angle.ofDeg(1.2739);
    private static final double CORR_E_A = Angle.ofDeg(0.1858);
    private static final double CORR_3 = Angle.ofDeg(0.37);
    private static final double CORR_E_C = Angle.ofDeg(6.2886);
    private static final double CORR_4 = Angle.ofDeg(0.214);
    private static final double VARIATION = Angle.ofDeg(0.6583);
    private static final double MEAN_LON_NOT = Angle.ofDeg(0.0529539);
    private static final double CORR_LON_NOT = Angle.ofDeg(0.16);

    //Angular size of the Moon
    private static final double THETA_NOT = Angle.ofDeg(0.5181);

    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double lonSun = sun.eclipticPos().lon();
        double sinSunAno = Math.sin(sun.meanAnomaly());

        // Calculations for the true orbital longitude
        double avgLonOrb = MEAN_ORB_LON * daysSinceJ2010 + AVG_LON;
        double meanAno = avgLonOrb - MEAN_ANO * daysSinceJ2010 - AVG_LON_PER;

        double eviction = EVICTION * Math.sin(2 * (avgLonOrb - lonSun) - meanAno);
        double corrEA = CORR_E_A * sinSunAno;
        double corr3 = CORR_3 * sinSunAno;

        double trueAno = meanAno + eviction - corrEA - corr3;
        double corrEC = CORR_E_C * Math.sin(trueAno);
        double corr4 = CORR_4 * Math.sin(2 * trueAno);

        double corrLonOrb = avgLonOrb + eviction + corrEC - corrEA + corr4;
        double variation = VARIATION * Math.sin(2 * (corrLonOrb - lonSun));
        double trueLonOrb = corrLonOrb + variation;

        // Calculations for the ecliptic position
        double meanLonNot = LON_NOT - MEAN_LON_NOT * daysSinceJ2010;
        double corrLonNot = meanLonNot - CORR_LON_NOT * sinSunAno;

        double sinTrueLonCorrLon = Math.sin(trueLonOrb - corrLonNot);
        double eclLon = Math.atan2(sinTrueLonCorrLon * COS_ORB_INCL,
                Math.cos(trueLonOrb - corrLonNot)) + corrLonNot;
        double eclLat = Math.asin(sinTrueLonCorrLon * SIN_ORB_INCL);

        // Calculations for the phase of the moon
        double phase = ((1 - Math.cos(trueLonOrb - lonSun)) / 2);

        // Calculations for the angular size of the moon
        double earthMoonDistance = EXC_SQUARED / (1 + EX_ORB * Math.cos(trueAno + corrEC));
        double angularSize = THETA_NOT / earthMoonDistance;

        // Transformation of the coordinates
        EquatorialCoordinates coord = eclipticToEquatorialConversion.apply(
                EclipticCoordinates.of(Angle.normalizePositive(eclLon), eclLat));

        // Return of the moon
        return new Moon(coord, (float) angularSize, 0, (float) phase);
    }
}
