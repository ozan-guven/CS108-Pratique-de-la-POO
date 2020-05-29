package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A bean for the viewing parameters like :
 * <ul>
 *     <li>the field of view</li>
 *     <li>the center of the projection</li>
 * </ul>
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class ViewingParametersBean {
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty(0);
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    /**
     * Getter for the field of view
     *
     * @return (double)
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * Getter for the field of view property
     *
     * @return (DoubleProperty)
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Setter for the field of view
     *
     * @param fieldOfViewDeg The degree of the field of view
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * Getter for the center
     *
     * @return (HorizontalCoordinates)
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Getter for the center property
     *
     * @return (ObjectProperty < HorizontalCoordinates >)
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Setter for the center
     *
     * @param center (HorizontalCoordinates) New center to be set
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
