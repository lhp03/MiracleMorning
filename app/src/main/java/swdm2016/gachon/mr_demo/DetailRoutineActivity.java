package swdm2016.gachon.mr_demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DetailRoutineActivity extends AppCompatActivity {
    private ArrayList<RoutineDetail> routineDetails;
    private DetailDataBaseHelper dt_dbHepler;

    private TextView textview_routine_title;
    private TextView textview_time;
    private Button btn_add;
    private ListView listview_routine_detail;
    private RoutineDetailListViewAdapter routineDetailListViewAdapter;

    private Routine parentRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_routine);

        routineDetails = new ArrayList();

        Intent intent = getIntent();
        parentRoutine = (Routine)intent.getSerializableExtra("routine");

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        routineDetailListViewAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        dt_dbHepler = new DetailDataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dt_dbHepler.getWritableDatabase();
        dt_dbHepler.onCreate(db);

        routineDetails.clear();
        Cursor cursor = dt_dbHepler.getAllDataFromRoutine(parentRoutine.getId());
        while(cursor.moveToNext()) {
            String type = cursor.getString(2);
            RoutineDetail detail = null;
            switch(type) {
                case "alarm":
                    detail = new RoutineDetail(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(6), cursor.getInt(7));
                    break;
                case "timer":
                    detail = new RoutineDetail(cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
                    break;
            }
            detail.setId(cursor.getInt(0));
            routineDetails.add(detail);
        }
    }

    private void initView() {
        //인텐트 받기
        Intent intent = getIntent();
        Routine routine = (Routine)intent.getSerializableExtra("routine");

        //루틴 타이틀
        textview_routine_title = findViewById(R.id.textview_routine_title);
        textview_routine_title.setText(routine.getTitle());

        //루틴 시간
        textview_time = findViewById(R.id.textview_time);
        textview_time.setText(timeToText(routine.getStart_hour(), routine.getStart_minutes(), routine.getEnd_hour(), routine.getEnd_minutes()));

        //추가 버튼
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRoutineDetailActivity.class);
                intent.putExtra("routine", parentRoutine);
                startActivity(intent);
            }
        });

        //루틴 세부사항 리스트뷰
        listview_routine_detail = findViewById(R.id.listview_routine_detail);

        routineDetailListViewAdapter = new RoutineDetailListViewAdapter(getApplicationContext(), routineDetails);
        listview_routine_detail.setAdapter(routineDetailListViewAdapter);
        //루틴 세부사항 아이템 클릭리스너
        listview_routine_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(routineDetails.get(i).getIsDone() == 1) {
                    Toast.makeText(getApplicationContext(), "이미 완료한 루틴입니다.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(!isBetweenTime(LocalTime.now().getHour(), LocalTime.now().getMinute())) {
                    Toast.makeText(getApplicationContext(), "루틴을 수행 가능한 시간이 아닙니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                RoutineDetail detail = routineDetails.get(i);
                String type = detail.getType();

                switch(type) {
                    case "alarm":
                        doAlarm(detail);
                        break;
                    case "timer":
                        doTimer(detail);
                        break;
                }
            }
        });
    }

    private String timeToText(int start_hour, int start_minute, int end_hour, int end_minute) {
        String start_ampm = "am";
        String end_ampm = "am";

        if(start_hour > 12) {
            start_hour %= 12;
            start_ampm = "pm";
        }

        if(end_hour > 12) {
            end_hour %= 12;
            end_ampm = "pm";
        }

        return String.format("%d:%02d%s ~ %02d:%02d%s", start_hour, start_minute, start_ampm, end_hour, end_minute, end_ampm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doAlarm(RoutineDetail detail) {
        LocalTime toDoTime = LocalTime.of(detail.getHour(), detail.getMinutes(), 0);
        LocalTime now = LocalTime.now();
        int dif = (int)ChronoUnit.MINUTES.between(now, toDoTime);

        if(Math.abs(dif) < 5) {
            dt_dbHepler.updateIsDone(detail.getId(), 1);
            detail.setIsDone(1);
            routineDetailListViewAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(getApplicationContext(), "정해진 시간에서 5분 내에 수행할 수 있습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private void doTimer(RoutineDetail detail) {
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        intent.putExtra("detail", detail);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isBetweenTime(int hour, int minute) {
        LocalTime start = LocalTime.of(parentRoutine.getStart_hour(), parentRoutine.getStart_minutes());
        LocalTime end = LocalTime.of(parentRoutine.getEnd_hour(), parentRoutine.getEnd_minutes());
        LocalTime want = LocalTime.of(hour, minute);

        return (want.isAfter(start) && want.isBefore(end)) || (want.equals(start) || want.equals(end));
    }
}