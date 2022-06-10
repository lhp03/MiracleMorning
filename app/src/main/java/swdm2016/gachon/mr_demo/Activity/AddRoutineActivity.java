package swdm2016.gachon.mr_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import swdm2016.gachon.mr_demo.Helper.AlarmHelper;
import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.Routine;
import swdm2016.gachon.mr_demo.Helper.RoutineDataBaseHelper;

public class AddRoutineActivity extends AppCompatActivity {
    private static String TAG = "AddRoutineActivity";

    RoutineDataBaseHelper rt_dbHelper;
    AlarmHelper alarmHelper;

    EditText edit_title;

    TimePicker timePicker_start;
    TimePicker timePicker_end;

    CheckBox chk_sun;
    CheckBox chk_mon;
    CheckBox chk_tue;
    CheckBox chk_wed;
    CheckBox chk_thu;
    CheckBox chk_fri;
    CheckBox chk_sat;

    LinearLayout layout_alarm_setting;
    CheckBox chk_start_alarm;
    CheckBox chk_end_alarm;

    RadioGroup rd_group_end_time;

    Button btn_add;
    Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        initView();
    }

    private void initView() {
        //타이틀
        edit_title = findViewById(R.id.edit_routine_title);

        //시간 선택
        timePicker_start = findViewById(R.id.timepicker_start);
        timePicker_end = findViewById(R.id.timepicker_end);

        //요일 체크버튼
        chk_sun = findViewById(R.id.chk_sun);
        chk_mon = findViewById(R.id.chk_mon);
        chk_tue = findViewById(R.id.chk_tue);
        chk_wed = findViewById(R.id.chk_wed);
        chk_thu = findViewById(R.id.chk_thu);
        chk_fri = findViewById(R.id.chk_fri);
        chk_sat = findViewById(R.id.chk_sat);

        //시작 종료 알람 설정 부분
        layout_alarm_setting = findViewById(R.id.layout_alarm_setting);
        chk_start_alarm = findViewById(R.id.chk_start_alarm);
        chk_end_alarm = findViewById(R.id.chk_end_alarm);

        rd_group_end_time = findViewById(R.id.rd_group_end_time);

        chk_end_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    layout_alarm_setting.addView(rd_group_end_time);
                } else {
                    layout_alarm_setting.removeView(rd_group_end_time);
                }
            }
        });


        //확인 버튼
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_title.getText().toString().trim();

                if(title.equals("")) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_LONG).show();
                    return;
                }

                int start_hour = timePicker_start.getHour();
                int start_minute = timePicker_start.getMinute();
                int end_hour = timePicker_end.getHour();
                int end_minute = timePicker_end.getMinute();
                String days = getDays();
                int start_alarm = 0;
                int end_alarm = 0;
                int end_alarm_time = 0;

                if (chk_start_alarm.isChecked()) start_alarm = 1;
                if (chk_end_alarm.isChecked()) {
                    end_alarm = 1;
                    switch(rd_group_end_time.getCheckedRadioButtonId()) {
                        case R.id.rd_three:
                            end_alarm_time = 3;
                            break;
                        case R.id.rd_five:
                            end_alarm_time = 5;
                            break;
                        case R.id.rd_ten:
                            end_alarm_time = 10;
                            break;
                        case R.id.rd_fifteen:
                            end_alarm_time = 15;
                            break;
                        case R.id.rd_thirty:
                            end_alarm_time = 30;
                            break;
                    }
                }

                insertRoutine(new Routine(title, start_hour, start_minute, end_hour, end_minute, days, start_alarm, end_alarm, end_alarm_time));

                finish();
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

    }

    private String getDays() {
        CheckBox[] chk_days = {chk_sun, chk_mon, chk_tue, chk_wed, chk_thu, chk_fri, chk_sat};

        String days = "";

        for (int i = 0; i < chk_days.length; i++) {
            if(chk_days[i].isChecked()) {
                days += "1";
            } else {
                days = days + "0";
            }
        }

        return days;
    }

    private void insertRoutine(Routine routine) {
        rt_dbHelper = new RoutineDataBaseHelper(getApplicationContext());
        int id =  (int)rt_dbHelper.insertData(routine);
        routine.setId(id);

        alarmHelper = new AlarmHelper(getApplicationContext());
        alarmHelper.regist_alarm(routine);

    }
}