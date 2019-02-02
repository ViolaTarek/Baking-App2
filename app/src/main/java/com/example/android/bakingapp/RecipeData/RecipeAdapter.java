package com.example.android.bakingapp.RecipeData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipeList;

    final private ListItemClickListener mOnClickListener;


    public interface ListItemClickListener{
        void onListItemClick(int clickedItemId);
    }

    public RecipeAdapter(Context mContext, ListItemClickListener listener) {
        this.mContext = mContext;
        mOnClickListener = listener;
    }


    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card_item,parent,false);
        return  new RecipeAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeList.get(position);
        holder.itemTextView.setText(currentRecipe.getName());

        int id = mRecipeList.get(position).getId();

            switch (mRecipeList.get(position).getId()) {
                case 0:
                    Picasso.with(mContext).load(R.drawable.nutellapie)
                            .into(holder.itemImage);
                    break;
                case 1:
                    Picasso.with(mContext).load(R.drawable.brownies)
                            .into(holder.itemImage);
                    break;
                case 2:
                    Picasso.with(mContext).load(R.drawable.yellowcake)
                            .into(holder.itemImage);
                    break;
                case 3:
                    Picasso.with(mContext).load(R.drawable.cheesecake)
                            .into(holder.itemImage);
                    break;
            }

    }

    public void swapList(ArrayList<Recipe> list){
        mRecipeList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null){
            return mRecipeList.size();
        }
        return 0;
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_item_iv)
        ImageView itemImage;
        @BindView(R.id.recipe_item_tv)
        TextView itemTextView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
