package swdm2016.gachon.mr_demo.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import swdm2016.gachon.mr_demo.Activity.DetailRoutineActivity;
import swdm2016.gachon.mr_demo.Helper.DetailDataBaseHelper;
import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.Routine;

public class AlarmService extends Service {
    private static String TAG = "AlarmService";

    private static final String PRIMARY_CHANNEL_ID = "primary_channel_id";
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;

    private Routine routine;
    private String command;

    public AlarmService() {

    }

    @Override
    public void onCreate() {
        createNotificationChannel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        routine = (Routine)intent.getSerializableExtra("routine");
        command = intent.getStringExtra("command");
        Log.d(TAG, "Command : " + command);
        sendNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Notification 채널 생성
    public void createNotificationChannel() {
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Routine Notification", notificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Routine Notification");

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = null;

        Intent todo_intent = new Intent(getApplicationContext(), DetailRoutineActivity.class);
        todo_intent.putExtra("routine", routine);
        Log.d(TAG, routine.getId() + "");

        PendingIntent pIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, todo_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (command.equals("start")) {
            // 수행 여부 초기화
            initIsDone();

            notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                    .setContentTitle("루틴 알림")
                    .setContentText(routine.getTitle() + " 루틴을 시작할 시간입니다.")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);
        } else if(command.equals("end")) {
            String message = "의 모든 활동을 완료하였습니다!";

            //수행 되지 않은 루틴 세부사항 확인
            DetailDataBaseHelper dt_dbHelper = new DetailDataBaseHelper(getApplicationContext());
            Cursor cursor = dt_dbHelper.getAllDataFromRoutine(routine.getId());

            while(cursor.moveToNext()) {
                if(cursor.getInt(6) == 0) {
                    message = " 중 아직 완료하지 않은 루틴 활동이 있습니다! 수행을 완료해주세요!";
                    break;
                }
            }

            notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                    .setContentTitle("루틴 알림")
                    .setContentText(routine.getTitle() + message)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);
        }

        return notifyBuilder;
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }


    public void initIsDone() {
        DetailDataBaseHelper dt_dbHelper = new DetailDataBaseHelper(getApplicationContext());
        dt_dbHelper.initIsDone(routine.getId());
    }
}