package com.dankideacentral.dic.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

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

    private NotificationCompat.Builder notificationBuilder;
    private TaskStackBuilder taskStackBuilder;
    private NotificationManager notificationManager;

    public NotificationHandler(final Context context, final Class activity) {
        notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_cloud_white_24dp)
                .setContentTitle(context.getString(R.string.app_name));
        notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

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
