package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

    private FileService fileService = new FileService();
    private List<File> selectedFiles = new ArrayList<>();

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

        // Layout
        VBox regexControls = new VBox(10,
                new Label("Regex Pattern:"), regexField,
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