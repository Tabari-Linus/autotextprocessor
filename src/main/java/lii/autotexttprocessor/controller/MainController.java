package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lii.autotexttprocessor.service.FileService;
import lii.autotexttprocessor.service.RegexService;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private TextField regexInput;
    @FXML
    private TextArea textInput;
    @FXML
    private TextArea resultOutput;

    private final RegexService regexService = new RegexService();
    private final FileService fileService = new FileService();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void processText() {
        String regex = regexInput.getText();
        String inputText = textInput.getText();
        String result = regexService.searchAndReplace(inputText, regex, "[REPLACED]");
        resultOutput.setText(result);
    }


    @FXML
    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String content = fileService.readFile(file.getAbsolutePath());
                textInput.setText(content);
            } catch (IOException e) {
                resultOutput.setText("Error reading file: " + e.getMessage());
            }
        }
    }

    @FXML
    public void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Processed File");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                fileService.writeFile(file.getAbsolutePath(), resultOutput.getText());
            } catch (IOException e) {
                resultOutput.setText("Error saving file: " + e.getMessage());
            }
        }
    }

    public void processBatchFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files for Batch Processing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            List<String> filePaths = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
            String regex = regexInput.getText();
            String replacement = "[REPLACED]";

            try {
                fileService.processFiles(filePaths, regex, replacement);
                resultOutput.setText("Batch processing completed successfully.");
            } catch (IOException e) {
                resultOutput.setText("Error during batch processing: " + e.getMessage());
            }
        }
    }
}