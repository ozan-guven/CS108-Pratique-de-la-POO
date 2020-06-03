package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Non instantiatable class used to create the top menu bar
 *
 * @author Robin Goumaz (301420)
 * @author Ozan Güven (297076)
 */
public final class TopMenuBar {

    private static final BooleanProperty DRAW_ASTERISIMS_SELECTED = new SimpleBooleanProperty(true);
    private static final BooleanProperty DAY_NIGHT_SELECTED = new SimpleBooleanProperty(false);

    private static final HorizontalCoordinates DEFAULT_CENTER_FOR_VIEW =
            HorizontalCoordinates.ofDeg(180.000000000001, 15);

    private TopMenuBar() {
    }

    /**
     * Only public method of the class that is used to create the top menu bar
     *
     * @param primaryStage the primary stag
     * @param canvasManager the canvas manager
     * @param viewingParametersBean the viewing parameter bean
     * @return the top menu bar
     */
    public static MenuBar createMenuBar(Stage primaryStage, SkyCanvasManager canvasManager, ViewingParametersBean viewingParametersBean) {
        Menu graphicsMenu = createGraphicsMenu(canvasManager);
        Menu windowOptions = createWindowOptionsMenu(primaryStage);
        Menu celestialMenu = createCelestialMenu(canvasManager, viewingParametersBean);
        Menu settingsMenu = createSettingsMenu();

        MenuBar mainMenu = new MenuBar(graphicsMenu, windowOptions, celestialMenu, settingsMenu);
        mainMenu.setStyle("-fx-font-size: 11px; -fx-background-color: white;");

        return mainMenu;
    }

    private static Menu createGraphicsMenu(SkyCanvasManager canvasManager) {
        Menu graphicsMenu = new Menu("_Graphismes");

        CheckMenuItem asterismsOption = new CheckMenuItem("Afficher les astérismes");
        asterismsOption.setSelected(canvasManager.isDrawAsterisms());
        asterismsOption.setOnAction(action -> canvasManager.setDrawAsterisms(!canvasManager.isDrawAsterisms()));
        DRAW_ASTERISIMS_SELECTED.bind(asterismsOption.selectedProperty());

        CheckMenuItem dayNightMenu = new CheckMenuItem("Cycle jour/nuit");
        canvasManager.allowDayNightCycleProperty().bind(dayNightMenu.selectedProperty());
        DAY_NIGHT_SELECTED.bind(dayNightMenu.selectedProperty());

        graphicsMenu.getItems().addAll(asterismsOption, dayNightMenu);
        return graphicsMenu;
    }

    private static Menu createWindowOptionsMenu(Stage primaryStage) {
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
        return windowOptions;
    }

    private static Menu createCelestialMenu(SkyCanvasManager canvasManager, ViewingParametersBean viewingParametersBean) {
        Menu celestialMenu = new Menu("_Astres");

        MenuItem sunMenu = new MenuItem("_Soleil");
        sunMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getSunCoordinates()));

        MenuItem moonMenu = new MenuItem("_Lune");
        moonMenu.setOnAction(action -> viewingParametersBean.setCenter(canvasManager.getMoonCoordinates()));

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

        MenuItem resetMenu = new MenuItem("_Défaut");
        resetMenu.setOnAction(action -> viewingParametersBean.setCenter(DEFAULT_CENTER_FOR_VIEW));

        celestialMenu.getItems().addAll(sunMenu, moonMenu, mercuryMenu, venusMenu, marsMenu, jupiterMenu, saturnMenu, uranusMenu, neptuneMenu, resetMenu);
        return celestialMenu;
    }

    private static Menu createSettingsMenu() {
        Menu settingsMenu = new Menu("_Paramètres");
        MenuItem saveCurrentState = new MenuItem("Enregister les choix actuelles");

        //Calls the settings to be updated and write the current settings
        saveCurrentState.setOnAction(action -> Settings.writeSettings(DRAW_ASTERISIMS_SELECTED, DAY_NIGHT_SELECTED));

        settingsMenu.getItems().add(saveCurrentState);

        return settingsMenu;
    }
}
