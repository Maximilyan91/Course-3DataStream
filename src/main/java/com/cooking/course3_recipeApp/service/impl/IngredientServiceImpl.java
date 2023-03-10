package com.cooking.course3_recipeApp.service.impl;

import com.cooking.course3_recipeApp.exception.ValidationException;
import com.cooking.course3_recipeApp.model.Ingredient;
import com.cooking.course3_recipeApp.service.IngredientService;
import com.cooking.course3_recipeApp.service.ValidationService;
import com.cooking.course3_recipeApp.service.fileServiceimpl.FileServiceIngredientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final FileServiceIngredientImpl fileService;
    private static int id = 1;

    private static Map<Integer, Ingredient> ingredients = new HashMap<>();
    private final ValidationService validationService;

    public IngredientServiceImpl(FileServiceIngredientImpl fileService, ValidationService validationService) {
        this.fileService = fileService;
        this.validationService = validationService;
    }
    @PostConstruct
    public void init() {
        readFromFile();
    }


    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        if (!validationService.validate(ingredient)) {
            throw new ValidationException(ingredient.toString());
        }
        Ingredient addedIngredient = ingredients.put(id++, ingredient);
        saveToFile();
        return addedIngredient;
    }

    @Override
    public Optional<Ingredient> getIngredient(int id) {
        return Optional.ofNullable(ingredients.get(id));
    }

    @Override
    public Ingredient update(int id, Ingredient ingredient) {
        if (!validationService.validate(ingredient)) {
            throw new ValidationException(ingredient.toString());
        }
        Ingredient updatedIngredient = ingredients.replace(id, ingredient);
        saveToFile();
        return updatedIngredient;
    }

    @Override
    public Ingredient delete(int id) {
        return ingredients.remove(id);
    }

    @Override
    public Map<Integer, Ingredient> getAll() {
        return ingredients;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(ingredients);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void readFromFile() {
        String json = fileService.readFromFile();
        try {
            ingredients = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Ingredient>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
