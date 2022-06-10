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

        //달력 클릭시, 날짜 선택하고 표시&버튼조정, checkDiary 불러오기
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

        //저장 버튼 누를시 해당 루틴으로 이동, saveDiary 불러오기(스트링 저장)
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

    //데이터 받아오기
    private void loadData() {
        //데이터 받아오는 객체 생성
        dt_dbHepler = new DetailDataBaseHelper(getApplicationContext());
        //db에 저장
        SQLiteDatabase db = dt_dbHepler.getWritableDatabase();
        //db 열기
        dt_dbHepler.onCreate(db);
        //arraylist 클리어
        routineDetails.clear();
        //모든 데이터 가지고 오기, 커서를 이용해서 행별로 가지고 온다
        Cursor cursor = dt_dbHepler.getAllDataFromRoutine(parentRoutine.getId());
        //커서 이동하며 데이터 가져오기
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
            //arraylist에 저장
            routineDetails.add(detail);
        }
    }

    //선택된 날짜의 string 보여주기
    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";//해당 날짜의 성과률 txt 설정
        FileInputStream fis; //파일 읽고 저장

        try//파일을 읽고 저장할때는 꼭 try catch 사용
        {
            fis = openFileInput(readDay);  //파일 input

            byte[] fileData = new byte[fis.available()]; //파일 내용 담을 리스트 생성
            fis.read(fileData);//파일 읽기
            fis.close(); //파일 닫기

            str = new String(fileData); //파일의 문자열 저장

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            //수정시 ->저장버튼
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

            //삭제시 ->removeDiary 통해"" 파일에 저장 -> 버튼 표시
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