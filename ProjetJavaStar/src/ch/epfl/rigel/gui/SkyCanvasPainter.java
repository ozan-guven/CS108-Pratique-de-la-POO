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

/**
 * Class used to draw the sky on the screen
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext ctx;

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
     * Clears the canvas and sets its color to black
     */
    public void clear() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Method that draws the asterisms onto the canvas
     *
     * @param sky         the observed sky containing the asterisms
     * @param transformed the transformed coordinates of all
     *                    the stars contained in the observed sky
     */
    private void drawAsterisms(ObservedSky sky, double[] transformed) {
        ctx.setLineWidth(1);
        ctx.setStroke(Color.BLUE);
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
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] transformed = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformed, 0, sky.starPositions().length / 2);

        drawAsterisms(sky, transformed); //TODO : To add later as a bonus : user could choose whether to show the asterisms or not

        int i = 0;
        double z;
        for (Star star : sky.stars()) {
            z = planeToCanvas.deltaTransform(diameterFromMagnitude(star.magnitude(), projection), 0).getX();
            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            ctx.fillOval(transformed[i++] - z / 2, transformed[i++] - z / 2, z, z);
        }
    }

    /**
     * Draws the 7 planets onto the canvas
     *
     * @param sky           the observed sky
     * @param projection    the projection used
     * @param planeToCanvas the affine transformation to transform from
     *                      cartesian coordinates to the coordinates of the screen
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] planetCoordinates = new double[sky.planetPositions().length];
        planeToCanvas.transform2DPoints(sky.planetPositions(), 0, planetCoordinates, 0, sky.planetPositions().length / 2);

        int i = 0;
        double diameter;
        ctx.setFill(Color.LIGHTGRAY);
        for (Planet planet : sky.planets()) {
            diameter = planeToCanvas.deltaTransform(diameterFromMagnitude(planet.magnitude(), projection), 0).getX();
            //TODO : later ctx.setFill(planet.color());
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

        ctx.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.25));
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
        //TODO : Comme bonus on pourrait choisir d'afficher ou non l'horizon ?
        //Draws the horizon
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        HorizontalCoordinates coordForHorizon = HorizontalCoordinates.of(0, 0);

        double circleRadius = planeToCanvas.deltaTransform(projection.circleRadiusForParallel(coordForHorizon) * 2, 0).getX();
        CartesianCoordinates circleCenter = projection.circleCenterForParallel(coordForHorizon);
        Point2D circlePoint = planeToCanvas.transform(circleCenter.x(), circleCenter.y());

        ctx.strokeOval(circlePoint.getX() - circleRadius / 2, circlePoint.getY() - circleRadius / 2, circleRadius, circleRadius);

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
    public void drawAll(ObservedSky observedSky, StereographicProjection projection, Transform planeToCanvas) {
        clear();
        drawStars(observedSky, projection, planeToCanvas);
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
        ClosedInterval interval = ClosedInterval.of(-2, 5);
        double clippedMagnitude = interval.clip(magnitude);
        double factor = (99 - 17 * clippedMagnitude) / 140.0;
        return factor * projection.applyToAngle(Angle.ofDeg(0.5));
    }
}
