package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class SkyCanvasManager {

    public DoubleBinding mouseAzDeg;
    public DoubleBinding mouseAltDeg;
    public ObjectBinding<CelestialObject> objectUnderMouse;

    private static final ClosedInterval FIELD_OF_VIEW_BOUNDS = ClosedInterval.of(30, 150);
    private static final RightOpenInterval AZ_DEG_BOUNDS = RightOpenInterval.of(0, 360);
    private static final ClosedInterval ALT_DEG_BOUNDS = ClosedInterval.of(5, 90);

    private static final int MOVE_NORTH_SOUTH = 5;
    private static final int MOVE_EAST_WEST = 10;

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
        SkyCanvasPainter painter = new SkyCanvasPainter(skyCanvas);

        skyCanvas.setOnMouseMoved((mouse) -> mousePosition.set(new Point2D(mouse.getX(), mouse.getY())));

        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        observedSky = Bindings.createObjectBinding(() ->
                        new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(),
                                projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(), projection);

        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation = dilatation(skyCanvas, projection.get(), viewingParametersBean.getFieldOfViewDeg());
                    return Transform.affine(dilatation, 0, 0, -dilatation, skyCanvas.getWidth() / 2, skyCanvas.getHeight() / 2);
                },
                skyCanvas.widthProperty(), skyCanvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty());

        objectUnderMouse = Bindings.createObjectBinding(() -> {
            try {
                Point2D mousePoint = planeToCanvas.get().inverseTransform(mousePosition.get());
                if (observedSky.get().objectClosestTo(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()), planeToCanvas.get().inverseDeltaTransform(10, 0).getX()).isPresent()) {
                    return observedSky.get().objectClosestTo(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()), planeToCanvas.get().inverseDeltaTransform(10, 0).getX()).get();
                } else {
                    return null;
                }
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, observedSky, mousePosition, planeToCanvas);

        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
            try {
                Point2D mousePoint = planeToCanvas.get().inverseTransform(mousePosition.get());
                return projection.get().inverseApply(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()));
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, projection, planeToCanvas, mousePosition);

        mouseAltDeg = Bindings.createDoubleBinding(() -> {
            try {
                return mouseHorizontalPosition.get().altDeg();
            } catch (NullPointerException e) {
                return 0.0; //TODO: je ne sais pas si c'est la meilleure des moyens
            }
        }, mouseHorizontalPosition);
        mouseAzDeg = Bindings.createDoubleBinding(() -> {
            try {
                return mouseHorizontalPosition.get().azDeg();
            } catch (NullPointerException e) {
                return 0.0;
            }
        }, mouseHorizontalPosition);

        skyCanvas.setOnMousePressed((mousePress) -> {
            if (mousePress.isPrimaryButtonDown())
                skyCanvas.requestFocus();
        });

        skyCanvas.setOnKeyPressed((key) -> {
            key.consume(); //Avoids to go to the top control bar when pressing UP
            HorizontalCoordinates coord = viewingParametersBean.getCenter();
            switch (key.getCode()) {
                case UP:
                    if (ALT_DEG_BOUNDS.contains(coord.altDeg() + MOVE_NORTH_SOUTH))
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg(), coord.altDeg() + MOVE_NORTH_SOUTH));
                    break;
                case DOWN:
                    if (ALT_DEG_BOUNDS.contains(coord.altDeg() - MOVE_NORTH_SOUTH))
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg(), coord.altDeg() - MOVE_NORTH_SOUTH));
                    break;
                case LEFT:
                    //TODO les else font des trucs bizarres
                    if (AZ_DEG_BOUNDS.contains(coord.azDeg() - MOVE_EAST_WEST))
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg() - MOVE_EAST_WEST, coord.altDeg()));
                    else
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZ_DEG_BOUNDS.reduce(coord.azDeg() - MOVE_EAST_WEST), coord.altDeg()));
                    break;
                case RIGHT:
                    if (AZ_DEG_BOUNDS.contains(coord.azDeg() + MOVE_EAST_WEST))
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg() + MOVE_EAST_WEST, coord.altDeg()));
                    else
                        viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZ_DEG_BOUNDS.reduce(coord.azDeg() + MOVE_EAST_WEST), coord.altDeg()));
                    break;
            }
        });

        skyCanvas.setOnScroll((scroll) -> {
            double initFOW = viewingParametersBean.getFieldOfViewDeg();
            if (Math.abs(scroll.getDeltaX()) >= Math.abs(scroll.getDeltaY()) && FIELD_OF_VIEW_BOUNDS.contains(initFOW - scroll.getDeltaX()))
                viewingParametersBean.setFieldOfViewDeg(initFOW - scroll.getDeltaX());
            else if (FIELD_OF_VIEW_BOUNDS.contains(initFOW - scroll.getDeltaY()))
                viewingParametersBean.setFieldOfViewDeg(initFOW - scroll.getDeltaY());
        });

        planeToCanvas.addListener((o, oV, nV) -> painter.drawAll(observedSky.get(), projection.get(), nV));
        observedSky.addListener((o, oV, nV) -> painter.drawAll(nV, projection.get(), planeToCanvas.get()));
    }

    public Canvas canvas() {
        return skyCanvas;
    }

    public Number getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    public DoubleBinding mouseAzDegProperty() {
        return mouseAzDeg;
    }

    public Number getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    public DoubleBinding mouseAltDegProperty() {
        return mouseAltDeg;
    }

    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.get();
    }

    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    private double dilatation(Canvas canvas, StereographicProjection projection, double fieldOfViewDeg) {
        return canvas.getWidth() / projection.applyToAngle(Angle.ofDeg(fieldOfViewDeg));
    }
}
