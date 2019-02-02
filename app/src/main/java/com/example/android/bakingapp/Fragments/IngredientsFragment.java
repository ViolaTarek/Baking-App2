package com.example.android.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeData.Ingredient;
import com.example.android.bakingapp.RecipeData.IngredientAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsFragment extends Fragment {

    @BindView(R.id.ingredient_list_rv)
    RecyclerView ingredientRv;

    private static final String INTENT_EXTRA_INGREDIENT_ITEM = "ingredient_item_position";
    private static final String INTENT_EXTRA_INGREDIENT_LIST_KEY = "ingredient_list";
    private ArrayList<Ingredient> mIngredients;
    private IngredientAdapter mIngredientAdapter;

    public IngredientsFragment(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        ButterKnife.bind(this, rootView);

        mIngredients = (ArrayList)getArguments().getSerializable(INTENT_EXTRA_INGREDIENT_LIST_KEY);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientRv.setLayoutManager(layoutManager);
        mIngredientAdapter = new IngredientAdapter(getContext(), mIngredients);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),layoutManager.getOrientation());
        ingredientRv.addItemDecoration(dividerItemDecoration);
        ingredientRv.setAdapter(mIngredientAdapter);

        if (getArguments().containsKey(INTENT_EXTRA_INGREDIENT_ITEM)){
            int itemPosition = getArguments().getInt(INTENT_EXTRA_INGREDIENT_ITEM);
            ingredientRv.scrollToPosition(itemPosition);
        }

        return rootView;
    }


}
