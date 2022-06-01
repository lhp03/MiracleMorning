package swdm2016.gachon.mr_demo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private RoutineDataBaseHelper rt_dbHelper;
    private AlarmHelper alarmHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        rt_dbHelper = new RoutineDataBaseHelper(context);


        Cursor cursor = rt_dbHelper.getData(id);
        cursor.moveToFirst();

        Routine routine = new Routine(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));

        alarmHelper = new AlarmHelper(context);
        alarmHelper.regist_alarm(routine);

        Log.d(TAG, "Command : " + intent.getStringExtra("command"));
        Intent service_intent = new Intent(context, AlarmService.class);
        service_intent.putExtra("routine", routine);
        service_intent.putExtra("command", intent.getStringExtra("command"));
        context.startService(service_intent);
    }
}