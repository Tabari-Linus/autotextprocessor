package lii.autotexttprocessor.model;

import java.util.Objects;

public class DataEntry {
    private int id;
    private String name;
    private String value;

    public DataEntry(int id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntry dataEntry = (DataEntry) o;
        return id == dataEntry.id && Objects.equals(name, dataEntry.name) && Objects.equals(value, dataEntry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }
}