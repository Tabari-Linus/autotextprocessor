package lii.autotexttprocessor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lii.autotexttprocessor.model.DataEntry;
import lii.autotexttprocessor.service.DataManagementService;

public class DataEntryController {

    @FXML private TextField idField, nameField, valueField;
    @FXML private TableView<DataEntry> entryTableView;
    @FXML private TableColumn<DataEntry, Integer> idColumn;
    @FXML private TableColumn<DataEntry, String> nameColumn;
    @FXML private TableColumn<DataEntry, String> valueColumn;

    private final DataManagementService dataService = new DataManagementService();
    private final ObservableList<DataEntry> entryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        valueColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue()));

        entryTableView.setItems(entryList);
        entryTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> fillForm(newSelection)
        );
    }

    private void fillForm(DataEntry entry) {
        if (entry != null) {
            idField.setText(String.valueOf(entry.getId()));
            nameField.setText(entry.getName());
            valueField.setText(entry.getValue());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String value = valueField.getText();
            DataEntry entry = new DataEntry(id, name, value);
            dataService.addEntry(entry);
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showAlert("Add Error", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String value = valueField.getText();
            dataService.updateEntry(id, name, value);
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showAlert("Update Error", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int id = Integer.parseInt(idField.getText());
            dataService.deleteEntry(id);
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    private void refreshTable() {
        entryList.setAll(dataService.getAllEntries());
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        valueField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
