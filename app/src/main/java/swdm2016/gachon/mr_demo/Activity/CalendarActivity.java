package swdm2016.gachon.mr_demo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import swdm2016.gachon.mr_demo.Helper.DetailDataBaseHelper;
import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.Routine;
import swdm2016.gachon.mr_demo.RoutineDetail;
import swdm2016.gachon.mr_demo.Adapter.RoutineDetailListViewAdapter;

public class CalendarActivity extends AppCompatActivity {

    private ArrayList<RoutineDetail> routineDetails;
    private DetailDataBaseHelper dt_dbHepler;
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    private RoutineDetailListViewAdapter routineDetailListViewAdapter;
    private Routine parentRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        routineDetails = new ArrayList();
        Intent intent = getIntent();

        parentRoutine = (Routine)intent.getSerializableExtra("routine");
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);

        //?????? ?????????, ?????? ???????????? ??????&????????????, checkDiary ????????????
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);
            }
        });

        //?????? ?????? ????????? ?????? ???????????? ??????, saveDiary ????????????(????????? ??????)
        save_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveDiary(readDay);
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        routineDetailListViewAdapter.notifyDataSetChanged();
        textView2.setText(routineDetails.toString());
    }

    //????????? ????????????
    private void loadData() {
        //????????? ???????????? ?????? ??????
        dt_dbHepler = new DetailDataBaseHelper(getApplicationContext());
        //db??? ??????
        SQLiteDatabase db = dt_dbHepler.getWritableDatabase();
        //db ??????
        dt_dbHepler.onCreate(db);
        //arraylist ?????????
        routineDetails.clear();
        //?????? ????????? ????????? ??????, ????????? ???????????? ????????? ????????? ??????
        Cursor cursor = dt_dbHepler.getAllDataFromRoutine(parentRoutine.getId());
        //?????? ???????????? ????????? ????????????
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
            //arraylist??? ??????
            routineDetails.add(detail);
        }
    }

    //????????? ????????? string ????????????
    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";//?????? ????????? ????????? txt ??????
        FileInputStream fis; //?????? ?????? ??????

        try//????????? ?????? ??????????????? ??? try catch ??????
        {
            fis = openFileInput(readDay);  //?????? input

            byte[] fileData = new byte[fis.available()]; //?????? ?????? ?????? ????????? ??????
            fis.read(fileData);//?????? ??????
            fis.close(); //?????? ??????

            str = new String(fileData); //????????? ????????? ??????

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            //????????? ->????????????
            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });

            //????????? ->removeDiary ??????"" ????????? ?????? -> ?????? ??????
            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });
            if (textView2.getText() == null)
            {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}