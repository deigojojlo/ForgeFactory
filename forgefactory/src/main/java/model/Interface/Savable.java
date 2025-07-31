package main.java.model.Interface;

import main.java.model.Exception.InvalidSaveFormat;

public interface Savable {
    String save();

    void restore(String s) throws InvalidSaveFormat;
}
