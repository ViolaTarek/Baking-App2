package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.bakingapp.Fragments.IngredientsFragment;
import com.example.android.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.android.bakingapp.Fragments.StepDetailsFragment;
import com.example.android.bakingapp.RecipeData.Ingredient;
import com.example.android.bakingapp.RecipeData.Recipe;
import com.example.android.bakingapp.RecipeData.Step;
import com.example.android.bakingapp.Widget.IngredientsWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;



public class RecipeDetailsActivity extends AppCompatActivity
implements RecipeDetailsFragment.OnItemClickListener{

    private static final int NO_INGREDIENT_POSITION_SPECIFIED = -1;
    private static final String INTENT_EXTRA_INGREDIENT_ITEM = "ingredient_item_position";
    private static final String INTENT_EXTRA_RECIPE_KEY = "recipe_object";
    private static final String INTENT_EXTRA_STEP_LIST_KEY = "step_list";
    private static final String INTENT_EXTRA_INGREDIENT_LIST_KEY = "ingredient_list";
    private static final String INTENT_EXTRA_STEPS_KEY = "steps";
    private static final String INTENT_EXTRA_STEP_SELECTED_KEY = "step_clicked";

    private static final String SAVE_INSTANCE_RECEIVED_RECIPE = "received_recipe";

    @Nullable
    @BindView(R.id.step_ingredient_list)
    FrameLayout tabletFl;

    private Recipe rcvRecipe;
    private Toast mToast;
    private boolean openInTablet;

    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        rcvRecipe = (Recipe)intent.getSerializableExtra(INTENT_EXTRA_RECIPE_KEY);
        setTitle(rcvRecipe.getName());
     
        removeFragmentsFromBackstack();
        if (intent.hasExtra(INTENT_EXTRA_INGREDIENT_ITEM)){
            startIngredientsFragment(intent.getIntExtra(INTENT_EXTRA_INGREDIENT_ITEM,
                    NO_INGREDIENT_POSITION_SPECIFIED));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_view);

        openInTablet = getResources().getBoolean(R.bool.open_in_tablet);

        //Create new fragments if there is no previously saved state
        if (savedInstanceState == null) {
            Intent starterIntent = getIntent();
            rcvRecipe = (Recipe)starterIntent.getSerializableExtra(INTENT_EXTRA_RECIPE_KEY);
            setTitle(rcvRecipe.getName());

            if (openInTablet) {
                if (getActionBar() != null) {
                    getActionBar().setDisplayHomeAsUpEnabled(true);
                }
                startIngredientsFragment(starterIntent.getIntExtra(INTENT_EXTRA_INGREDIENT_ITEM,
                        NO_INGREDIENT_POSITION_SPECIFIED));
                startDetailsFragment();
            } else {
                if (starterIntent.hasExtra(INTENT_EXTRA_INGREDIENT_ITEM)) {
                    startIngredientsFragment(starterIntent.getIntExtra(INTENT_EXTRA_INGREDIENT_ITEM,
                            NO_INGREDIENT_POSITION_SPECIFIED));
                } else {
                    startDetailsFragment();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_set_on_widget:
                setCurrentRecipeOnWidget();
                showToast(rcvRecipe.getName() + getString(R.string.widget_load_text));
                break;
            case android.R.id.home:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setCurrentRecipeOnWidget(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rcvRecipe);
        preferenceEditor.putString(getString(R.string.json_recipe_object), json);
        preferenceEditor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, rcvRecipe.getName());
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_INSTANCE_RECEIVED_RECIPE, rcvRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rcvRecipe = (Recipe)savedInstanceState.getSerializable(SAVE_INSTANCE_RECEIVED_RECIPE);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    

    private void startIngredientsFragment(int recipeItemPosition){
        Bundle args = new Bundle();
        ArrayList<Ingredient> ingredients = (ArrayList)rcvRecipe.getIngredients();
        args.putSerializable(INTENT_EXTRA_INGREDIENT_LIST_KEY, ingredients);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (recipeItemPosition != -1){
            args.putInt(INTENT_EXTRA_INGREDIENT_ITEM, recipeItemPosition);
        } else {
            fragmentTransaction.addToBackStack(null);
        }
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setArguments(args);

        fragmentTransaction.replace(R.id.recipe_detail_container, ingredientsFragment).commit();
    }

    private void startDetailsFragment(){
        ArrayList<Step> steps = (ArrayList)rcvRecipe.getSteps();
        RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(INTENT_EXTRA_STEP_LIST_KEY, steps);
        detailsFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (openInTablet) {
            fragmentTransaction.add(R.id.step_ingredient_list, detailsFragment)
                    .commit();
        } else {
            fragmentTransaction.add(R.id.recipe_detail_container, detailsFragment)
                    .commit();
        }

    }

    @Override
    public void onIngredientsClicked() {
        startIngredientsFragment(NO_INGREDIENT_POSITION_SPECIFIED);

    }

  
    @Override
    public void onStepClicked(int stepNumber) {
        Bundle args = new Bundle();
        ArrayList<Step> steps = (ArrayList)rcvRecipe.getSteps();
        args.putSerializable(INTENT_EXTRA_STEPS_KEY, steps);
        args.putInt(INTENT_EXTRA_STEP_SELECTED_KEY, stepNumber);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.recipe_detail_container, stepDetailsFragment).commit();
    }

    private void removeFragmentsFromBackstack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i){
            fragmentManager.popBackStack();
        }
    }

    private void showToast(String message){
        if (mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
