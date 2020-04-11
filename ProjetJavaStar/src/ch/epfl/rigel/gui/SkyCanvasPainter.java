package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.List;

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
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] transformed = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformed, 0, sky.starPositions().length / 2);

        ctx.setFill(Color.BLUE);
        ctx.setLineWidth(1);

        int i = 0;
        double z;
        for (Star star : sky.stars()) {
            z = planeToCanvas.deltaTransform(diameterFromMagnitude(star.magnitude(), projection), 0).getX();
            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            ctx.fillOval(transformed[i++] - z / 2, transformed[i++] - z / 2, z, z);
        }
    }

    private double diameterFromMagnitude(double magnitude, StereographicProjection projection) {
        ClosedInterval interval = ClosedInterval.of(-2, 5);
        double clippedMagnitude = interval.clip(magnitude);
        double factor = (99 - 17 * clippedMagnitude) / 140.0;
        return factor * projection.applyToAngle(Angle.ofDeg(0.5));
    }
}
