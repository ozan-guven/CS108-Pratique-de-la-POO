package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.function.Function;

/**
 * Class used to draw the sky on the screen
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class SkyCanvasPainter {

    private static final double SUN_HALO_OPACITY_FACTOR = 0.25;
    //private static final ClosedInterval INTERVAL_FOR_DUSK_DAWN = ClosedInterval.of(-90, 2.976047);
    private static final ClosedInterval INTERVAL_FOR_DIAMETER = ClosedInterval.of(-2, 5);
    //private static final ClosedInterval INTERVAL_FOR_GREEN = ClosedInterval.of(0, 127);
    private static final double TOLERANCE_CONSTANT = 1E12;
    //private static final Polynomial POLYNOMIAL_FOR_RED = Polynomial.of(-227d/16d, 227d/8, 2405d/16);
    //private static final Polynomial POLYNOMIAL_FOR_GREEN = Polynomial.of(-21d/2d, 21, 315d/2d);

    private final Canvas canvas;
    private final GraphicsContext ctx;

    private SkyColorManager skyColorManager; //Initialized in clear as it's the first method called

    /**
     * Initializes the sky painter
     *
     * @param canvas the canvas to draw onto
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Clears the canvas and sets the color of the sky
     *
     * @param sky                the current observed sky
     * @param allowDayNightCycle if the day night cycle is wanted
     */
    public void clear(ObservedSky sky, boolean allowDayNightCycle) {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        skyColorManager = new SkyColorManager(sky, allowDayNightCycle);
        ctx.setFill(skyColorManager.setSkyColor());
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Draws the grid of horizontal coordinates
     *
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawGrid(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(skyColorManager.deriveColor(Color.LIGHTGRAY));
        ctx.setLineWidth(0.15);
        // latitudes
        for (int i = -9; i < 10; i++) {
            HorizontalCoordinates coordForHorizon = HorizontalCoordinates.ofDeg(0, 10 * i);
            drawGridLines(projection::circleRadiusForParallel, projection::circleCenterForParallel, coordForHorizon, planeToCanvas);
        }
        // longitudes
        for (int j = 0; j < 36; j++) {
            HorizontalCoordinates coordForHorizon = HorizontalCoordinates.ofDeg(10 * j, 0);
            drawGridLines(projection::circleRadiusForMeridian, projection::circleCenterForMeridian, coordForHorizon, planeToCanvas);
        }
    }

    private void drawGridLines(Function<HorizontalCoordinates, Double> circleRadius, Function<HorizontalCoordinates, CartesianCoordinates> circleCenter, HorizontalCoordinates coordForHorizon, Transform planeToCanvas) {
        double diameter = planeToCanvas.deltaTransform(circleRadius.apply(coordForHorizon) * 2, 0).getX();
        CartesianCoordinates center = circleCenter.apply(coordForHorizon);
        Point2D circlePoint = planeToCanvas.transform(center.x(), center.y());
        ctx.strokeOval(circlePoint.getX() - diameter / 2, circlePoint.getY() - diameter / 2, diameter, diameter);
    }

    /**
     * Method that draws the asterisms onto the canvas
     *
     * @param sky         the observed sky containing the asterisms
     * @param transformed the transformed coordinates of all
     */
    private void drawAsterisms(ObservedSky sky, double[] transformed) {
        ctx.setLineWidth(1);
        ctx.setStroke(skyColorManager.deriveColor(Color.BLUE));
        List<Integer> asterismIndices;
        int currentStar, nextStar;
        for (Asterism asterism : sky.asterisms()) {
            asterismIndices = sky.asterismIndices(asterism);
            ctx.beginPath();
            for (int i = 0; i < asterismIndices.size() - 1; i++) {
                currentStar = 2 * asterismIndices.get(i);
                nextStar = 2 * asterismIndices.get(i + 1);
                if (canvas.getBoundsInLocal().contains(transformed[currentStar], transformed[currentStar + 1])
                        || canvas.getBoundsInLocal().contains(transformed[nextStar], transformed[nextStar + 1])) { //Checks if there is a path outside the canvas
                    ctx.moveTo(transformed[currentStar], transformed[currentStar + 1]);
                    ctx.lineTo(transformed[nextStar], transformed[nextStar + 1]);
                }
            }
            ctx.closePath();
            ctx.stroke();
        }
    }

    /**
     * Draws the asterisms and the stars onto the canvas
     *
     * @param sky           the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas, boolean drawAsterisms) {
        double[] transformed = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformed, 0, sky.starPositions().length / 2);

        if (drawAsterisms) drawAsterisms(sky, transformed);

        int i = 0;
        double starDiameter;
        for (Star star : sky.stars()) {
            starDiameter = planeToCanvas.deltaTransform(diameterFromMagnitude(star.magnitude(), projection), 0).getX();
            ctx.setFill(skyColorManager.deriveColor(BlackBodyColor.colorForTemperature(star.colorTemperature())));
            ctx.fillOval(transformed[i++] - starDiameter / 2, transformed[i++] - starDiameter / 2, starDiameter, starDiameter);
        }
    }

    /**
     * Draws the 7 planets onto the canvas
     *
     * @param sky           the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] planetCoordinates = new double[sky.planetPositions().length];
        planeToCanvas.transform2DPoints(sky.planetPositions(), 0, planetCoordinates, 0, sky.planetPositions().length / 2);

        int i = 0;
        double diameter;
        for (Planet planet : sky.planets()) {
            ctx.setFill(skyColorManager.deriveColor(Color.web(planet.color)));
            diameter = planeToCanvas.deltaTransform(diameterFromMagnitude(planet.magnitude(), projection), 0).getX();
            ctx.fillOval(planetCoordinates[i++] - diameter / 2, planetCoordinates[i++] - diameter / 2, diameter, diameter);
        }
    }

    /**
     * Draws the sun onto the canvas
     *
     * @param sky           the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Sun sun = sky.sun();
        Point2D sunCoordinates = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());
        double sunX = sunCoordinates.getX();
        double sunY = sunCoordinates.getY();

        double sunDiameter = planeToCanvas.deltaTransform(projection.applyToAngle(sun.angularSize()), 0).getX();
        double sunSecondDiameter = sunDiameter + 2;
        double sunHaloDiameter = sunDiameter * 2.2;

        ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, SUN_HALO_OPACITY_FACTOR));
        ctx.fillOval(sunX - sunHaloDiameter / 2, sunY - sunHaloDiameter / 2, sunHaloDiameter, sunHaloDiameter);

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(sunX - sunSecondDiameter / 2, sunY - sunSecondDiameter / 2, sunSecondDiameter, sunSecondDiameter);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(sunX - sunDiameter / 2, sunY - sunDiameter / 2, sunDiameter, sunDiameter);
    }

    /**
     * Draws the moon on the canvas
     *
     * @param sky           the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        Moon moon = sky.moon();
        Point2D moonCoordinates = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        double moonX = moonCoordinates.getX();
        double moonY = moonCoordinates.getY();

        double diameter = planeToCanvas.deltaTransform(projection.applyToAngle(moon.angularSize()), 0).getX();

        ctx.setFill(Color.WHITE);
        ctx.fillOval(moonX - diameter / 2, moonY - diameter / 2, diameter, diameter);
    }

    /**
     * Draws the horizon line and the cardinal and
     * intercardinal points onto the canvas
     *
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        //Draws the horizon
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        HorizontalCoordinates coordForHorizon = HorizontalCoordinates.of(0, 0);

        double circleRadiusForParallel = projection.circleRadiusForParallel(coordForHorizon);
        if (circleRadiusForParallel > TOLERANCE_CONSTANT) {
            ctx.strokeLine(0, canvas.getHeight() / 2d, canvas.getWidth(), canvas.getHeight() / 2d);
        } else {
            double circleRadius = planeToCanvas.deltaTransform(circleRadiusForParallel * 2, 0).getX();
            CartesianCoordinates circleCenter = projection.circleCenterForParallel(coordForHorizon);
            Point2D circlePoint = planeToCanvas.transform(circleCenter.x(), circleCenter.y());

            ctx.strokeOval(circlePoint.getX() - circleRadius / 2, circlePoint.getY() - circleRadius / 2, circleRadius, circleRadius);
        }

        //Draws the cardinal points
        ctx.setFill(Color.RED);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.TOP);

        HorizontalCoordinates coordOfCardinal;
        CartesianCoordinates coordOnProjection;
        Point2D cardinalPoint;
        for (int i = 0; i < 8; i++) {
            coordOfCardinal = HorizontalCoordinates.ofDeg(i * 45, -0.5);
            coordOnProjection = projection.apply(coordOfCardinal);
            cardinalPoint = planeToCanvas.transform(coordOnProjection.x(), coordOnProjection.y());
            ctx.fillText(coordOfCardinal.azOctantName("N", "E", "S", "O"), cardinalPoint.getX(), cardinalPoint.getY());
        }
    }

    /**
     * Draws all of the sky (drawStar, drawPlanets, drawSun, drawMoon, drawHorizon)
     *
     * @param observedSky   the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the plane to canvas transform used
     */
    public void drawAll(ObservedSky observedSky, StereographicProjection projection, Transform planeToCanvas, boolean drawAsterisms, boolean allowDayNightCycle, boolean drawHorizontalGrid) {
        clear(observedSky, allowDayNightCycle);
        if (drawHorizontalGrid) drawGrid(projection, planeToCanvas); //Draws a grid for horizontal coordinates if wanted
        drawStars(observedSky, projection, planeToCanvas, drawAsterisms); //Draws the stars and draws the asterisms if wanted
        drawPlanets(observedSky, projection, planeToCanvas); //Draws the planets
        drawSun(observedSky, projection, planeToCanvas); //Draws the sun
        drawMoon(observedSky, projection, planeToCanvas);//Draws the moon
        drawHorizon(projection, planeToCanvas); //Draws the horizon
    }

    /**
     * Method to compute the diameter of a celestial object given it's magnitude.<br>
     * (scale to an object with angular size equal to 0.5°)
     *
     * @param magnitude  the magnitude of the object
     * @param projection the projection to be used
     * @return the diameter from the magnitude
     */
    private double diameterFromMagnitude(double magnitude, StereographicProjection projection) {
        double clippedMagnitude = INTERVAL_FOR_DIAMETER.clip(magnitude);
        double factor = (99 - 17 * clippedMagnitude) / 140.0;
        return factor * projection.applyToAngle(Angle.ofDeg(0.5));
    }
}
