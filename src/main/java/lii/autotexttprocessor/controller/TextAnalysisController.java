package lii.autotexttprocessor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lii.autotexttprocessor.service.DataProcessingService;
import lii.autotexttprocessor.util.LoggerUtil;

import java.util.Map;

public class TextAnalysisController {
    @FXML private TextArea inputTextArea;
    @FXML private Button analyzeBtn;
    @FXML private TableView<WordFrequency> frequencyTable;
    @FXML private TableColumn<WordFrequency, String> wordColumn;
    @FXML private TableColumn<WordFrequency, Long> countColumn;
    @FXML private Label lineCountLabel;
    @FXML private Label wordCountLabel;
    @FXML private Label charCountLabel;
    @FXML private ListView<String> sentencesListView;
    @FXML private VBox mainContainer;

    private DataProcessingService dataProcessingService = new DataProcessingService();

    public VBox getView() {
        if (mainContainer == null) {
            initializeUI();
        }
        return mainContainer;
    }

    private void initializeUI() {
        // Create UI components
        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter text to analyze...");

        analyzeBtn = new Button("Analyze Text");

        // Frequency table setup
        wordColumn = new TableColumn<>("Word");
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));

        countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        frequencyTable = new TableView<>();
        frequencyTable.getColumns().addAll(wordColumn, countColumn);

        // Stats labels
        VBox statsBox = new VBox(5,
                new Label("Text Statistics:"),
                lineCountLabel = new Label("Lines: 0"),
                wordCountLabel = new Label("Words: 0"),
                charCountLabel = new Label("Characters: 0")
        );

        sentencesListView = new ListView<>();

        // Layout
        mainContainer = new VBox(15,
                new Label("Input Text:"), inputTextArea,
                analyzeBtn,
                new Label("Word Frequency:"), frequencyTable,
                statsBox,
                new Label("Sentences:"), sentencesListView
        );
        mainContainer.setPadding(new Insets(15));

        // Event handlers
        analyzeBtn.setOnAction(e -> analyzeText());
    }

    private void analyzeText() {
        try {
            String text = inputTextArea.getText();

            // Word frequency
            Map<String, Long> frequencyMap = dataProcessingService.wordFrequency(text);
            ObservableList<WordFrequency> frequencyData = FXCollections.observableArrayList();
            frequencyMap.forEach((word, count) -> frequencyData.add(new WordFrequency(word, count)));
            frequencyTable.setItems(frequencyData);

            // Text statistics
            Map<String, Long> stats = dataProcessingService.summarizeText(text);
            lineCountLabel.setText("Lines: " + stats.getOrDefault("Line Count", 0L));
            wordCountLabel.setText("Words: " + stats.getOrDefault("Word Count", 0L));
            charCountLabel.setText("Characters: " + stats.getOrDefault("Character Count", 0L));

            // Sentences
            sentencesListView.getItems().setAll(dataProcessingService.extractSentences(text));

        } catch (Exception e) {
            LoggerUtil.logError("Error analyzing text", e);
            showErrorAlert("Analysis Error", e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper class for table data
    public static class WordFrequency {
        private final String word;
        private final long count;

        public WordFrequency(String word, long count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() { return word; }
        public long getCount() { return count; }
    }
}