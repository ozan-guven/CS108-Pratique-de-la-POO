package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Main extends Application {

    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //TODO: ATENTION FAUT MATTRE FINAL A TOTES LES PROPRIETES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private static final int MIN_WIDTH_STAGE = 800;
    private static final int MIN_HEIGHT_STAGE = 600;
    private static final GeographicCoordinates DEFAULT_OBSERVATION_COORDINATES =
            GeographicCoordinates.ofDeg(6.57, 46.52);
    private static final HorizontalCoordinates DEFAULT_CENTER_FOR_VIEW =
            HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private static final int DEFAULT_FIELD_OF_VIEW = 100;
    private static final String ROLL_BACK = "\uf0e2";
    private static final String PLAY = "\uf04b";
    private static final String PAUSE = "\uf04c";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        StarCatalogue catalogue;
        Font fontForButtons;

        try (InputStream hygDataLoader = resourceStream("/hygdata_v3.csv");
             InputStream astLoader = resourceStream("/asterisms.txt");
             InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {
            catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygDataLoader, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astLoader, AsterismLoader.INSTANCE)
                    .build();

            fontForButtons = Font.loadFont(fontStream, 15);
        }

        ZonedDateTime when = ZonedDateTime.now();
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime(when);

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(DEFAULT_OBSERVATION_COORDINATES);

        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(DEFAULT_CENTER_FOR_VIEW);
        viewingParametersBean.setFieldOfViewDeg(DEFAULT_FIELD_OF_VIEW);

        SkyCanvasManager canvasManager =
                new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);

        Canvas sky = canvasManager.canvas();
        Pane skyPane = new Pane(sky);

        sky.widthProperty().bind(skyPane.widthProperty());
        sky.heightProperty().bind(skyPane.heightProperty());

        //-------------------------------------------- COORDINATES CONTROL------------------------------------------------------
        HBox coordControl = createCoordinatesController(observerLocationBean);

        coordControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //------------------------------------------------ DATE CONTROL------------------------------------------------------
        HBox dateControl = createDateController(dateTimeBean);

        dateControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        dateControl.disableProperty().bind(timeAnimator.runningProperty());

        //------------------------------------------------SPEED CONTROLLER------------------------------------------------------

        HBox speedControl = createSpeedController(timeAnimator, dateTimeBean, fontForButtons);
        speedControl.setStyle("-fx-spacing: inherit;");

        //------------------------------------------------WHOLE CONTROL BAR-----------------------------------------------------
        HBox controlBar = new HBox(coordControl, new Separator(Orientation.VERTICAL), dateControl, new Separator(Orientation.VERTICAL), speedControl);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        //------------------------------------------------BOTTOM INFORMATION BAR------------------------------------------------

        BorderPane infoBar = createBottomInfoBar(viewingParametersBean, canvasManager);

        //------------------------------------------------ROOT BORDERPANE-------------------------------------------------------
        BorderPane root = new BorderPane(skyPane, controlBar, null, infoBar, null);

        //------------------------------------------------PRIMARY STAGE---------------------------------------------------------
        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(MIN_WIDTH_STAGE);
        primaryStage.setMinHeight(MIN_HEIGHT_STAGE);

        primaryStage.setScene(new Scene(root));
        //primaryStage.setFullScreen(true);
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("esc")); //TODO : on peut aussi faire ça comme bonus
        primaryStage.show();

        sky.requestFocus();
    }

    /**
     * Creates the coordinates controller
     *
     * @param locationBean the location bean to bind with
     * @return the coordinates controller
     */
    private HBox createCoordinatesController(ObserverLocationBean locationBean) {
        TextField textFieldLon = new TextField();
        TextField textFieldLat = new TextField();
        textFieldLon.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        textFieldLat.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        TextFormatter<Number> lonTextFormatter = createNumberFormatter(GeographicCoordinates::isValidLonDeg, stringConverter);
        TextFormatter<Number> latTextFormatter = createNumberFormatter(GeographicCoordinates::isValidLatDeg, stringConverter);

        textFieldLon.setTextFormatter(lonTextFormatter);
        textFieldLat.setTextFormatter(latTextFormatter);

        lonTextFormatter.valueProperty().bindBidirectional(locationBean.lonDegProperty());
        latTextFormatter.valueProperty().bindBidirectional(locationBean.latDegProperty());

        return new HBox(new Label("Longitude (°) :"), textFieldLon, new Label("Latitude (°) :"), textFieldLat);
    }

    /**
     * Creates the number formatter for the coordinates controller
     *
     * @param predicate       the method to use
     * @param stringConverter the string converter to use
     * @return the text formatter
     */
    private TextFormatter<Number> createNumberFormatter(Predicate<Double> predicate, NumberStringConverter stringConverter) {
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return predicate.test(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 0, filter);
    }

    private HBox createDateController(DateTimeBean dateTimeBean) {
        Label date = new Label("Date :");

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");

        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());

        Label hour = new Label("Heure :");

        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter timeStringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(timeStringConverter);

        TextField hourPicker = new TextField();
        hourPicker.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        hourPicker.setTextFormatter(timeFormatter);

        ComboBox<ZoneId> zoneIdPicker = new ComboBox<>();
        List<String> listOfZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        listOfZoneIds.sort(String::compareTo); //Calls the String compareTo method
        listOfZoneIds.forEach(id -> zoneIdPicker.getItems().add(ZoneId.of(id)));
        zoneIdPicker.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());

        zoneIdPicker.setStyle("-fx-pref-width: 180;");

        return new HBox(date, datePicker, hour, hourPicker, zoneIdPicker);
    }

    private HBox createSpeedController(TimeAnimator timeAnimator, DateTimeBean dateTimeBean, Font fontForButtons) {
        ChoiceBox<NamedTimeAccelerator> timeAcceleratorPicker = new ChoiceBox<>();
        timeAcceleratorPicker.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));
        timeAcceleratorPicker.getSelectionModel().select(NamedTimeAccelerator.TIMES_300); //Default selection of x300
        timeAcceleratorPicker.disableProperty().bind(timeAnimator.runningProperty());

        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorPicker.valueProperty(), "accelerator"));

        Button resetButton = new Button(ROLL_BACK);
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        resetButton.setOnAction((pressed) -> {
            dateTimeBean.setDate(LocalDate.now());
            dateTimeBean.setTime(LocalTime.now());
            dateTimeBean.setZone(ZoneId.systemDefault());
        });

        Button playPauseButton = new Button(PLAY);

        resetButton.setFont(fontForButtons);
        playPauseButton.setFont(fontForButtons);

        playPauseButton.setOnAction(pressed -> {
            if (playPauseButton.getText().equals(PLAY)) {
                timeAnimator.start();
                playPauseButton.setText(PAUSE);
            } else {
                timeAnimator.stop();
                playPauseButton.setText(PLAY);
            }
        });

        return new HBox(timeAcceleratorPicker, resetButton, playPauseButton);
    }

    private BorderPane createBottomInfoBar(ViewingParametersBean viewingParametersBean, SkyCanvasManager canvasManager) {
        Text fieldOfViewText = new Text();
        fieldOfViewText.setStyle("-fx-padding: 4; -fx-background-color: white;");
        fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        Text closestObject = new Text();
        closestObject.setStyle("-fx-padding: 4; -fx-background-color: white;");
        canvasManager.objectUnderMouseProperty().addListener((o, oV, nV) ->
                closestObject.setText(nV == null ? "" : nV.info())); //TODO: stringBinding

        //closestObject.textProperty().bind(Bindings.format(, canvasManager.objectUnderMouseProperty()));

        Text horCoord = new Text();
        horCoord.setStyle("-fx-padding: 4; -fx-background-color: white;");
        horCoord.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));

        return new BorderPane(closestObject, null, horCoord, null, fieldOfViewText);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }
}