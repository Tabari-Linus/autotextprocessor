package lii.autotexttprocessor.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lii.autotexttprocessor.service.FileService;
import lii.autotexttprocessor.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileProcessingController {
    @FXML private TextField regexField;
    @FXML private TextField replacementField;
    @FXML private Button addFileBtn;
    @FXML private Button processFilesBtn;
    @FXML private ListView<String> filesListView;
    @FXML private TextArea logTextArea;
    @FXML private VBox mainContainer;
    @FXML private ComboBox<String> patternComboBox;
    @FXML private ToggleButton customPatternToggle;
    @FXML private TextField customPatternField;

    private FileService fileService = new FileService();
    private List<File> selectedFiles = new ArrayList<>();
    private Stage primaryStage;

    public FileProcessingController(TabPane tabPane) {
        tabPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                this.primaryStage = (Stage) newScene.getWindow();
            }
        });
    }
    public VBox getView() {
        if (mainContainer == null) {
            initializeUI();
        }
        return mainContainer;
    }

    private void initializeUI() {
        // Create UI components
        regexField = new TextField();
        regexField.setPromptText("Enter regex pattern");

        replacementField = new TextField();
        replacementField.setPromptText("Enter replacement text");

        addFileBtn = new Button("Add Files");
        processFilesBtn = new Button("Process Files");

        filesListView = new ListView<>();
        logTextArea = new TextArea();
        logTextArea.setEditable(false);

        patternComboBox = new ComboBox<>();
        patternComboBox.setPromptText("Select common pattern");
        patternComboBox.setItems(FXCollections.observableArrayList(
                "Email", "Date", "Time", "Phone Number", "URL", "IP Address"
        ));

        customPatternToggle = new ToggleButton("Custom");
        customPatternField = new TextField();
        customPatternField.setPromptText("Enter custom pattern");
        customPatternField.setDisable(true);

        HBox patternSelectionBox = new HBox(10,
                patternComboBox,
                customPatternToggle,
                customPatternField
        );

        // Replace the simple regexField with the new pattern selection
        VBox regexControls = new VBox(10,
                new Label("Regex Pattern:"),
                patternSelectionBox,
                new Label("Replacement Text:"), replacementField,
                new HBox(10, addFileBtn, processFilesBtn)
        );

        mainContainer = new VBox(15,
                regexControls,
                new Label("Selected Files:"), filesListView,
                new Label("Processing Log:"), logTextArea
        );
        mainContainer.setPadding(new Insets(15));

        // Event handlers
        setupPatternSelectionHandlers();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        addFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Files to Process");
            List<File> files = fileChooser.showOpenMultipleDialog(null);

            if (files != null) {
                selectedFiles.addAll(files);
                updateFilesListView();
            }
        });

        processFilesBtn.setOnAction(e -> {
            if (selectedFiles.isEmpty()) {
                showErrorAlert("No Files", "Please add files to process");
                return;
            }

            if (regexField.getText().isEmpty()) {
                showErrorAlert("No Pattern", "Please enter a regex pattern");
                return;
            }

            try {
                List<String> filePaths = selectedFiles.stream()
                        .map(File::getAbsolutePath)
                        .toList();

                fileService.processFiles(filePaths, regexField.getText(), replacementField.getText());
                logTextArea.appendText("Successfully processed " + selectedFiles.size() + " files\n");

            } catch (IOException ex) {
                LoggerUtil.logError("Error processing files", ex);
                logTextArea.appendText("Error processing files: " + ex.getMessage() + "\n");
            }
        });
    }

    private void setupPatternSelectionHandlers() {
        // Same implementation as in RegexToolController
        customPatternToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            patternComboBox.setDisable(newVal);
            customPatternField.setDisable(!newVal);
            if (newVal) {
                regexField.setText(customPatternField.getText());
            }
        });

        patternComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !customPatternToggle.isSelected()) {
                String pattern = switch (newVal) {
                    case "Email" -> "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
                    case "Date" -> "\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b";
                    case "Time" -> "\\b\\d{1,2}:\\d{2}(:\\d{2})?\\b";
                    case "Phone Number" -> "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
                    case "URL" -> "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
                    case "IP Address" -> "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
                    default -> "";
                };
                regexField.setText(pattern);
            }
        });

        customPatternField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (customPatternToggle.isSelected()) {
                regexField.setText(newVal);
            }
        });
    }

    private void updateFilesListView() {
        filesListView.getItems().setAll(
                selectedFiles.stream()
                        .map(File::getName)
                        .toList()
        );
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}