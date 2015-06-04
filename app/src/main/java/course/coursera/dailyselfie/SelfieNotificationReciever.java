package course.coursera.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Guillermo Celarie on 03/06/2015.
 */
public class SelfieNotificationReciever extends BroadcastReceiver {
    private static final String TAG = "SelfieNotificationReceiver";
    private Intent mSelfieIntent;
    private PendingIntent mContentIntent;

    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie!";

    @Override
    public void onReceive(Context context, Intent intent) {
        mSelfieIntent = new Intent(context, MainActivity.class);
        mContentIntent = PendingIntent.getActivity(context, 0, mSelfieIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentIntent(mContentIntent)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());

    }
}
