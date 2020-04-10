package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

/**
 * Class used to draw the sky on the screen
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    public void clear() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] transformed = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformed, 0, sky.starPositions().length);

        int i = 0;
        for (Star star : sky.stars()) {
            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            ctx.fillOval(transformed[i++], transformed[i++],
                    diameterFromMagnitude(star, projection), diameterFromMagnitude(star, projection));
        }
    }

    private double diameterFromMagnitude(CelestialObject celestialObject, StereographicProjection projection) {
        ClosedInterval interval = ClosedInterval.of(-2, 5);
        double clippedMagnitude = interval.clip(celestialObject.magnitude());
        double factor = (99 - 17 * clippedMagnitude) / 140;
        return factor * 2 * Math.tan(projection.applyToAngle(Angle.ofDeg(0.5)) / 4);
    }
}
