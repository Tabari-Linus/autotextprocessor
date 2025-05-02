package lii.autotexttprocessor.service;

import lii.autotexttprocessor.model.DataEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManagementService {
    private final Map<Integer, DataEntry> dataEntries = new HashMap<>();

    public void addEntry(DataEntry entry) {
        if (dataEntries.containsKey(entry.getId())) {
            throw new IllegalArgumentException("Entry with Id " + entry.getId() + " already exists.");
        }
        dataEntries.put(entry.getId(), entry);
    }

    public void updateEntry(int id, String name, String value) {
        DataEntry entry = dataEntries.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("Entry with Id " + id + " does not exist.");
        }
        entry.setName(name);
        entry.setValue(value);
        dataEntries.put(id, entry);
    }

    public void deleteEntry(int id) {
        DataEntry entry = dataEntries.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("Entry with Id " + id + " does not exist.");
        }
        dataEntries.remove(id);
    }

    public List<DataEntry> getAllEntries() {
        return new ArrayList<>(dataEntries);
    }
}