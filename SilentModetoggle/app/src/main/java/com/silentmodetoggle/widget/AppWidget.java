package com.silentmodetoggle.widget;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class AppWidget extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int[] appWidgetIds)
    {
        context.startService(new Intent(context, AppWidgetService.class));
    }
}
