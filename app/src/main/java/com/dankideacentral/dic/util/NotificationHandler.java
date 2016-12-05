package com.dankideacentral.dic.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;

import com.dankideacentral.dic.R;

/**
 * Class:   NotificationHandler.java
 * Purpose: Util to send application push
 *          notifications.
 *
 * @author Chris Ermel & Basim Ramadhan
 * @version 1.0
 * @since 2016-11-20
 */
public class NotificationHandler {
    public final static int NOTIF_CLUSTER = 0;
    public final static int NOTIF_CELEBRITY = 1;
    public final static int NOTIF_FOLLOWER = 2;
    public final static int NOTIF_FOLLOWING = 3;

    private NotificationCompat.Builder notificationBuilder;
    private TaskStackBuilder taskStackBuilder;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    public NotificationHandler(final Context context, final Class activity) {
        notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_cloud_white_24dp)
                .setContentTitle(context.getString(R.string.app_name));
        notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        // Get user's notification preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean vibrateOnNotification = preferences.getBoolean("pref_key_vibrate_on_notification", false);
        boolean lightOnNotification = preferences.getBoolean("pref_key_light_on_notification", false);
        String ringtone = preferences.getString("pref_key_notification_ringtone", null);

        // Apply user's notification preferences
        if(vibrateOnNotification) {
            notificationBuilder.setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 });
        }
        if(lightOnNotification) {
            int color = ContextCompat.getColor(context, R.color.migrateGreen);
            int duration = 3000;
            notificationBuilder.setLights(color, duration, duration);
        }
        if(ringtone != null) {
            notificationBuilder.setSound(Uri.parse(ringtone)); // TODO: Test that the ringtone works.
        }

        taskStackBuilder = TaskStackBuilder.create(context);
        setNotificationRedirectIntent(context, activity);
    }

    /**
     * Sends a notification to Android.
     *
     * @param message
     *          The string message that will appear
     *          in the notification.
     *
     * @param notificationId
     *          The identification number of the
     *          notification, for notification updating.
     */
    public void sendNotification(final String message, final int notificationId) {
        // Sets the notifications message
        notificationBuilder.setContentText(message);

        // Give the notification a unique ID so that it can be updated later
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    /**
     * Sets the intent/activity the notification will load
     * when clicked by the user.
     *
     * @param context
     *          The context from which the intent is
     *          being created.
     *
     * @param activity
     *          The activity to open on notification click.
     */
    public void setNotificationRedirectIntent(final Context context, final Class activity) {
        // Create concrete intent for notification to redirect to
        Intent redirectIntent = new Intent(context, activity);

        // Adds the back stack for the Intent (but not the Intent itself)
        taskStackBuilder.addParentStack(activity);

        // Adds the Intent that starts the Activity to the top of the stack
        taskStackBuilder.addNextIntent(redirectIntent);

        // Set the notification builder's pending intent
        notificationBuilder.setContentIntent(
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
