package com.example.studymate.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ReminderManager {

    public static void setReminder(Context context, int id, String title, long targetTimeInMs) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("REMINDER_ID", id);
        intent.putExtra("REMINDER_TITLE", title);

        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags);

        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Berjalan akurat tanpa memicu crash aturan canScheduleExactAlarms
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTimeInMs, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetTimeInMs, pendingIntent);
                }
            } catch (SecurityException e) {
                // Solusi otomatis jika sistem Android menolak izin presisi, langsung dialihkan ke alarm standar
                alarmManager.set(AlarmManager.RTC_WAKEUP, targetTimeInMs, pendingIntent);
                Log.e("ReminderManager", "Izin exact ditolak. Menggunakan fallback alarm standar.", e);
            }
        }
    }

    public static void cancelReminder(Context context, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);

        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}