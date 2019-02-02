package com.example.android.bakingapp.RecipeData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private Context mContext;
    private ArrayList<Ingredient> mIngredientList;

    public IngredientAdapter(Context mContext, ArrayList<Ingredient> mIngredientList) {
        this.mContext = mContext;
        this.mIngredientList = mIngredientList;
    }

    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapterViewHolder holder, int position) {
        Ingredient currentIngredient = mIngredientList.get(position);
        holder.measure_tv.setText(currentIngredient.getMeasure());
        holder.ingredientNameTv.setText(currentIngredient.getName());
        holder.quantity_tv.setText(String.valueOf(currentIngredient.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null){
            return 0;
        }
        return mIngredientList.size();
    }


    class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_list_item_tv)
        TextView ingredientNameTv;
        @BindView(R.id.ingredient_measure_tv)
        TextView measure_tv;
        @BindView(R.id.ingredient_quantity_tv)
        TextView quantity_tv;

        public IngredientAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);;
        }
    }
}
