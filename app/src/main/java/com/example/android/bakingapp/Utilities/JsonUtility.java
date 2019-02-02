package com.example.android.bakingapp.Utilities;

import com.example.android.bakingapp.RecipeData.Ingredient;
import com.example.android.bakingapp.RecipeData.Recipe;
import com.example.android.bakingapp.RecipeData.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonUtility {

//keys for Json
    private static final String J_KEY_RECIPE_ID = "id";
    private static final String J_KEY_RECIPE_NAME = "name";
    private static final String J_KEY_INGREDIENTS_ARRAY_NAME = "ingredients";
    private static final String J_KEY_INGREDIENT_QUANTITY = "quantity";
    private static final String J_KEY_INGREDIENT_MEASURE = "measure";
    private static final String J_KEY_INGREDIENT_NAME = "ingredient";
    private static final String J_KEY_STEPS_ARRAY_NAME = "steps";
    private static final String J_KEY_STEP_ID = "id";
    private static final String J_KEY_STEP_SHORT_DESCRIPTION = "shortDescription";
    private static final String J_KEY_STEP_DESCRIPTION = "description";
    private static final String J_KEY_STEP_VIDEO_URL = "videoURL";
    private static final String J_KEY_STEP_THUMBNAIL_URL = "thumbnailURL";

    public static ArrayList<Recipe> readJsonArray(JSONArray jsonArray){
        int numberOfItems = jsonArray.length();
        if (numberOfItems < 1){
            return null;
        }

        int itemId = 0;
        String itemName = "";
        ArrayList<Recipe> recipeList = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++){
            try {
                JSONObject itemObject = jsonArray.getJSONObject(i);

                itemId = i;
                itemName = itemObject.getString(J_KEY_RECIPE_NAME);

                JSONArray ingredientsArray = itemObject.getJSONArray(J_KEY_INGREDIENTS_ARRAY_NAME);
                ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
                int numberOfIngredients = ingredientsArray.length();
                for (int j = 0; j < numberOfIngredients; j++){
                    JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                    Double ingredientQuantity = ingredientObject.getDouble(J_KEY_INGREDIENT_QUANTITY);
                    String ingredientMeasure = ingredientObject.getString(J_KEY_INGREDIENT_MEASURE);
                    String ingredientName = ingredientObject.getString(J_KEY_INGREDIENT_NAME);
                    ingredientList.add(new Ingredient(ingredientQuantity,ingredientMeasure,ingredientName));
                }

                JSONArray stepsArray = itemObject.getJSONArray(J_KEY_STEPS_ARRAY_NAME);
                ArrayList<Step> stepList = new ArrayList<Step>();
                int numberOfSteps = stepsArray.length();
                for (int j = 0; j < numberOfSteps; j++){
                    JSONObject stepObject = stepsArray.getJSONObject(j);
                    int stepId = stepObject.getInt(J_KEY_STEP_ID);
                    String stepShortDescription = stepObject.getString(J_KEY_STEP_SHORT_DESCRIPTION);
                    String stepDescription = stepObject.getString(J_KEY_STEP_DESCRIPTION);
                    String stepVideoUrl = stepObject.getString(J_KEY_STEP_VIDEO_URL);
                    String stepThumbnailURL = stepObject.getString(J_KEY_STEP_THUMBNAIL_URL);
                    stepList.add(new Step(stepId, stepShortDescription,stepDescription,stepVideoUrl,stepThumbnailURL));
                }
                recipeList.add(new Recipe(itemId, itemName, ingredientList,stepList));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return recipeList;
    }
}
