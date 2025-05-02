package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lii.autotexttprocessor.model.TextProcessor;
import lii.autotexttprocessor.util.LoggerUtil;

import java.util.List;

public class RegexToolController {
    @FXML private TextArea inputTextArea;
    @FXML private TextField regexField;
    @FXML private TextField replacementField;
    @FXML private Button findMatchesBtn;
    @FXML private Button replaceBtn;
    @FXML private Button validateRegexBtn;
    @FXML private ListView<String> matchesListView;
    @FXML private TextArea resultTextArea;
    @FXML private VBox mainContainer;

    private TextProcessor textProcessor = new TextProcessor();

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
        VBox regexControls = new VBox(10,
                new Label("Regex Pattern:"), regexField,
                new Label("Replacement Text:"), replacementField,
                new HBox(10, findMatchesBtn, replaceBtn, validateRegexBtn)
        );

        mainContainer = new VBox(15,
                new Label("Input Text:"), inputTextArea,
                regexControls,
                new Label("Matches Found:"), matchesListView,
                new Label("Result:"), resultTextArea
        );
        mainContainer.setPadding(new Insets(15));

        // Event handlers
        setupEventHandlers();
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