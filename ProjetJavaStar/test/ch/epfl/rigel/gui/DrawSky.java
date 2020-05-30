package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;

public final class DrawSky extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    String AST_CATALOGUE_NAME = "/asterisms.txt";
    StarCatalogue catalogue;
    StarCatalogue.Builder builder;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*try (InputStream hs = resourceStream("/hygdata_v3.csv")){
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .build();*/
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME);
             InputStream astStream = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
            builder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
            catalogue = builder.loadFrom(astStream, AsterismLoader.INSTANCE).build();
        }

        ZonedDateTime when =
                ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
        GeographicCoordinates where =
                GeographicCoordinates.ofDeg(6.57, 46.52);
        HorizontalCoordinates projCenter =
                //HorizontalCoordinates.ofDeg(180, 45);                  //Original center
                //HorizontalCoordinates.ofDeg(277, -23);                 //Center for sun
                //HorizontalCoordinates.ofDeg(3.7, -65);                 //Center for Moon
                HorizontalCoordinates.ofDeg(0, 23);                    //Center for Ursa Minor Polaris
        //HorizontalCoordinates.ofDeg(0, 90);                    //Center for whole horizon
        StereographicProjection projection =
                new StereographicProjection(projCenter);
        ObservedSky sky =
                new ObservedSky(when, where, projection, catalogue);

        Canvas canvas =
                new Canvas(800, 600);
        Transform planeToCanvas =
                Transform.affine(1300, 0, 0, -1300, 400, 300); //Normal transform
        //Transform.affine(260, 0, 0, -260, 400, 300); //For whole horizon
        SkyCanvasPainter painter =
                new SkyCanvasPainter(canvas);

        painter.clear();
        painter.drawStars(sky, projection, planeToCanvas, true);
        painter.drawPlanets(sky, projection, planeToCanvas); //Draws the planets
        painter.drawSun(sky, projection, planeToCanvas); //Draws the sun
        painter.drawMoon(sky, projection, planeToCanvas);//Draws the moon
        painter.drawHorizon(projection, planeToCanvas); //Draws the horizon

        WritableImage fxImage =
                canvas.snapshot(null, null);
        BufferedImage swingImage =
                SwingFXUtils.fromFXImage(fxImage, null);
        //ImageIO.write(swingImage, "png", new File("sky.png"));
        //ImageIO.write(swingImage, "png", new File("skyAsterisms.png"));
        //ImageIO.write(swingImage, "png", new File("skySun.png"));
        //ImageIO.write(swingImage, "png", new File("skyHorizon.png"));
        //ImageIO.write(swingImage, "png", new File("skyMoon.png"));
        ImageIO.write(swingImage, "png", new File("skyUrsaMinorPolaris.png"));
        //ImageIO.write(swingImage, "png", new File("skyWholeHorizon.png"));
        //}
        Platform.exit();
    }
}