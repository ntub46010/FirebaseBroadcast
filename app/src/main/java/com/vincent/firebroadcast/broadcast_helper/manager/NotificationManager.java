package com.vincent.firebroadcast.broadcast_helper.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.vincent.firebroadcast.MainActivity;
import com.vincent.firebroadcast.R;

public class NotificationManager {
    public static final int NOTIFICATION_ID = -Integer.MAX_VALUE;
    private static NotificationManager INSTANCE = null;

    private NotificationManager() {

    }

    public synchronized static NotificationManager getInstance() {
        if (INSTANCE == null) {
            return new NotificationManager();
        }
        return INSTANCE;
    }

    public void generateNotification(Context context, Bitmap bitmap, String title, String message) {
        //onResourceReady
        Notification notification = createNotification(context, bitmap, title, message);
        notifyNotification(context, notification);
    }

    public void generateNotification(Context context, String title, String message) {
        //onLoadFailed
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); //通知大圖示
        Notification notification = createNotification(context, bitmap, title, message);
        notifyNotification(context, notification);
    }

    private Notification createNotification(Context context, Bitmap bitmap, String title, String message) {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        //NotificationCompat.Style notificationStyle = createNotificationStyle(message, bitmap);
        Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                //.setStyle(notificationStyle) 將大圖示圖片附加在下面
                .build();
        notification.flags |= Intent.FLAG_ACTIVITY_SINGLE_TOP;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        return notification;
    }

    /*
    private NotificationCompat.Style createNotificationStyle(String message, Bitmap picBitmap) {
        //顯示大圖片
        NotificationCompat.Style style;

        if (picBitmap == null) {
            style = new NotificationCompat.BigTextStyle().bigText(message);
        } else {
            style = new NotificationCompat.BigPictureStyle().bigPicture(picBitmap).setSummaryText(message);
        }

        return style;
    }
    */

    private void notifyNotification(Context context, Notification notification) {
        if (notification != null) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}
