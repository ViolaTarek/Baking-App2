package com.example.android.bakingapp.RecipeData;

import java.io.Serializable;
import java.util.List;



public class Recipe implements Serializable{
    private int mId;
    private String mName;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    public Recipe(int mId, String mName, List<Ingredient> ingredients, List<Step> steps) {
        this.mId = mId;
        this.mName = mName;
        this.mIngredients = ingredients;
        this.mSteps = steps;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }
}
