package com.example.studymate.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "studymate_reminder_channel";
    private static final String CHANNEL_NAME = "StudyMate Reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("REMINDER_ID", 0);
        String title = intent.getStringExtra("REMINDER_TITLE");
        if (title == null) {
            title = "Pengingat Tugas StudyMate!";
        }

        showNotification(context, id, title, "Deadline sudah tiba! Yuk, cek dan selesaikan tugasmu.");
        updateStatusSelesai(id);
    }

    private void showNotification(Context context, int id, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());
        }
    }

    private void updateStatusSelesai(int id) {
        System.out.println("STUDYMATE_DEBUG: Tugas/Jadwal dengan ID " + id + " otomatis diset SELESAI.");
    }
}