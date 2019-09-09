package com.example.termtrackerandroid;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class Notifications extends BroadcastReceiver {

    public static final String courseNotifAlert = "courseNotification";
    public static final String assessNotifAlert = "assessNotification";
    public static final String notifAlert = "notifAlert";
    public static final String notifField = "notifFieldID";

    @Override
    public void onReceive(Context context, Intent intent) {
        String destination = intent.getStringExtra("destination");
        if (destination == null || destination.isEmpty()) {
            destination = "";
        }

        long ID = intent.getIntExtra("ID", 0);
        String notifTitle = intent.getStringExtra("title");
        String notifText = intent.getStringExtra("text");
        int notifFieldID = intent.getIntExtra("notifFieldID", getIncUpNotifID(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notifi_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifText);
        Intent resInt;
        Uri uri;
        SharedPreferences sharedPreferences;

        switch (destination) {
            case "course":
                Courses course = DM.getCourse(context, ID);
                if (course != null && course.notifi == 1) {
                    resInt = new Intent(context, CourseView.class);
                    uri = Uri.parse(DP.COURSES_URI + "/" + ID);
                    resInt.putExtra(DP.COURSE_CONTENT_TYPE, uri);
                } else {
                    return;
                }
                break;
            case "assessment":
                Assessments assessment = DM.getAssess(context, ID);
                if (assessment != null && assessment.notifi == 1) {
                    resInt = new Intent(context, AssessView.class);
                    uri = Uri.parse(DP.ASSESS_URI + "/" + ID);
                    resInt.putExtra(DP.ASSESS_CONTENT_TYPE, uri);
                } else {
                    return;
                }
                break;
            default:
                resInt = new Intent(context, MainActivity.class);
                break;
        }

        TaskStackBuilder stkBuild = TaskStackBuilder.create(context);
        stkBuild.addParentStack(MainActivity.class);
        stkBuild.addNextIntent(resInt);
        PendingIntent rsltPendInt = stkBuild.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(rsltPendInt).setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifFieldID, builder.build());
    }

    public static boolean scheduleCourseNotifi(Context context, long ID, long time, String title, String text) {
        AlarmManager notifManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int notifFieldID = getUpNotifID(context);
        Intent intNotif = new Intent(context, Notifications.class);
        intNotif.putExtra("ID", ID);
        intNotif.putExtra("title", title);
        intNotif.putExtra("text", text);
        intNotif.putExtra("destination", "course");
        intNotif.putExtra("notifFieldID", notifFieldID);
        notifManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, notifFieldID, intNotif, PendingIntent.FLAG_ONE_SHOT));

        SharedPreferences sp = context.getSharedPreferences(courseNotifAlert, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(ID), notifFieldID);
        editor.commit();

        incUpNotifID(context);
        return true;
    }

    public static boolean scheduleAssessNotifi(Context context, int ID, long time, String tile, String text) {
        AlarmManager notifManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int notifFieldID = getUpNotifID(context);
        Intent intNotif = new Intent(context, Notifications.class);
        intNotif.putExtra("ID", ID);
        intNotif.putExtra("title", tile);
        intNotif.putExtra("text", text);
        intNotif.putExtra("destination", "assessment");
        intNotif.putExtra("notifFieldID", notifFieldID);
        notifManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intNotif, PendingIntent.FLAG_UPDATE_CURRENT));

        SharedPreferences sp = context.getSharedPreferences(assessNotifAlert, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(ID), notifFieldID);
        editor.commit();

        incUpNotifID(context);
        return true;
    }

    private static int getUpNotifID(Context context) {
        SharedPreferences notifPrefs;
        notifPrefs = context.getSharedPreferences(notifAlert, Context.MODE_PRIVATE);
        int notifFieldID = notifPrefs.getInt(notifField, 1);
        return notifFieldID;
    }

    private static void incUpNotifID(Context context) {
        SharedPreferences notifPrefs;
        notifPrefs = context.getSharedPreferences(notifAlert, Context.MODE_PRIVATE);
        int notifFieldID = notifPrefs.getInt(notifField, 1);
        SharedPreferences.Editor notifEditor = notifPrefs.edit();
        notifEditor.putInt(notifField, notifFieldID + 1);
        notifEditor.commit();
    }

    private static int getIncUpNotifID(Context context) {
        int notifFieldID = getUpNotifID(context);
        incUpNotifID(context);
        return notifFieldID;

    }
}
