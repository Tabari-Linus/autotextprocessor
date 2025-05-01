package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lii.autotexttprocessor.service.FileService;
import lii.autotexttprocessor.service.RegexService;
import lii.autotexttprocessor.service.DataProcessingService;
import lii.autotexttprocessor.util.LoggerUtil;
import lii.autotexttprocessor.util.RegexUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private TextField regexInput;
    @FXML
    private TextArea textInput;
    @FXML
    private TextArea resultOutput;

    private final RegexService regexService = new RegexService();
    private final FileService fileService = new FileService();
    private final DataProcessingService dataProcessingService = new DataProcessingService();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void processText() {
        try {
            String regex = regexInput.getText();
            String inputText = textInput.getText();
            String result = regexService.searchAndReplace(inputText, regex, "[REPLACED]");
            resultOutput.setText(result);
            LoggerUtil.logInfo("Processed text with regex: " + regex);
        } catch (Exception e) {
            LoggerUtil.logError("Error processing text", e);
            resultOutput.setText("Error processing text: " + e.getMessage());
        }
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
                LoggerUtil.logInfo("Loaded file: " + file.getAbsolutePath());
            } catch (IOException e) {
                LoggerUtil.logError("Error reading file", e);
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
                LoggerUtil.logInfo("Saved file: " + file.getAbsolutePath());
            } catch (IOException e) {
                LoggerUtil.logError("Error saving file", e);
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
                LoggerUtil.logInfo("Batch processed files: " + filePaths);
            } catch (IOException e) {
                LoggerUtil.logError("Error during batch processing", e);
                resultOutput.setText("Error during batch processing: " + e.getMessage());
            }
        }
    }

    @FXML
    public void analyzeWordFrequency() {
        try {
            String inputText = textInput.getText();
            Map<String, Long> wordFrequency = dataProcessingService.analyzeWordFrequency(inputText);
            StringBuilder result = new StringBuilder("Word Frequency Analysis:\n");
            wordFrequency.forEach((word, count) -> result.append(word).append(": ").append(count).append("\n"));
            resultOutput.setText(result.toString());
            LoggerUtil.logInfo("Performed word frequency analysis");
        } catch (Exception e) {
            LoggerUtil.logError("Error analyzing word frequency", e);
            resultOutput.setText("Error analyzing word frequency: " + e.getMessage());
        }
    }

    @FXML
    public void summarizeText() {
        try {
            String inputText = textInput.getText();
            String summary = dataProcessingService.summarizeText(inputText, 50); // Limit to 50 words
            resultOutput.setText("Text Summary:\n" + summary);
            LoggerUtil.logInfo("Summarized text");
        } catch (Exception e) {
            LoggerUtil.logError("Error summarizing text", e);
            resultOutput.setText("Error summarizing text: " + e.getMessage());
        }
    }

    @FXML
    public void findMatches() {
        try {
            String regex = regexInput.getText();
            String inputText = textInput.getText();

            if (!RegexUtil.isValidRegex(regex)) {
                resultOutput.setText("Invalid regex pattern.");
                return;
            }

            List<String> matches = RegexUtil.findMatches(inputText, regex);
            if (matches.isEmpty()) {
                resultOutput.setText("No matches found.");
            } else {
                resultOutput.setText("Matches:\n" + String.join("\n", matches));
            }
            LoggerUtil.logInfo("Performed regex search with pattern: " + regex);
        } catch (Exception e) {
            LoggerUtil.logError("Error finding matches", e);
            resultOutput.setText("Error finding matches: " + e.getMessage());
        }
    }

    @FXML
    public void replaceMatches() {
        try {
            String regex = regexInput.getText();
            String inputText = textInput.getText();
            String replacement = "[REPLACED]";

            if (!RegexUtil.isValidRegex(regex)) {
                resultOutput.setText("Invalid regex pattern.");
                return;
            }

            String replacedText = RegexUtil.replaceMatches(inputText, regex, replacement);
            resultOutput.setText("Replaced Text:\n" + replacedText);
            LoggerUtil.logInfo("Performed regex replace with pattern: " + regex);
        } catch (Exception e) {
            LoggerUtil.logError("Error replacing matches", e);
            resultOutput.setText("Error replacing matches: " + e.getMessage());
        }
    }

}