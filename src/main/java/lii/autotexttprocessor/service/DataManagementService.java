package lii.autotexttprocessor.service;

import lii.autotexttprocessor.model.DataEntry;

import java.util.ArrayList;
import java.util.List;

public class DataManagementService {
    private final List<DataEntry> dataEntries = new ArrayList<>();

    public void addEntry(DataEntry entry) {
        dataEntries.add(entry);
    }

    public void updateEntry(int id, String name, String value) {
        for (DataEntry entry : dataEntries) {
            if (entry.getId() == id) {
                entry.setName(name);
                entry.setValue(value);
                return;
            }
        }
    }

    public void deleteEntry(int id) {
        dataEntries.removeIf(entry -> entry.getId() == id);
    }

    public List<DataEntry> getAllEntries() {
        return new ArrayList<>(dataEntries);
    }
}