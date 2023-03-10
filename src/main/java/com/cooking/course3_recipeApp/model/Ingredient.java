package com.cooking.course3_recipeApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private String ingredientName;
    private int ingredientNum;
    private String unitOfMeasure;

}
