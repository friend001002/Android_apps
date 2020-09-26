package com.silentmodetoggle.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;
import com.silentmodetoggle.SilentModeHelper;
import com.silentmodetoggle.R;

public class AppWidgetService extends IntentService
{
    private static String ACTION_DO_TOGGLE = "actionDoToggle";

    AudioManager audioManager;

    public AppWidgetService()
    {
        super("AppWidgetService");
    }

    @Override
    public void onCreate()
    {
        // Always call super.onCreate
        super.onCreate();
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent!=null && intent.getBooleanExtra(ACTION_DO_TOGGLE, false))
        {
            SilentModeHelper.toggleSilentMode(audioManager);
        }

        AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        ComponentName name = new ComponentName(this, AppWidget.class);
        mgr.updateAppWidget(name, updateUi());
    }

    private RemoteViews updateUi()
    {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.app_widget);

        int phoneImage = SilentModeHelper.isPhoneSilent(audioManager)
                ? R.drawable.ringer_off
                : R.drawable.ringer_on;

        remoteViews.setImageViewResource(R.id.phone_state, phoneImage);

        Intent intent = new Intent(this, AppWidgetService.class).putExtra(ACTION_DO_TOGGLE,true);

        PendingIntent pendingIntent = PendingIntent.getService(  this, 0, intent
                                                               , PendingIntent.FLAG_ONE_SHOT);

        remoteViews.setOnClickPendingIntent(R.id.phone_state, pendingIntent);
        return remoteViews;
    }
}
