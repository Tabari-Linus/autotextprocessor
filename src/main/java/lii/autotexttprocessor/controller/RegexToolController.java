package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lii.autotexttprocessor.util.RegexUtil;

import java.util.List;

public class RegexToolController {

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextField regexField, replacementField;

    @FXML
    private ListView<String> resultListView;

    private final RegexUtil regexUtil = new RegexUtil();

    @FXML
    private void handleFindMatches() {
        String text = inputTextArea.getText();
        String regex = regexField.getText();

        if (!RegexUtil.isValidRegexPattern(regex)) {
            showAlert("Invalid regex pattern.");
            return;
        }

        List<String> matches = RegexUtil.getMatchInfo(text, regex);
        resultListView.getItems().setAll(matches);
    }

    @FXML
    private void handleReplace() {
        String text = inputTextArea.getText();
        String regex = regexField.getText();
        String replacement = replacementField.getText();

        if (!RegexUtil.isValidRegexPattern(regex)) {
            showAlert("Invalid regex pattern.");
            return;
        }

        String updatedText = RegexUtil.replaceMatches(text, regex, replacement);
        inputTextArea.setText(updatedText);
        resultListView.getItems().clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Regex Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
