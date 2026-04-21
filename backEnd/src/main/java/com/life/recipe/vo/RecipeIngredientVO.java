package com.life.recipe.vo;

import lombok.Data;

@Data
public class RecipeIngredientVO {
	
	private Long ingredientId;
	private Long recipeId;
	private String name;

}
