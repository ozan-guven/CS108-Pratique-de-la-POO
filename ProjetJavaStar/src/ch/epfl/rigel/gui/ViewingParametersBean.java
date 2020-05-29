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
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    public ViewingParametersBean() {
        fieldOfViewDeg.set(0);
        center.set(null);
    }

    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
