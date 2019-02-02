package com.example.android.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeData.Recipe;
import com.example.android.bakingapp.RecipeDetailsActivity;
import com.example.android.bakingapp.Widget.WidgetService;
import com.google.gson.Gson;


public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String widgetTitle) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_view, pendingIntent);

        Intent listIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, listIntent);

        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        PendingIntent detailPendingIntent = PendingIntent.getActivity(context, 0,
                appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, detailPendingIntent);

        views.setTextViewText(R.id.widget_title_text, widgetTitle);


        views.setEmptyView(R.id.widget_list_view, R.id.empty_widget_text);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String widgetTitle = context.getString(R.string.default_widget_title);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getString(R.string.json_recipe_object))) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(context.getString(R.string.json_recipe_object), "");
            Recipe recipe = gson.fromJson(json, Recipe.class);
            widgetTitle = recipe.getName();
        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, widgetTitle);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] widgetIds, String recipeName) {
        for (int widgetId : widgetIds){
            updateAppWidget(context, appWidgetManager, widgetId, recipeName);
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {

    }
}

