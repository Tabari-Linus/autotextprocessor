package lii.autotexttprocessor.service;

import lii.autotexttprocessor.model.DataEntry;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataManagementService is responsible for managing data entries.
 * It provides methods to add, update, delete, and retrieve data entries.
 */

public class DataManagementService {
    private final Map<Integer, DataEntry> dataEntries = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Adds a new data entry to the collection.
     *
     * @param entry The DataEntry object to be added.
     * @throws IllegalArgumentException if an entry with the same ID already exists.
     */
    public void addEntry(DataEntry entry) {
        if (dataEntries.containsKey(entry.getId())) {
            throw new IllegalArgumentException("Entry with Id " + entry.getId() + " already exists.");
        }
        dataEntries.put(entry.getId(), entry);
    }

    /**
     * Updates an existing data entry.
     *
     * @param id    The ID of the entry to be updated.
     * @param name  The new name for the entry.
     * @param value The new value for the entry.
     * @throws IllegalArgumentException if the entry with the specified ID does not exist.
     */
    public void updateEntry(int id, String name, String value) {
        DataEntry entry = dataEntries.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("Entry with Id " + id + " does not exist.");
        }
        entry.setName(name);
        entry.setValue(value);
        dataEntries.put(id, entry);
    }

    /**
     * Deletes a data entry from the collection.
     *
     * @param id The ID of the entry to be deleted.
     * @throws IllegalArgumentException if the entry with the specified ID does not exist.
     */
    public void deleteEntry(int id) {
        DataEntry entry = dataEntries.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("Entry with Id " + id + " does not exist.");
        }
        dataEntries.remove(id);
    }

    /**
     * Retrieves a data entry by its ID.
     *
     * @param id The ID of the entry to be retrieved.
     * @return The DataEntry object with the specified ID, or null if not found.
     */
    public DataEntry getEntryById(int id) {
        return dataEntries.get(id);
    }

    /**
     * Retrieves all data entries.
     *
     * @return A list of all DataEntry objects.
     */
    public List<DataEntry> getAllEntries() {
        if (dataEntries.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(dataEntries.values());
    }

    /**
     * Loads initial data entries for demonstration purposes.
     *
     * @return A list of initial DataEntry objects.
     */
    public List<DataEntry> loadInitialEntries() {
        List<DataEntry> initialEntries = new ArrayList<>();
        initialEntries.add(new DataEntry(1, "Sample Entry 1", "Value 1"));
        initialEntries.add(new DataEntry(2, "Sample Entry 2", "Value 2"));
        initialEntries.add(new DataEntry(3, "Sample Entry 3", "Value 3"));
        return initialEntries;
    }

    public int getNextId() {
        return idCounter.getAndIncrement();
    }

}