package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeData.Step;
import com.example.android.bakingapp.RecipeData.StepAdapter;
import com.example.android.bakingapp.Utilities.VolleyUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipeDetailsFragment extends Fragment implements
StepAdapter.StepListItemClickListener{

    @BindView (R.id.recipe_details_rv)
    RecyclerView stepRv;

    private static final String THUMBNAIL_IMAGE_REQUEST_TAG = "get_thumbnail_tag";
    private static final String INTENT_EXTRA_STEP_LIST_KEY = "step_list";


    private ArrayList<Step> mSteps;
    private StepAdapter mStepAdapter;

    OnItemClickListener mCallback;

    public interface OnItemClickListener {
        void onIngredientsClicked();
        void onStepClicked(int stepNumber);
    }

    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate
                (R.layout.fragment_recipe_detail_view, container, false);

        ButterKnife.bind(this, rootView);
        mSteps = (ArrayList)getArguments().getSerializable(INTENT_EXTRA_STEP_LIST_KEY);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        stepRv.setLayoutManager(layoutManager);
        mStepAdapter = new StepAdapter(getContext(), mSteps, this);
        stepRv.setAdapter(mStepAdapter);

        return rootView;
    }

    @Override
    public void onStepListItemClick(int clickedStep) {
        mCallback.onStepClicked(clickedStep);
    }

    @Override
    public void onIngredientsClick() {
        mCallback.onIngredientsClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " you have to implement OnItemClickListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelThumbnailDownloads();
    }

    private void cancelThumbnailDownloads(){
        VolleyUtility.getInstance(getContext()).cancelPendingRequests(THUMBNAIL_IMAGE_REQUEST_TAG);
    }
}
