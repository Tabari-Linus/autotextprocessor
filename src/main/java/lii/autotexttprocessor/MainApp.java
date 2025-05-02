package lii.autotexttprocessor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lii.autotexttprocessor.controller.*;
import lii.autotexttprocessor.util.LoggerUtil;

public class MainApp extends Application {

    private static final String APP_TITLE = "Auto Text Processor";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create main layout with menu bar
            BorderPane root = new BorderPane();

            // Create and add menu bar
            root.setTop(createMenuBar());

            // Create main tab pane
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            // Add tabs - Dashboard first
            Tab dashboardTab = new Tab("Dashboard", new DashboardController(tabPane).getView());
            Tab regexTab = new Tab("Regex Tools", new RegexToolController(tabPane).getView());
            Tab textAnalysisTab = new Tab("Text Analysis", new TextAnalysisController(tabPane).getView());
            Tab fileProcessingTab = new Tab("File Processing", new FileProcessingController(tabPane).getView());
            Tab dataManagementTab = new Tab("Data Management", new DataManagementController(tabPane).getView());

            tabPane.getTabs().addAll(dashboardTab, regexTab, textAnalysisTab, fileProcessingTab, dataManagementTab);
            root.setCenter(tabPane);

            // Set up scene and stage
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icons-company.png")));
            primaryStage.show();

        } catch (Exception e) {
            LoggerUtil.logError("Error starting application", e);
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem exitItem = new MenuItem("Exit");

        fileMenu.getItems().addAll(openItem, saveItem, loadItem, new SeparatorMenuItem(), exitItem);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");

        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), copyItem, pasteItem);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem docsItem = new MenuItem("Documentation");

        helpMenu.getItems().addAll(aboutItem, docsItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        // Set event handlers
        exitItem.setOnAction(e -> System.exit(0));
        aboutItem.setOnAction(e -> showAboutDialog());

        return menuBar;
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AutoText Processor");
        alert.setHeaderText("AutoText Processor v1.0");
        alert.setContentText("A powerful text processing tool with regex capabilities, text analysis, and file processing features.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}