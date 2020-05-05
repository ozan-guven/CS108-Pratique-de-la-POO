package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

public class SkyCanvasManager {

    public DoubleBinding mouseAzDeg;
    public DoubleBinding mouseAltDeg;
    public ObjectBinding<CelestialObject> objectUnderMouse;

    private ObjectBinding<StereographicProjection> projection;
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky;
    private ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(new Point2D(0, 0));
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    private Canvas skyCanvas;

    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean) {

        skyCanvas = new Canvas();

        skyCanvas.setOnMouseMoved((mouse) -> mousePosition.set(new Point2D(mouse.getX(), mouse.getY())));

        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        observedSky = Bindings.createObjectBinding(() ->
                        new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(),
                                projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(), projection);

        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation = dilatation(skyCanvas, viewingParametersBean.getFieldOfViewDeg());
                    return Transform.affine(dilatation, 0, 0, -dilatation, skyCanvas.getWidth() / 2, skyCanvas.getHeight() / 2);
                },
                skyCanvas.widthProperty(), skyCanvas.heightProperty(), projection);

        objectUnderMouse = Bindings.createObjectBinding(() ->
                observedSky.get().objectClosestTo(
                        CartesianCoordinates.of(mousePosition.get().getX(), mousePosition.get().getY()), 10)
                        .get(), observedSky, mousePosition);

        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    Point2D mousePoint = planeToCanvas.get().inverseTransform(mousePosition.get());
                    return projection.get().inverseApply(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()));
                }, projection, planeToCanvas, mousePosition);

        mouseAltDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition);
        mouseAzDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.get().azDeg(), mouseHorizontalPosition);

        SkyCanvasPainter painter = new SkyCanvasPainter(skyCanvas);
        painter.clear();
        observedSky.addListener((o, oV, nV) -> {
            painter.drawStars(nV, projection.get(), planeToCanvas.get());
            painter.drawPlanets(nV, projection.get(), planeToCanvas.get()); //Draws the planets
            painter.drawSun(nV, projection.get(), planeToCanvas.get()); //Draws the sun
            painter.drawMoon(nV, projection.get(), planeToCanvas.get());//Draws the moon
            painter.drawHorizon(projection.get(), planeToCanvas.get()); //Draws the horizon
        });
    }

    public Canvas canvas() {
        return skyCanvas;
    }

    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.get();
    }

    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    private double dilatation(Canvas canvas, double fieldOfViewDeg) {
        return canvas.getWidth() / (2 * Math.tan(Angle.ofDeg(fieldOfViewDeg) / 4));
    }
}
