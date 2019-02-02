package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.android.bakingapp.RecipeData.Recipe;
import com.example.android.bakingapp.RecipeData.RecipeAdapter;
import com.example.android.bakingapp.Utilities.JsonUtility;
import com.example.android.bakingapp.Utilities.VolleyUtility;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{

    private static final String REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String INTENT_EXTRA_RECIPE_KEY = "recipe_object";
    private static final int NUMBER_OF_GRID_COLUMNS = 3;
    ArrayList<Recipe> recipeList = null;

    @BindView(R.id.recipe_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.error_getting_data_tv)
    TextView mErrorTv;

    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getJsonResponse();

        if (getResources().getBoolean(R.bool.open_in_tablet)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_GRID_COLUMNS);

            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    private int getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void getJsonResponse(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                REQUEST_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        recipeList = JsonUtility.readJsonArray(response);
                        mRecipeAdapter.swapList(recipeList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        mErrorTv.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, R.string.failed_to_connect, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        VolleyUtility.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onListItemClick(int clickedItemId) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_RECIPE_KEY, recipeList.get(clickedItemId));
        startActivity(intent);
    }
}
