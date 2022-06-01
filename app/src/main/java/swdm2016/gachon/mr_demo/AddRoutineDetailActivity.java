package swdm2016.gachon.mr_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class AddRoutineDetailActivity extends AppCompatActivity {
    private static String TAG = "AddRoutineDetailActivity";

    RadioGroup radioGroup;

    LinearLayout layout_alarm;
    LinearLayout layout_timer;

    EditText edit_title;

    TimePicker timePicker;
    NumberPicker numberPicker;

    Button btn_exit;
    Button btn_add;

    DetailDataBaseHelper rt_dbHelper;

    Routine parentRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine_detail);

        loadData();
        initView();
    }

    private void initView() {
        //레이아웃
        layout_alarm = findViewById(R.id.layout_alarm);
        layout_timer = findViewById(R.id.layout_timer);

        //EditText
        edit_title = findViewById(R.id.edit_detail_title);

        //Picker
        timePicker = findViewById(R.id.timepicker);
        numberPicker = findViewById(R.id.numberpicker);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(1);

        //라디오 버튼 그룹
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radio_btn_alarm:
                        layout_alarm.setVisibility(View.VISIBLE);
                        layout_timer.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio_btn_timer:
                        layout_alarm.setVisibility(View.INVISIBLE);
                        layout_timer.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_btn_read:
                        break;
                }
            }
        });

        //취소 버튼
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //확인 버튼
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                RoutineDetail detail = null;
                String title = edit_title.getText().toString().trim();

                if(title.equals("")) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_LONG).show();
                    return;
                }

                String type = "";
                int isDone = 0;

                switch(radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_btn_alarm :
                        type = "alarm";
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if(isBetweenTime(hour, minute)) {
                                detail = new RoutineDetail(title, type, hour, minute, isDone, parentRoutine.getId());
                            } else {
                                Toast.makeText(getApplicationContext(), "정해진 시간 내에서 입력해주세요", Toast.LENGTH_LONG).show();
                            }
                        }
                        break;

                    case R.id.radio_btn_timer:
                        type = "timer";
                        int runtime = numberPicker.getValue();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if(isBetweenTime(runtime)) {
                                detail = new RoutineDetail(title, type, runtime, isDone, parentRoutine.getId());
                            } else {
                                Toast.makeText(getApplicationContext(), "타이머 시간이 루틴의 시간을 초과합니다.", Toast.LENGTH_LONG).show();
                            }
                        }

                        break;
                }

                if(detail != null) {
                    insertRoutineDetail(detail);
                    finish();
                }
            }
        });
    }

    private void loadData() {
        parentRoutine = (Routine)getIntent().getSerializableExtra("routine");
    }


    private void insertRoutineDetail(RoutineDetail detail) {
        rt_dbHelper = new DetailDataBaseHelper(getApplicationContext());
        rt_dbHelper.insertData(detail);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isBetweenTime(int hour, int minute) {
        LocalTime start = LocalTime.of(parentRoutine.getStart_hour(), parentRoutine.getStart_minutes());
        LocalTime end = LocalTime.of(parentRoutine.getEnd_hour(), parentRoutine.getEnd_minutes());
        LocalTime want = LocalTime.of(hour, minute);

        return (want.isAfter(start) && want.isBefore(end)) || (want.equals(start) || want.equals(end));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isBetweenTime(int runtime) {
        LocalTime start = LocalTime.of(parentRoutine.getStart_hour(), parentRoutine.getStart_minutes());
        LocalTime end = LocalTime.of(parentRoutine.getEnd_hour(), parentRoutine.getEnd_minutes());

        long diff = ChronoUnit.MINUTES.between(start, end);

        if((int)diff > runtime) return true;
        else return false;
    }
}