package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * Enumeration containing the models of the 8 planets of the Solar system.
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612,
            0.205627, 0.387098, 7.0051,
            48.449, 6.74, -0.42, "#A9A9A9"),

    VENUS("Vénus", 0.615207, 272.30044, 131.54,
            0.006812, 0.723329, 3.3947,
            76.769, 16.92, -4.40, "#D3A567"),

    EARTH("Terre", 0.999996, 99.556772, 103.2055,
            0.016671, 0.999985, 0,
            0, 0, 0, "#00CED1"),

    MARS("Mars", 1.880765, 109.09646, 336.217,
            0.093348, 1.523689, 1.8497,
            49.632, 9.36, -1.52, "#FF8C00"),

    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633,
            0.048907, 5.20278, 1.3035,
            100.595, 196.74, -9.40, "#C99039"),

    SATURN("Saturne", 29.310579, 172.398316, 89.567,
            0.053853, 9.51134, 2.4873,
            113.752, 165.60, -8.88, "#F5F5DC"),

    URANUS("Uranus", 84.039492, 356.135400, 172.884833,
            0.046321, 19.21814, 0.773059,
            73.926961, 65.80, -7.19, "#BBE1E4"),

    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07,
            0.010483, 30.1985, 1.7673,
            131.879, 62.20, -6.87, "#1E90FF");

    private static final double AVERAGE_ANGULAR_SPEED = Angle.TAU / 365.242191;

    /**
     * List containing all the planets.
     */
    public static List<PlanetModel> ALL = List.of(PlanetModel.values());
    private static List<PlanetModel> INNER_PLANETS = ALL.subList(0, 2);

    private final double sinOrbIncl;
    private final double cosOrbIncl;

    private final String frenchName; //The french name of the planet
    private final double orbitalRevolution; //The orbital revolution
    private final double lonAtJ2010; //The longitude of the planet at epoch J2010
    private final double lonAtPerigee; //The longitude of the planet at Perigee
    private final double orbitalEccentricity; //The orbital eccentricity of the planet
    private final double orbitalSemiMajorAxis; //The orbital semi-major axis
    private final double lonOfAscendingNode; //The longitude of the ascending node
    private final double angularSize; //The angular size
    private final double magnitude; //The magnitude
    private final String color;

    PlanetModel(String frenchName, double orbitalRevolution, double lonAtJ2010Deg, double lonAtPerigeeDeg,
                double orbitalEccentricity, double orbitalSemiMajorAxis, double orbitalInclinationAtEclipticDeg,
                double lonOfAscendingNodeDeg, double angularSizeArcsec, double magnitude, String color) {
        this.frenchName = frenchName;
        this.orbitalRevolution = orbitalRevolution;
        lonAtJ2010 = Angle.ofDeg(lonAtJ2010Deg);
        lonAtPerigee = Angle.ofDeg(lonAtPerigeeDeg);
        this.orbitalEccentricity = orbitalEccentricity;
        this.orbitalSemiMajorAxis = orbitalSemiMajorAxis;
        double orbitalInclinationAtEcliptic = Angle.ofDeg(orbitalInclinationAtEclipticDeg); //The orbital inclination at the Ecliptic
        this.lonOfAscendingNode = Angle.ofDeg(lonOfAscendingNodeDeg);
        this.angularSize = Angle.ofArcsec(angularSizeArcsec);
        this.magnitude = magnitude;
        this.color = color;

        sinOrbIncl = Math.sin(orbitalInclinationAtEcliptic);
        cosOrbIncl = Math.cos(orbitalInclinationAtEcliptic);
    }

    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        //**************** VALUES FOR THE PLANET ********************
        double nu = trueAnomaly(daysSinceJ2010); //True anomaly

        double radius = radius(nu);
        double helioLon = nu + lonAtPerigee;
        double phi = Math.asin(Math.sin(helioLon - lonOfAscendingNode) * sinOrbIncl);


        double eclRadius = radius * Math.cos(phi);
        double eclHelioLon = Math.atan2(Math.sin(helioLon - lonOfAscendingNode) * cosOrbIncl,
                Math.cos(helioLon - lonOfAscendingNode)) + lonOfAscendingNode;

        //**************** VALUES FOR THE EARTH *********************
        double nuE = EARTH.trueAnomaly(daysSinceJ2010); //True anomaly

        double radiusE = EARTH.radius(nuE);
        double helioLonE = nuE + EARTH.lonAtPerigee;

        //**************** ECLIPTIC COORDINATES *********************
        double lambda; //Ecliptic lon
        double beta; //Ecliptic lat

        double rSinus = radiusE * Math.sin(eclHelioLon - helioLonE);
        if (INNER_PLANETS.contains(this)) {
            lambda = Math.PI + helioLonE + Math.atan2(eclRadius * Math.sin(helioLonE - eclHelioLon),
                    radiusE - eclRadius * Math.cos(helioLonE - eclHelioLon));
        } else {
            lambda = eclHelioLon + Math.atan2(rSinus, eclRadius - radiusE * Math.cos(eclHelioLon - helioLonE));
        }

        beta = Math.atan(eclRadius * Math.tan(phi) * Math.sin(lambda - eclHelioLon) / rSinus);

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(Angle.normalizePositive(lambda), beta);

        //**************** ANGULAR SIZE *****************************
        double rho = Math.sqrt(radiusE * radiusE + radius * radius
                - 2 * radiusE * radius * Math.cos(helioLon - helioLonE) * Math.cos(phi)); //Distance form Earth to planet

        double newAngularSize = angularSize / rho;

        //**************** MAGNITUDE ********************************
        double phase = (1 + Math.cos(lambda - helioLon)) / 2;

        double newMagnitude = magnitude + 5 * Math.log10(radius * rho / Math.sqrt(phase));

        return new Planet(frenchName, eclipticToEquatorialConversion.apply(eclipticCoordinates), (float) newAngularSize, (float) newMagnitude, color);
    }

    /**
     * Computes the true anomaly for the given number
     * of days since J2010
     *
     * @param daysSinceJ2010 the number of days since J2010
     * @return the true anomaly of the planet
     */
    private double trueAnomaly(double daysSinceJ2010) {
        double meanAnomaly = AVERAGE_ANGULAR_SPEED * (daysSinceJ2010 / orbitalRevolution) + lonAtJ2010 - lonAtPerigee; //Mean anomaly
        return meanAnomaly + 2 * orbitalEccentricity * Math.sin(meanAnomaly); //True anomaly
    }

    /**
     * Computes the radius of the planet given its true anomaly
     *
     * @param trueAnomaly the true anomaly of the planet
     * @return the radius of the planet
     */
    private double radius(double trueAnomaly) {
        return (orbitalSemiMajorAxis * (1 - orbitalEccentricity * orbitalEccentricity)) / (1 + orbitalEccentricity * Math.cos(trueAnomaly));
    }
}
