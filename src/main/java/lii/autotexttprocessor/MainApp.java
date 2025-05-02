package lii.autotexttprocessor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lii.autotexttprocessor.controller.*;
import lii.autotexttprocessor.util.LoggerUtil;

public class MainApp extends Application {

    private static final String APP_TITLE = "Auto Text Processor";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create main tab pane
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            // Add tabs
            Tab regexTab = new Tab("Regex Tools", new RegexToolController().getView());
            Tab textAnalysisTab = new Tab("Text Analysis", new TextAnalysisController().getView());
            Tab fileProcessingTab = new Tab("File Processing", new FileProcessingController().getView());
            Tab dataManagementTab = new Tab("Data Management", new DataManagementController().getView());

            tabPane.getTabs().addAll(regexTab, textAnalysisTab, fileProcessingTab, dataManagementTab);

            // Set up scene and stage
            Scene scene = new Scene(tabPane, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
//            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app-icon.png")));
            primaryStage.show();

        } catch (Exception e) {
            LoggerUtil.logError("Error starting application", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}