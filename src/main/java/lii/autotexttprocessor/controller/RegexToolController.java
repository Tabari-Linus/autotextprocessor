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
import lii.autotexttprocessor.model.TextProcessor;
import lii.autotexttprocessor.util.LoggerUtil;

import java.io.File;
import java.util.List;

public class RegexToolController {
    @FXML private TextArea inputTextArea;
    @FXML private TextField regexField;
    @FXML private TextField replacementField;
    @FXML private Button findMatchesBtn;
    @FXML private Button replaceBtn;
    @FXML private Button loadFileBtn;
    @FXML private Button saveResultsBtn;
    @FXML private Button validateRegexBtn;
    @FXML private ListView<String> matchesListView;
    @FXML private TextArea resultTextArea;
    @FXML private VBox mainContainer;
    @FXML private ComboBox<String> patternComboBox;
    @FXML private ToggleButton customPatternToggle;
    @FXML private TextField customPatternField;


    private TextProcessor textProcessor = new TextProcessor();
    private FileService fileService = new FileService();
    private Stage primaryStage;

    public RegexToolController(TabPane tabPane) {
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
        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter your text here...");

        regexField = new TextField();
        regexField.setPromptText("Enter regex pattern");

        replacementField = new TextField();
        replacementField.setPromptText("Enter replacement text (for replace operation)");

        findMatchesBtn = new Button("Find Matches");
        replaceBtn = new Button("Replace Matches");
        validateRegexBtn = new Button("Validate Regex");

        matchesListView = new ListView<>();
        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);

        // Layout
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
        HBox fileButtons = new HBox(10, loadFileBtn = new Button("Load from File"),
                saveResultsBtn = new Button("Save Results"));

        VBox regexControls = new VBox(10,
                new Label("Regex Pattern:"),
                patternSelectionBox,
                new Label("Replacement Text:"), replacementField,
                fileButtons,
                new HBox(10, findMatchesBtn, replaceBtn, validateRegexBtn)
        );

        mainContainer = new VBox(15,
                new Label("Input Text:"), inputTextArea,
                regexControls,
                new Label("Matches Found:"), matchesListView,
                new Label("Result:"), resultTextArea
        );
        mainContainer.setPadding(new Insets(15));

        setupPatternSelectionHandlers();
        // Event handlers
        setupFileHandlers();
        setupEventHandlers();

    }

    private void setupFileHandlers() {
        loadFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Text File");
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try {
                    String content = fileService.readFile(file.getAbsolutePath());
                    inputTextArea.setText(content);
                } catch (Exception ex) {
                    showErrorAlert("File Error", "Could not load file: " + ex.getMessage());
                }
            }
        });

        saveResultsBtn.setOnAction(e -> {
            if (resultTextArea.getText().isEmpty()) {
                showErrorAlert("Save Error", "No results to save");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Results");
            fileChooser.setInitialFileName("regex_results.txt");
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try {
                    fileService.writeFile(file.getAbsolutePath(), resultTextArea.getText());
                    showInfoAlert("Success", "Results saved successfully");
                } catch (Exception ex) {
                    showErrorAlert("Save Error", "Could not save file: " + ex.getMessage());
                }
            }
        });
    }

    private void setupPatternSelectionHandlers() {
        // Toggle between predefined and custom patterns
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

        // Sync custom pattern field with regex field
        customPatternField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (customPatternToggle.isSelected()) {
                regexField.setText(newVal);
            }
        });
    }

    private void setupEventHandlers() {
        findMatchesBtn.setOnAction(e -> {
            try {
                List<String> matches = textProcessor.findMatchPattern(
                        inputTextArea.getText(),
                        regexField.getText()
                );
                matchesListView.getItems().setAll(matches);
                resultTextArea.setText(inputTextArea.getText());
            } catch (Exception ex) {
                showErrorAlert("Regex Error", ex.getMessage());
                LoggerUtil.logError("Error finding matches", ex);
            }
        });

        replaceBtn.setOnAction(e -> {
            try {
                String result = textProcessor.replaceMatches(
                        inputTextArea.getText(),
                        regexField.getText(),
                        replacementField.getText()
                );
                resultTextArea.setText(result);
                matchesListView.getItems().clear();
            } catch (Exception ex) {
                showErrorAlert("Regex Error", ex.getMessage());
                LoggerUtil.logError("Error replacing matches", ex);
            }
        });

        validateRegexBtn.setOnAction(e -> {
            try {
                boolean isValid = textProcessor.findMatchPattern(inputTextArea.getText(), regexField.getText()) != null;
                showInfoAlert("Regex Validation", isValid ? "Valid regex pattern!" : "Invalid regex pattern!");
            } catch (Exception ex) {
                showErrorAlert("Regex Error", ex.getMessage());
                LoggerUtil.logError("Error validating regex", ex);
            }
        });
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}