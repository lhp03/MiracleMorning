package swdm2016.gachon.mr_demo.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import swdm2016.gachon.mr_demo.Activity.AddRoutineActivity;
import swdm2016.gachon.mr_demo.Activity.DetailRoutineActivity;
import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.Routine;
import swdm2016.gachon.mr_demo.Helper.RoutineDataBaseHelper;
import swdm2016.gachon.mr_demo.Adapter.RoutineListViewAdapter;

public class AlarmFragment extends Fragment {
    private View view;
    ArrayList<Routine> routines;
    RoutineDataBaseHelper rt_dbHepler;

    private Button btn_add_routine;
    private ListView listview_routines;
    RoutineListViewAdapter routineListViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        routines = new ArrayList<Routine>();
        view = inflater.inflate(R.layout.fragment_alarm, container, false);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
        routineListViewAdapter.notifyDataSetChanged();

    }

    private void initView() {
        //루틴 추가 버튼
        btn_add_routine = view.findViewById(R.id.btn_add_routine);
        btn_add_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRoutineActivity.class);
                //TODO: 루틴 아이디 전달 추가
                startActivity(intent); //루틴 추가 액티비티 실행
            }
        });

        //리스트뷰 생성
        routineListViewAdapter = new RoutineListViewAdapter(getActivity(), routines);
        listview_routines = view.findViewById(R.id.listview_routines);
        listview_routines.setAdapter(routineListViewAdapter);

        //리스트뷰 아이템 클릭이벤트 설정
        listview_routines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailRoutineActivity.class);
                intent.putExtra("routine", routines.get(i));
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        rt_dbHepler = new RoutineDataBaseHelper(getActivity());
        SQLiteDatabase db = rt_dbHepler.getWritableDatabase();
        rt_dbHepler.onCreate(db);

        routines.clear();
        Cursor cursor = rt_dbHepler.getAllData();
        while(cursor.moveToNext()) {
            Routine routine = new Routine(cursor.getString(1), cursor.getInt(2), cursor.getInt(3),cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));
            routine.setId(cursor.getInt(0));
            routines.add(routine);
        }
    }
}