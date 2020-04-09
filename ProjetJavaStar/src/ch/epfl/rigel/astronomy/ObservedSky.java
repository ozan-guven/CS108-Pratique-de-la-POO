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
    private List<Star> stars;

    private final CartesianCoordinates sunCoordinates;
    private final CartesianCoordinates moonCoordinates;
    private final double[] planetPositions = new double[14];

    private Map<CelestialObject, CartesianCoordinates> mapOfAll;

    public ObservedSky(ZonedDateTime time, GeographicCoordinates coordinates,
                       StereographicProjection projection, StarCatalogue catalogue) {
        this.time = time;
        this.coordinates = coordinates;
        this.projection = projection;
        this.catalogue = catalogue;
        this.stars = this.catalogue.stars();

        double daysUntilJ2010 = Epoch.J2010.daysUntil(time);
        EclipticToEquatorialConversion conversionToEqu = new EclipticToEquatorialConversion(time);
        EquatorialToHorizontalConversion conversionToHor = new EquatorialToHorizontalConversion(time, coordinates);

        mapOfAll = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, conversionToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, conversionToEqu);

        sunCoordinates = projection.apply(conversionToHor.apply(sun.equatorialPos()));
        moonCoordinates = projection.apply(conversionToHor.apply(moon.equatorialPos()));

        mapOfAll.put(sun, sunCoordinates);
        mapOfAll.put(moon, moonCoordinates);

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

                mapOfAll.put(newPlanet, planetProjection);
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
        return stars;
    }

    public double[] starPositions() {
        return null;
    }

    public List<Asterism> asterisms() {
        return new ArrayList<>(catalogue.asterisms());
    }

    public List<Integer> starIndexes() {
        return null;
    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
        double currentDistance = coordinates.distanceToSquared(sunCoordinates); //Initialises a first distance
        double nextDistance;
        CelestialObject nearestObject = null;
        CartesianCoordinates coordOfObject;
        ClosedInterval intervalX = ClosedInterval.of(coordinates.x() - maxDistance, coordinates.x() + maxDistance);
        ClosedInterval intervalY = ClosedInterval.of(coordinates.y() - maxDistance, coordinates.y() + maxDistance);

        for (CelestialObject object : mapOfAll.keySet()) {
            coordOfObject = mapOfAll.get(object);
            //In order
            //Checks if the coordinates are in the given square with the two intervals (not to compute the distance all the times)
            //Computes and checks if the distance is in the given maxDistance
            //Checks if the new distance is smaller or equal to the current one
            if (coordOfObject.inPartOfPlane(intervalX, intervalY)
                    && (nextDistance = coordOfObject.distanceToSquared(coordinates)) <= maxDistance * maxDistance
                    && (currentDistance >= nextDistance)) {
                currentDistance = nextDistance;
                nearestObject = object;
                /*currentDistance = (distanceSquared(mapOfAll.get(object), coordinates) <= currentDistance)
                        ? distanceSquared(mapOfAll.get(nearestObject = object), coordinates) //Sets the current distance to the calculated one and sets the nearest object
                        : currentDistance; //The current distance remains the same*/
            }
        }
        return nearestObject == null ? Optional.empty() : Optional.of(nearestObject);
    }
}
