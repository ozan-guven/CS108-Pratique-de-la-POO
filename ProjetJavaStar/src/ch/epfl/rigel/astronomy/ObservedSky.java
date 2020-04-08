package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.ClosedInterval;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Class representing a set of celestial objects projected
 * onto a plan with a stereographic projection
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class ObservedSky {

    private final ZonedDateTime time;
    private final GeographicCoordinates coordinates;
    private final StereographicProjection projection;
    private final StarCatalogue catalogue;

    private final Sun sun;
    private final Moon moon;
    private final ArrayList<Planet> planets;
    private ArrayList<Star> stars;

    private final CartesianCoordinates sunCoordinates;
    private final CartesianCoordinates moonCoordinates;
    private final double[] planetPositions = new double[14];

    private Map<CartesianCoordinates, CelestialObject> mapOfAll;

    public ObservedSky(ZonedDateTime time, GeographicCoordinates coordinates,
                       StereographicProjection projection, StarCatalogue catalogue) {
        this.time = time;
        this.coordinates = coordinates;
        this.projection = projection;
        this.catalogue = catalogue;

        double daysUntilJ2010 = Epoch.J2010.daysUntil(time);
        EclipticToEquatorialConversion conversionToEqu = new EclipticToEquatorialConversion(time);
        EquatorialToHorizontalConversion conversionToHor = new EquatorialToHorizontalConversion(time, coordinates);

        mapOfAll = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, conversionToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, conversionToEqu);

        sunCoordinates = projection.apply(conversionToHor.apply(sun.equatorialPos()));
        moonCoordinates = projection.apply(conversionToHor.apply(moon.equatorialPos()));

        mapOfAll.put(sunCoordinates, sun);
        mapOfAll.put(moonCoordinates, moon);

        planets = new ArrayList<>();
        //List<Double> planetCoordinates = new ArrayList<>();
        int i = 0;
        for (PlanetModel planet : PlanetModel.ALL) {
            if (planet != PlanetModel.EARTH) {
                Planet newPlanet = planet.at(daysUntilJ2010, conversionToEqu);
                planets.add(newPlanet);

                CartesianCoordinates planetProjection = projection.apply(conversionToHor.apply(newPlanet.equatorialPos()));
                planetPositions[i++] = planetProjection.x();
                planetPositions[i++] = planetProjection.y();

                mapOfAll.put(planetProjection, newPlanet);
            }
        }

        //TODO : Et comment on sait la position des étoiles ??????????????????????????????
    }

    public Sun sun() {
        return sun;
    }

    public CartesianCoordinates sunPosition() {
        return sunCoordinates;
    }

    public Moon moon() {
        return moon;
    }

    public CartesianCoordinates moonPosition() {
        return moonCoordinates;
    }

    public List<Planet> planets() {
        return List.copyOf(planets);
    }

    public double[] planetPositions() {
        return planetPositions;
    }

    public List<Star> stars() {
        return null;
    }

    public double[] starPositions() {
        return null;
    }

    public List<Asterism> asterisms() {
        return null;
    }

    public List<Integer> starIndexes() {
        return null;
    }

    public CelestialObject objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
        CartesianCoordinates coordOfNearest = null;
        double currentDistance;
        ClosedInterval intervalX = ClosedInterval.of(coordinates.x() - maxDistance, coordinates.x() + maxDistance);
        ClosedInterval intervalY = ClosedInterval.of(coordinates.y() - maxDistance, coordinates.y() + maxDistance);
        for (CartesianCoordinates coord : mapOfAll.keySet()) {
            if (intervalX.contains(coord.x()) && intervalY.contains(coord.y())) {
                //coordOfNearest = currentDistance > distance(coordinates, coord) ? coord : coordOfNearest;
            }
        }
        return mapOfAll.get(coordOfNearest);
    }

    private double distance(CartesianCoordinates coord1, CartesianCoordinates coord2) {
        return Math.sqrt(Math.pow(coord1.x() - coord2.x(), 2) + Math.pow(coord1.y() - coord2.y(), 2));
    }
}
