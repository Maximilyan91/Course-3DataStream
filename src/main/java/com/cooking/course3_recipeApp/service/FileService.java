package com.cooking.course3_recipeApp.service;

public interface FileService {
    boolean saveToFile(String json);

    String readFromFile();

    boolean cleanDataFile();
}
