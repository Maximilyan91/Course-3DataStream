package com.cooking.course3_recipeApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private String recipeName;
    private int cookingTime;
    private List<Ingredient> ingredient;
    private List<String> stepsCook;
}
