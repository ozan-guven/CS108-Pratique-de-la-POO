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

    private static final int MIN_WIDTH_STAGE = 950;
    private static final int MIN_HEIGHT_STAGE = 600;
    private static final GeographicCoordinates DEFAULT_OBSERVATION_COORDINATES =
            GeographicCoordinates.ofDeg(6.57, 46.52);
    private static final HorizontalCoordinates DEFAULT_CENTER_FOR_VIEW =
            HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private static final double DEFAULT_FIELD_OF_VIEW = 100;
    private static final String UNDO = "\uf0e2";
    private static final String PLAY = "\uf04b";
    private static final String PAUSE = "\uf04c";

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

        DateTimeBean dateTimeBean = new DateTimeBean(ZonedDateTime.now());

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

        //------------------------------------------------TOP CONTROL BAR-----------------------------------------------
        VBox controlBar = createTopControlBar(observerLocationBean, dateTimeBean, timeAnimator, fontForButtons, canvasManager, primaryStage, viewingParametersBean);

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

        ComboBox<String> cityPicker = new ComboBox<>();
        List<String> cityList = new ArrayList<>(CityCoordinates.getCityMap().keySet());
        cityList.sort(String::compareTo);
        cityPicker.getItems().addAll(cityList);
        cityPicker.getSelectionModel().select("EPFL");
        cityPicker.setOnAction(action -> locationBean.setCoordinates(CityCoordinates.getCityMap().get(cityPicker.getValue())));

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

    private HBox createSpeedController(TimeAnimator timeAnimator, DateTimeBean dateTimeBean, Font fontForButtons) {
        ChoiceBox<NamedTimeAccelerator> timeAcceleratorPicker = new ChoiceBox<>();
        timeAcceleratorPicker.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));
        timeAcceleratorPicker.getSelectionModel().select(NamedTimeAccelerator.TIMES_300); //Default selection of x300
        timeAcceleratorPicker.disableProperty().bind(timeAnimator.runningProperty());

        timeAnimator.acceleratorProperty().bind(Bindings.select(timeAcceleratorPicker.valueProperty(), "accelerator"));

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

    /*private Button createAsterismsButton(SkyCanvasManager canvasManager) {
        Button asterismsButton = new Button(HIDE_ASTERISMS);

        asterismsButton.setOnAction(action -> {
            if (canvasManager.isDrawAsterisms()) {
                asterismsButton.setText(SHOW_ASTERISMS);
                canvasManager.setDrawAsterisms(false);
            } else {
                asterismsButton.setText(HIDE_ASTERISMS);
                canvasManager.setDrawAsterisms(true);
            }
        });
        return asterismsButton;
    }*/

    private MenuBar createMenuBar(SkyCanvasManager canvasManager, Stage primaryStage, ViewingParametersBean viewingParametersBean) {
        Menu graphicsMenu = new Menu("_Graphismes");
        //Menu items
        CheckMenuItem asterismsOption = new CheckMenuItem("Afficher les astérismes");
        asterismsOption.setSelected(canvasManager.isDrawAsterisms());
        asterismsOption.setOnAction(action -> canvasManager.setDrawAsterisms(!canvasManager.isDrawAsterisms()));
        graphicsMenu.getItems().add(asterismsOption);

        Menu windowOptions = new Menu("_Fenêtre");
        Text fullScreenText = new Text();
        fullScreenText.textProperty().bind(Bindings.when(primaryStage.fullScreenProperty())
                .then("Quitter le mode plein écran")
                .otherwise("Passer en mode plein écran"));
        //Menu items
        MenuItem fullScreenOption = new MenuItem();
        fullScreenOption.textProperty().bind(fullScreenText.textProperty());
        fullScreenOption.setOnAction(action -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));
        windowOptions.getItems().add(fullScreenOption);

        Menu celestialMenu = new Menu("_Astres");

        MenuItem sunMenu = new MenuItem("_Soleil");
        sunMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getSunCoordinates()));

        MenuItem moonMenu = new MenuItem("_Lune");
        moonMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getMoonCoordinates()));

        //MenuItem planet = new MenuItem("Planets");
        MenuItem mercuryMenu = new MenuItem("Mér_cure");
        mercuryMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(0)));

        MenuItem venusMenu = new MenuItem("_Vénus");
        venusMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(1)));

        MenuItem marsMenu = new MenuItem("_Mars");
        marsMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(2)));

        MenuItem jupiterMenu = new MenuItem("_Jupiter");
        jupiterMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(3)));

        MenuItem saturnMenu = new MenuItem("_Satrune");
        saturnMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(4)));

        MenuItem uranusMenu = new MenuItem("_Uranus");
        uranusMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(5)));

        MenuItem neptuneMenu = new MenuItem("_Néptune");
        neptuneMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getPlanetsCoordinates().get(6)));

        MenuItem resetMenu = new MenuItem("_Reset");
        resetMenu.setOnAction(action -> viewingParametersBean.setCenter(DEFAULT_CENTER_FOR_VIEW));

        celestialMenu.getItems().addAll(sunMenu, moonMenu, mercuryMenu, venusMenu, marsMenu, jupiterMenu, saturnMenu, uranusMenu, neptuneMenu, resetMenu);//planet);

        MenuBar mainMenu = new MenuBar(graphicsMenu, windowOptions, celestialMenu);
        mainMenu.setStyle("-fx-font-size: 11px; -fx-background-color: white;");

        return mainMenu;
    }

    private VBox createTopControlBar(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean, TimeAnimator timeAnimator, Font fontForButtons, SkyCanvasManager canvasManager, Stage primaryStage, ViewingParametersBean viewingParametersBean) {
        //COORDINATES CONTROL
        HBox coordControl = createCoordinatesController(observerLocationBean);

        //DATE CONTROL
        HBox dateControl = createDateController(dateTimeBean, timeAnimator);

        //SPEED CONTROLLER
        HBox speedControl = createSpeedController(timeAnimator, dateTimeBean, fontForButtons);

        //ASTERISMS BUTTON
        //Button asterismsButton = createAsterismsButton(canvasManager);

        //TOP MENU BAR
        MenuBar mainMenu = createMenuBar(canvasManager, primaryStage, viewingParametersBean);

        HBox controlBar = new HBox(coordControl, new Separator(Orientation.VERTICAL), dateControl,
                new Separator(Orientation.VERTICAL), speedControl);//, new Separator(Orientation.VERTICAL), asterismsButton);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4; -fx-background-color: white;");

        return new VBox( mainMenu, controlBar);
    }

    private BorderPane createBottomInfoBar(ViewingParametersBean viewingParametersBean, SkyCanvasManager canvasManager) {
        Text fieldOfViewText = new Text();
        fieldOfViewText.setStyle("-fx-padding: 4; -fx-background-color: white;");
        fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        Text closestObject = new Text();
        closestObject.setStyle("-fx-padding: 4; -fx-background-color: white;");
        closestObject.textProperty().bind(
                Bindings.createStringBinding(() -> canvasManager.getObjectUnderMouse() == null
                        ? ""
                        : canvasManager.getObjectUnderMouse().toString(), canvasManager.objectUnderMouseProperty()));

        Text horCoord = new Text();
        horCoord.setStyle("-fx-padding: 4; -fx-background-color: white;");
        horCoord.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltDegProperty()));

        return new BorderPane(closestObject, null, horCoord, null, fieldOfViewText);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }
}