package swdm2016.gachon.mr_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class TimerActivity extends AppCompatActivity {
    private enum TimerStatus {
        STARTED,
        STOPPED,
        PAUSED
    }

    //TAG
    private String TAG = "TimerActivity";

    //View
    private TextView textview_title;
    private ProgressBar progressbar;
    private TextView textview_timer;
    private ImageButton btn_startpause;
    private ImageButton btn_stop;

    private CountDownTimer countDownTimer;
    private long timeInMil;
    private long remainTime;
    private TimerStatus timerStatus = TimerStatus.STOPPED;

    //db
    private DetailDataBaseHelper dt_dbHelper;

    //detail
    private RoutineDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        loadData();
        initView();
    }

    //뷰 초기화
    private void initView() {
        textview_title = findViewById(R.id.textview_title);
        textview_title.setText(detail.getTitle());

        progressbar = findViewById(R.id.progressbar);
        textview_timer = findViewById(R.id.textview_timer);
        textview_timer.setText(toTimeFormat(timeInMil));

        btn_startpause = findViewById(R.id.btn_start);
        btn_startpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerStartPause();
            }
        });

        btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerStop();
            }
        });
    }

    private void loadData() {
        dt_dbHelper = new DetailDataBaseHelper(getApplicationContext());
        detail = (RoutineDetail)getIntent().getSerializableExtra("detail");
        timeInMil = detail.getRuntime() * 1000 * 60;
        remainTime = detail.getRuntime() * 1000 * 60;
    }

    private void timerStartPause() {
        if(timerStatus == TimerStatus.STOPPED || timerStatus == TimerStatus.PAUSED) {
            timerStatus = TimerStatus.STARTED;
            setTimerValues();
            startCountDownTimer();
        } else {
            timerStatus = TimerStatus.PAUSED;
            stopCountDownTimer();
        }
    }

    private void timerStop() {
        timerStatus = TimerStatus.STOPPED;
        stopCountDownTimer();
    }

    private void setTimerValues() {
        progressbar.setMax((int)(timeInMil / 1000));
        progressbar.setProgress((int)(remainTime / 1000));
        textview_timer.setText(toTimeFormat(remainTime));
    }

    //Interval 1000으로 수정정
   private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(remainTime, 100) {
            @Override
            public void onTick(long l) {
                remainTime = l;
                textview_timer.setText(toTimeFormat(l));
                progressbar.setProgress((int)(l / 1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), detail.getTitle() + " 수행 완료!", Toast.LENGTH_LONG).show();
                timerStatus = TimerStatus.STOPPED;
                dt_dbHelper.updateIsDone(detail.getId(), 1);
                finish();
            }
        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if(timerStatus == TimerStatus.PAUSED) {
            countDownTimer.cancel();
        } else {
            remainTime = timeInMil;
            countDownTimer.cancel();
        }
        setTimerValues();
    }

    private String toTimeFormat(long milliSeconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }
}