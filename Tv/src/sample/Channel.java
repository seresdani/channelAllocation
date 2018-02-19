package sample;

import javafx.beans.property.SimpleStringProperty;

public class Channel {

    private final SimpleStringProperty frek;
    private final SimpleStringProperty name;

    public Channel() {
        this.frek = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
    }

    public Channel(String ffrek, String fname) {
        this.frek = new SimpleStringProperty(ffrek);
        this.name = new SimpleStringProperty(fname);
    }

    public String getFrek() {
        return frek.get();
    }

    public String getName() {
        return name.get();
    }

    public void setFrek(String frek) {
        this.frek.set(frek);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
