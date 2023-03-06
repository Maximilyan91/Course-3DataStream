package com.cooking.course3_recipeApp.service.impl;

import com.cooking.course3_recipeApp.exception.ValidationException;
import com.cooking.course3_recipeApp.model.Recipe;
import com.cooking.course3_recipeApp.service.FileService;
import com.cooking.course3_recipeApp.service.RecipeService;
import com.cooking.course3_recipeApp.service.ValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final FileService fileService;
    private static Map<Integer, Recipe> recipes = new HashMap<>();
    private static int id = 0;
    private final ValidationService validationService;

    public RecipeServiceImpl(FileService fileService, ValidationService validationService) {
        this.fileService = fileService;
        this.validationService = validationService;
    }

    @PostConstruct
    public void init() {
        readFromFile();
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        if (!validationService.validate(recipe)) {
            throw new ValidationException(recipe.toString());
        }
        saveToFile();
        return recipes.put(id++, recipe);
    }

    @Override
    public Optional<Recipe> getRecipe(int id) {
        return Optional.ofNullable(recipes.get(id));
    }

    @Override
    public Recipe update(int id, Recipe recipe) {
        if (!validationService.validate(recipe)) {
            throw new ValidationException(recipe.toString());
        }
        saveToFile();
        return recipes.replace(id, recipe);
    }


    @Override
    public Recipe delete(int id) {
        return recipes.remove(id);
    }

    @Override
    public Map<Integer, Recipe> getAll() {
        return recipes;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipes);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void readFromFile() {
        String json = fileService.readFromFile();
        try {
            recipes = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Recipe>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
