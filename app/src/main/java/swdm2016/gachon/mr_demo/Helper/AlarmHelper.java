package swdm2016.gachon.mr_demo.Helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import swdm2016.gachon.mr_demo.Receiver.AlarmReceiver;
import swdm2016.gachon.mr_demo.Routine;

public class AlarmHelper {
    private static String TAG = "AlarmHelper";
    private Context context;
    private RoutineDataBaseHelper rt_dbHelper;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void regist_alarm(Routine routine) {
        //Alarm Manager에 알람 등록
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //시작 알람 시간
        long start_alarm_time = routine.getNextStartAlarm();
        long end_alarm_time = routine.getNextEndAlarm();

        //시작 알람 등록
        if(routine.getStart_alarm() == 1 && start_alarm_time != 0L) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("id", routine.getId());
            intent.putExtra("command", "start");
            PendingIntent pIntent = PendingIntent.getBroadcast(context, getStartAlarmId(routine.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start_alarm_time, pIntent);
            Log.d(TAG, routine.getId() + "시작 알람 등록 완료");
        }

        //종료 알람 등록
        if(routine.getEnd_alarm() == 1 && end_alarm_time != 0L) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("id", routine.getId());
            intent.putExtra("command", "end");
            PendingIntent pIntent = PendingIntent.getBroadcast(context, getEndAlarmId(routine.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, end_alarm_time, pIntent);
            Log.d(TAG, routine.getId() + "종료 알람 등록 완료");
        }
    }

    public void unregist_alarm(Routine routine) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //시작 알람 삭제
        if(routine.getStart_alarm() == 1) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, getStartAlarmId(routine.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);

            Log.d(TAG, "알람 삭제 완료");
        }

        if(routine.getEnd_alarm() == 1) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, getEndAlarmId(routine.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);

            Log.d(TAG, "알람 삭제 완료");
        }
    }

    public int getStartAlarmId(int id) {
        return Integer.parseInt(String.valueOf(id) + "0");
    }


    public int getEndAlarmId(int id) {
        return Integer.parseInt(String.valueOf(id) + "1");
    }
}
