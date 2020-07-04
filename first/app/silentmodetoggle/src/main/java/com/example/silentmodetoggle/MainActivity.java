package com.example.silentmodetoggle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.view.View;
import android.media.AudioManager;
import android.app.NotificationManager;
import android.os.Build;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends AppCompatActivity
{
    AudioManager audioManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get a reference to Android’s AudioManager so we can use
        // it to toggle our ringer.
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        setContentView(R.layout.activity_main);

        context = (Context)getApplicationContext();

        // Ask the user for Do Not Disturb Permission.
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted())
        {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        // Find the view with the ID "content" in our layout file.
        FrameLayout contentView = (FrameLayout)findViewById(R.id.content);

        // Create a click listener for the contentView that will toggle
        // the phone’s ringer state, and then update the UI to reflect
        // the new state.
        contentView.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    // Toggle the ringer mode.  If it’s currently normal,
                    // make it silent.  If it’s currently silent,
                    // do the opposite.
                    RingerHelper.performToggle(audioManager);
                    // Update the UI to reflect the new state
                    updateUi();
                }
            }
        );
    }

    /**
     * Updates the UI image to show an image representing silent or
     * normal, as appropriate
     */
    private void updateUi()
    {
        // Find the view named phone_icon in our layout.  We know it’s
        // an ImageView in the layout, so downcast it to an ImageView.
        ImageView imageView = (ImageView) findViewById(R.id.phone_icon);

        // Set phoneImage to the ID of image that represents ringer on
        // or off.  These are found in res/drawable-xxhdpi
        int phoneImage = RingerHelper.isPhoneSilent(audioManager)
                ? R.drawable.ringer_off
                : R.drawable.ringer_on;

        // Set the imageView to the image in phoneImage
        imageView.setImageResource(phoneImage);
    }
    /**
     * Every time the activity is resumed, make sure to update the
     * buttons to reflect the current state of the system (since the
     * user may have changed the phone’s silent state while we were in
     * the background).
     *
     * Visit http://d.android.com/reference/android/app/Activity.html
     * for more information about the Android Activity lifecycle.
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        // Update our UI in case anything has changed.
        updateUi();
    }
}
