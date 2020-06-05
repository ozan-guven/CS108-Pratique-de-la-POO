package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class SkyCanvasManager {

    public final DoubleBinding mouseAzDeg;
    public final DoubleBinding mouseAltDeg;
    public final ObjectBinding<CelestialObject> objectUnderMouse;

    public final BooleanProperty drawAsterisms = new SimpleBooleanProperty(true);
    public final BooleanProperty allowDayNightCycle = new SimpleBooleanProperty(false);
    public final BooleanProperty drawHorizontalGrid = new SimpleBooleanProperty(false);

    private static final ClosedInterval FIELD_OF_VIEW_BOUNDS = ClosedInterval.of(30, 150);
    private static final RightOpenInterval AZ_DEG_BOUNDS = RightOpenInterval.of(0, 360);
    private static final ClosedInterval ALT_DEG_BOUNDS = ClosedInterval.of(-90, 90);

    private static final int MOVE_NORTH_SOUTH = 5;
    private static final int MOVE_EAST_WEST = 10;

    private final ObjectBinding<StereographicProjection> projection;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> observedSky;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(new Point2D(0, 0));
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    private final ObjectBinding<HorizontalCoordinates> sunCoordinates;
    private final ObjectBinding<HorizontalCoordinates> moonCoordinates;
    private final ObjectBinding<List<HorizontalCoordinates>> planetsCoordinates;

    private final Canvas skyCanvas;


    private final ObjectProperty<Point2D> firstDrag = new SimpleObjectProperty<>(new Point2D(0, 0));
    private final ObjectProperty<HorizontalCoordinates> firstCenter = new SimpleObjectProperty<>(HorizontalCoordinates.of(0, 0));

    /**
     * SkyCanvasManager constructor
     *
     * @param catalogue             Catalogue of celestial objects to use to build the sky
     * @param dateTimeBean          Date and time of the observation
     * @param observerLocationBean  Location of the observation
     * @param viewingParametersBean The viewing parameters
     */
    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean) {
        skyCanvas = initiateSkyCanvas(viewingParametersBean);
        SkyCanvasPainter painter = new SkyCanvasPainter(skyCanvas);

        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        observedSky = initiateObservedSky(dateTimeBean, observerLocationBean, catalogue, painter);

        planeToCanvas = initiatePlaneToCanvas(viewingParametersBean, painter);

        objectUnderMouse = initiateObjectUnderMouse();

        mouseHorizontalPosition = initiateMouseHorizontalPosition();

        mouseAltDeg = Bindings.createDoubleBinding(() -> {
            try {
                return mouseHorizontalPosition.get().altDeg();
            } catch (NullPointerException e) {
                return 0.0;
            }
        }, mouseHorizontalPosition);

        mouseAzDeg = Bindings.createDoubleBinding(() -> {
            try {
                return mouseHorizontalPosition.get().azDeg();
            } catch (NullPointerException e) {
                return 0.0;
            }
        }, mouseHorizontalPosition);

        drawAsterisms.addListener((o, oV, nV) -> painter.drawAll(observedSky.get(), projection.get(), planeToCanvas.get(), nV, allowDayNightCycle.get(), drawHorizontalGrid.get()));
        allowDayNightCycle.addListener((o, oV, nV) -> painter.drawAll(observedSky.get(), projection.get(), planeToCanvas.get(), drawAsterisms.get(), nV, drawHorizontalGrid.get()));
        drawHorizontalGrid.addListener((o, oV, nV) -> painter.drawAll(observedSky.get(), projection.get(), planeToCanvas.get(), drawAsterisms.get(), allowDayNightCycle.get(), nV));

        sunCoordinates = Bindings.createObjectBinding(() -> observedSky.get().sunHorizontalCoordinates(), observedSky);
        moonCoordinates = Bindings.createObjectBinding(() -> observedSky.get().moonHorizontalCoordinates(), observedSky);
        planetsCoordinates = Bindings.createObjectBinding(() -> observedSky.get().planetsHorizontalCoordinates(), observedSky);
    }

    /**
     * Getter for the canvas
     *
     * @return (Canvas)
     */
    public Canvas canvas() {
        return skyCanvas;
    }

    /**
     * Getter fot the azimuth of the mouse
     *
     * @return (Number)
     */
    public Number getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * Getter for the mouse azimuth property
     *
     * @return (DoubleBinding)
     */
    public DoubleBinding mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Getter for the altitude of the mouse
     *
     * @return (Number)
     */
    public Number getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    /**
     * Getter for the mouse altitude property
     *
     * @return (DoubleBinding)
     */
    public DoubleBinding mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * Getter for the celestial object under the mouse
     *
     * @return (CelestialObject)
     */
    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.get();
    }

    /**
     * Getter for the draw asterisms boolean
     *
     * @return draw asterisms boolean
     */
    public boolean isDrawAsterisms() {
        return drawAsterisms.get();
    }

    /**
     * Getter for the draw asterisms property
     *
     * @return the draw asterisms property
     */
    public BooleanProperty drawAsterismsProperty() {
        return drawAsterisms;
    }

    /**
     * Getter for the draw horizontal grid boolean
     *
     * @return the draw horizontal grid boolean
     */
    public boolean isDrawHorizontalGrid() {
        return drawHorizontalGrid.get();
    }

    /**
     * Getter for the draw horizontal grid property
     *
     * @return the draw horizontal grid property
     */
    public BooleanProperty drawHorizontalGridProperty() {
        return drawHorizontalGrid;
    }

    /**
     * Getter for the day/night cycle property
     *
     * @return the day/night cycle property
     */
    public BooleanProperty allowDayNightCycleProperty() {
        return allowDayNightCycle;
    }

    /**
     * Getter for the celestial object under the mouse property
     *
     * @return (ObjectBinding < CelestialObject >)
     */
    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * Gets sun's horizontal coordinate
     *
     * @return sun's horizontal coordinate
     */
    public HorizontalCoordinates getSunCoordinates() {
        return sunCoordinates.get();
    }

    /**
     * Gets Moon's horizontal coordinate
     *
     * @return Moon's horizontal coordinate
     */
    public HorizontalCoordinates getMoonCoordinates() {
        return moonCoordinates.get();
    }

    /**
     * Gets a list containing the horizontal coordinates of each planets
     *
     * @return a list containing the horizontal coordinates of each planet
     */
    public List<HorizontalCoordinates> getPlanetsCoordinates() {
        return Collections.unmodifiableList(planetsCoordinates.get());
    }

    private Canvas initiateSkyCanvas(ViewingParametersBean viewingParametersBean) {
        Canvas skyCanvas = new Canvas();

        skyCanvas.setOnMouseMoved((mouse) -> mousePosition.set(new Point2D(mouse.getX(), mouse.getY())));

        skyCanvas.setOnMousePressed((mousePress) -> {
            if (mousePress.isPrimaryButtonDown())
                skyCanvas.requestFocus();
            firstDrag.set(new Point2D(mousePress.getX(), mousePress.getY()));
            firstCenter.set(viewingParametersBean.getCenter());
            canvas().setCursor(Cursor.MOVE);
        });

        skyCanvas.setOnMouseReleased(mouseRelease -> skyCanvas.setCursor(Cursor.DEFAULT));

        skyCanvas.setOnMouseDragged(dragEvent -> {
            double dragX = dragEvent.getX();
            double dragY = dragEvent.getY();
            try {
                Point2D firstInverse = planeToCanvas.get().inverseTransform(firstDrag.get().getX(), firstDrag.get().getY());
                HorizontalCoordinates firstCoord = projection.get().inverseApply(CartesianCoordinates.of(firstInverse.getX(), firstInverse.getY()));
                Point2D inverse = planeToCanvas.get().inverseTransform(dragX, dragY);
                HorizontalCoordinates coord = projection.get().inverseApply(CartesianCoordinates.of(inverse.getX(), inverse.getY()));
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(
                        AZ_DEG_BOUNDS.reduce(firstCenter.get().azDeg() - coord.azDeg() + firstCoord.azDeg()),
                        ALT_DEG_BOUNDS.clip(firstCenter.get().altDeg() - coord.altDeg() + firstCoord.altDeg())));
            } catch (NonInvertibleTransformException ignored) {
            }
        });

        skyCanvas.setOnKeyPressed((key) -> {
            key.consume(); //Avoids to go to the top control bar when pressing UP
            HorizontalCoordinates coord = viewingParametersBean.getCenter();
            switch (key.getCode()) {
                case UP:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg(), ALT_DEG_BOUNDS.clip(coord.altDeg() + MOVE_NORTH_SOUTH)));
                    break;
                case DOWN:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(coord.azDeg(), ALT_DEG_BOUNDS.clip(coord.altDeg() - MOVE_NORTH_SOUTH)));
                    break;
                case LEFT:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZ_DEG_BOUNDS.reduce(coord.azDeg() - MOVE_EAST_WEST), coord.altDeg()));
                    break;
                case RIGHT:
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZ_DEG_BOUNDS.reduce(coord.azDeg() + MOVE_EAST_WEST), coord.altDeg()));
                    break;
            }
        });

        skyCanvas.setOnScroll((scroll) -> {
            double initFOW = viewingParametersBean.getFieldOfViewDeg();
            if (Math.abs(scroll.getDeltaX()) >= Math.abs(scroll.getDeltaY()))
                viewingParametersBean.setFieldOfViewDeg(FIELD_OF_VIEW_BOUNDS.clip(initFOW - scroll.getDeltaX()));
            else
                viewingParametersBean.setFieldOfViewDeg(FIELD_OF_VIEW_BOUNDS.clip(initFOW - scroll.getDeltaY()));
        });

        return skyCanvas;
    }

    private ObjectBinding<ObservedSky> initiateObservedSky(DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, StarCatalogue catalogue, SkyCanvasPainter painter) {
        ObjectBinding<ObservedSky> observedSky = Bindings.createObjectBinding(() ->
                        new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(),
                                projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(), projection);

        observedSky.addListener((o, oV, nV) -> painter.drawAll(nV, projection.get(), planeToCanvas.get(), drawAsterisms.get(), allowDayNightCycle.get(), drawHorizontalGrid.get()));

        return observedSky;
    }

    private ObjectBinding<Transform> initiatePlaneToCanvas(ViewingParametersBean viewingParametersBean, SkyCanvasPainter painter) {
        ObjectBinding<Transform> planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation = dilatation(skyCanvas, projection.get(), viewingParametersBean.getFieldOfViewDeg());
                    return Transform.affine(dilatation, 0, 0, -dilatation, skyCanvas.getWidth() / 2, skyCanvas.getHeight() / 2);
                },
                skyCanvas.widthProperty(), skyCanvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty());

        planeToCanvas.addListener((o, oV, nV) -> painter.drawAll(observedSky.get(), projection.get(), nV, drawAsterisms.get(), allowDayNightCycle.get(), drawHorizontalGrid.get()));

        return planeToCanvas;
    }

    private ObjectBinding<CelestialObject> initiateObjectUnderMouse() {
        return Bindings.createObjectBinding(() -> {
            try {
                Point2D mousePoint = planeToCanvas.get().inverseTransform(mousePosition.get());
                Optional<CelestialObject> objectClosest = observedSky.get().objectClosestTo(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()), planeToCanvas.get().inverseDeltaTransform(10, 0).getX());
                return objectClosest.orElse(null);
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, observedSky, mousePosition, planeToCanvas);
    }

    private ObjectBinding<HorizontalCoordinates> initiateMouseHorizontalPosition() {
        return Bindings.createObjectBinding(() -> {
            try {
                Point2D mousePoint = planeToCanvas.get().inverseTransform(mousePosition.get());
                return projection.get().inverseApply(CartesianCoordinates.of(mousePoint.getX(), mousePoint.getY()));
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, projection, planeToCanvas, mousePosition);
    }

    private double dilatation(Canvas canvas, StereographicProjection projection, double fieldOfViewDeg) {
        return canvas.getWidth() / projection.applyToAngle(Angle.ofDeg(fieldOfViewDeg));
    }
}
