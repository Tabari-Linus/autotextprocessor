package lii.autotexttprocessor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lii.autotexttprocessor.service.RegexService;

public class MainController {
    @FXML
    private TextField regexInput;
    @FXML
    private TextArea textInput;
    @FXML
    private TextArea resultOutput;

    private final RegexService regexService = new RegexService();

    @FXML
    public void processText() {
        String regex = regexInput.getText();
        String inputText = textInput.getText();
        String result = regexService.searchAndReplace(inputText, regex, "[REPLACED]");
        resultOutput.setText(result);
    }
}