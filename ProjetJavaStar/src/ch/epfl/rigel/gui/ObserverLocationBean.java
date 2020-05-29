package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A bean for the location of the observer
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class ObserverLocationBean {
    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    private final DoubleProperty latDeg = new SimpleDoubleProperty();
    private final ObjectBinding<GeographicCoordinates> coordinates;

    /**
     * Constructor of the observer location bean
     */
    public ObserverLocationBean() {
        coordinates = Bindings.createObjectBinding(() ->
                GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get()), lonDeg, latDeg);
    }

    /**
     * Getter for the longitude
     *
     * @return (double)
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Getter for the lonitude property
     *
     * @return (DoubleProperty)
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * Setter for the longitude
     *
     * @param lonDeg (double) New longitude to be used
     */
    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     * Getter for the latitude
     *
     * @return (double)
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Getter for the latitude property
     *
     * @return (DoubleProperty)
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * Setter for the latitude
     *
     * @param latDeg (double) New latitude to be used
     */
    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * Getter for the coordinates
     *
     * @return (GeographicCoordinates)
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    /**
     * Getter for the coordinates binding
     *
     * @return (ObjectBinding < GeographicCoordinates >)
     */
    public ObjectBinding<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Setter for the coordinates
     *
     * @param coordinates (GeographicCoordinates) New coordinates to be used
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        setLonDeg(coordinates.lonDeg());
        setLatDeg(coordinates.latDeg());
    }
}