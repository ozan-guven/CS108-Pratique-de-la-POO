package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class Main extends Application {

    public static final int MIN_WIDTH_STAGE = 800;
    public static final int MIN_HEIGHT_STAGE = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //-------------------------------------------- COORDINATES CONTROL------------------------------------------------------
        TextField textFieldLon = new TextField();
        TextField textFieldLat = new TextField();
        textFieldLon.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        textFieldLat.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        //TODO : Faire une méthode apart
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringConverter, 0, lonFilter);

        UnaryOperator<TextFormatter.Change> latFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(stringConverter, 0, latFilter);

        textFieldLon.setTextFormatter(lonTextFormatter);
        textFieldLat.setTextFormatter(latTextFormatter);

        HBox coordControl = new HBox(new Label("Longitude (°) :"), textFieldLon, new Label("Latitude (°) :"), textFieldLat);

        coordControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        //------------------------------------------------ DATE CONTROL------------------------------------------------------
        Label date = new Label("Date :");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-pref-width: 120;");

        Label hour = new Label("Heure :");


        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter timeStringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter =
                new TextFormatter<>(timeStringConverter);


        TextField hourPicker = new TextField(LocalTime.now().format(hmsFormatter));
        hourPicker.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        hourPicker.setTextFormatter(timeFormatter); //TODO ne marche pas

        ComboBox<String> zoneIdPicker = new ComboBox<>();
        List<String> listOfZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        listOfZoneIds.sort(String::compareTo); //Calls the String compareTo method
        zoneIdPicker.getItems().addAll(listOfZoneIds);
        zoneIdPicker.getSelectionModel().select(ZoneId.systemDefault().toString());
        zoneIdPicker.setStyle("-fx-pref-width: 180;");

        HBox dateControl = new HBox(date, datePicker, hour, hourPicker, zoneIdPicker);

        dateControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");


        //------------------------------------------------SPEED CONTROLLER------------------------------------------------------

        ChoiceBox<NamedTimeAccelerator> timeAcceleratorPicker = new ChoiceBox<>();
        timeAcceleratorPicker.setItems(FXCollections.observableList(List.of(NamedTimeAccelerator.values())));
        timeAcceleratorPicker.getSelectionModel().select(NamedTimeAccelerator.TIMES_300); //Default selection of x300

        HBox speedControl = new HBox(timeAcceleratorPicker);
        speedControl.setStyle("-fx-spacing: inherit;");

        //------------------------------------------------WHOLE CONTROL BAR-----------------------------------------------------
        HBox controlBar = new HBox(coordControl, new Separator(Orientation.VERTICAL), dateControl, new Separator(Orientation.VERTICAL), speedControl);
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");


        //------------------------------------------------------------------------------------------------------------------------------------------------------------
        BorderPane root = new BorderPane(null, controlBar, null, null, null);

        //------------------------------------------------------------------------------------------------------------------------------------------------------------
        primaryStage.setTitle("Rigel");
        primaryStage.setMinWidth(MIN_WIDTH_STAGE);
        primaryStage.setMinHeight(MIN_HEIGHT_STAGE);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Creates the coordinate control HBox
     *
     * @return the coordinate control HBox
     */
    private HBox coordControl() {
        TextField textFieldLon = new TextField();
        TextField textFieldLat = new TextField();
        textFieldLon.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        textFieldLat.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        //TODO : Faire une méthode apart
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringConverter, 0, lonFilter);

        UnaryOperator<TextFormatter.Change> latFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(stringConverter, 0, latFilter);

        textFieldLon.setTextFormatter(lonTextFormatter);
        textFieldLat.setTextFormatter(latTextFormatter);

        return new HBox(new Label("Longitude (°) :"), textFieldLon, new Label("Latitude (°) :"), textFieldLat);
    }
}