package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CityCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.settings.Settings;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Main class of the program
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public class Main extends Application {

    private static final int MIN_WIDTH_STAGE = 1135;
    private static final int MIN_HEIGHT_STAGE = 600;
    private static final GeographicCoordinates DEFAULT_OBSERVATION_COORDINATES =
            GeographicCoordinates.ofDeg(6.57, 46.52);
    private static final HorizontalCoordinates DEFAULT_CENTER_FOR_VIEW =
            HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private static final double DEFAULT_FIELD_OF_VIEW = 100;
    private static final String UNDO = "\uf0e2";
    private static final String PLAY = "\uf04b";
    private static final String PAUSE = "\uf04c";

    private final ObjectProperty<String> selectedCity = new SimpleObjectProperty<>();
    private final ObjectProperty<NamedTimeAccelerator> selectedAccelerator = new SimpleObjectProperty<>();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        StarCatalogue catalogue;
        Font fontForButtons;

        try (InputStream hygDataLoader = resourceStream("/hygdata_v3.csv");
             InputStream astLoader = resourceStream("/asterisms.txt")) {
            catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygDataLoader, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astLoader, AsterismLoader.INSTANCE)
                    .build();
        }

        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {
            fontForButtons = Font.loadFont(fontStream, 15);
        }

        Settings currentSettings = Settings.readSettings();

        DateTimeBean dateTimeBean = new DateTimeBean(ZonedDateTime.now());

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(currentSettings.wasRead()
                ? CityCoordinates.getCityMap().get(currentSettings.selectedCity())
                : DEFAULT_OBSERVATION_COORDINATES);

        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(DEFAULT_CENTER_FOR_VIEW);
        viewingParametersBean.setFieldOfViewDeg(DEFAULT_FIELD_OF_VIEW);

        SkyCanvasManager canvasManager =
                new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);

        Canvas sky = canvasManager.canvas();
        Pane skyPane = new Pane(sky);

        sky.widthProperty().bind(skyPane.widthProperty());
        sky.heightProperty().bind(skyPane.heightProperty());

        //------------------------------------------------TOP CONTROL BAR-----------------------------------------------
        VBox controlBar = createTopControlBar(primaryStage, fontForButtons, currentSettings, timeAnimator, observerLocationBean, viewingParametersBean, canvasManager, dateTimeBean);

        //------------------------------------------------BOTTOM INFORMATION BAR----------------------------------------
        BorderPane infoBar = createBottomInfoBar(viewingParametersBean, canvasManager);

        //------------------------------------------------ROOT BORDERPANE-----------------------------------------------
        BorderPane root = new BorderPane(skyPane, controlBar, null, infoBar, null);

        //------------------------------------------------PRIMARY STAGE-------------------------------------------------
        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(MIN_WIDTH_STAGE);
        primaryStage.setMinHeight(MIN_HEIGHT_STAGE);

        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();

        sky.requestFocus();
    }

    /**
     * Creates the coordinates controller
     *
     * @param locationBean    the location bean to bind with
     * @param currentSettings the current settings
     * @return the coordinates controller
     */
    private HBox createCoordinatesController(ObserverLocationBean locationBean, Settings currentSettings) {
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

        ComboBox<String> cityPicker = new ComboBox<>();
        List<String> cityList = new ArrayList<>(CityCoordinates.getCityMap().keySet());
        cityList.sort(String::compareTo);
        cityPicker.getItems().addAll(cityList);
        cityPicker.getSelectionModel().select(
                currentSettings.wasRead()
                        ? currentSettings.selectedCity()
                        : "EPFL"); //Default selection
        cityPicker.setOnAction(action -> locationBean.setCoordinates(CityCoordinates.getCityMap().get(cityPicker.getValue())));
        selectedCity.bind(cityPicker.valueProperty()); //Binds the current selected city to the property (to be used in settings)

        cityPicker.setStyle("-fx-pref-width: 180;");

        HBox coordControl = new HBox(new Label("Longitude (°) :"), textFieldLon, new Label("Latitude (°) :"), textFieldLat, cityPicker);
        coordControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return coordControl;
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

    private HBox createDateController(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) {
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

        HBox dateControl = new HBox(date, datePicker, hour, hourPicker, zoneIdPicker);

        dateControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        dateControl.disableProperty().bind(timeAnimator.runningProperty());

        return dateControl;
    }

    private HBox createSpeedController(TimeAnimator timeAnimator, DateTimeBean dateTimeBean, Font fontForButtons, Settings currentSettings) {
        ChoiceBox<NamedTimeAccelerator> timeAcceleratorPicker = new ChoiceBox<>();
        timeAcceleratorPicker.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));
        timeAcceleratorPicker.getSelectionModel().select(
                currentSettings.wasRead()
                        ? currentSettings.selectedAccelerator()
                        : NamedTimeAccelerator.TIMES_300); //Default selection of x300
        timeAcceleratorPicker.disableProperty().bind(timeAnimator.runningProperty());

        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorPicker.valueProperty(), "accelerator"));
        selectedAccelerator.bind(timeAcceleratorPicker.valueProperty());

        Button resetButton = new Button(UNDO);
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

        HBox speedControl = new HBox(timeAcceleratorPicker, resetButton, playPauseButton);
        speedControl.setStyle("-fx-spacing: inherit;");

        return speedControl;
    }

    private VBox createTopControlBar(Stage primaryStage, Font fontForButtons, Settings currentSettings, TimeAnimator timeAnimator, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean, SkyCanvasManager canvasManager, DateTimeBean dateTimeBean) {
        //COORDINATES CONTROL
        HBox coordControl = createCoordinatesController(observerLocationBean, currentSettings);

        //DATE CONTROL
        HBox dateControl = createDateController(dateTimeBean, timeAnimator);

        //SPEED CONTROLLER
        HBox speedControl = createSpeedController(timeAnimator, dateTimeBean, fontForButtons, currentSettings);

        //TOP MENU BAR
        MenuBar mainMenu = TopMenuBar.createMenuBar(primaryStage, canvasManager, viewingParametersBean, currentSettings, selectedCity, selectedAccelerator);

        HBox controlBar = new HBox(coordControl, new Separator(Orientation.VERTICAL), dateControl,
                new Separator(Orientation.VERTICAL), speedControl);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4; -fx-background-color: white;");

        return new VBox(mainMenu, controlBar);
    }

    private BorderPane createBottomInfoBar(ViewingParametersBean viewingParametersBean, SkyCanvasManager canvasManager) {
        Text fieldOfViewText = new Text();
        fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        Text closestObject = new Text();
        closestObject.textProperty().bind(
                Bindings.createStringBinding(() -> canvasManager.getObjectUnderMouse() == null
                        ? ""
                        : canvasManager.getObjectUnderMouse().toString(), canvasManager.objectUnderMouseProperty()));

        Text horCoord = new Text();
        horCoord.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));

        BorderPane bottomPane = new BorderPane(closestObject, null, horCoord, null, fieldOfViewText);
        bottomPane.setStyle("-fx-padding: 4; -fx-background-color: white;");

        return new BorderPane(closestObject, null, horCoord, null, fieldOfViewText);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }
}