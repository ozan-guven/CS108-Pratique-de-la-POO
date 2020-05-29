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

    public double getLonDeg() {
        return lonDeg.get();
    }

    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    public double getLatDeg() {
        return latDeg.get();
    }

    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    public ObjectBinding<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    public void setCoordinates(GeographicCoordinates coordinates) {
        setLonDeg(coordinates.lonDeg());
        setLatDeg(coordinates.latDeg());
    }
}

/*//TODO TEST
class ObserverTest {
    public static void main(String[] args) {
        ObserverLocationBean bean = new ObserverLocationBean();
        bean.setLatDeg(20);
        System.out.println(bean.getCoordinates());
        bean.setLonDeg(35);
        System.out.println(bean.getCoordinates());
    }
}*/