package swdm2016.gachon.mr_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class RoutineListViewAdapter extends BaseAdapter {
    private ArrayList<Routine> routines;
    private Context context;
    RoutineDataBaseHelper rt_dbHelper;
    DetailDataBaseHelper dt_dbHelper;
    AlarmHelper alarmHelper;

    public RoutineListViewAdapter(Context context, ArrayList<Routine> routines) {
        this.context = context;
        this.routines = routines;
        rt_dbHelper = new RoutineDataBaseHelper(context);
        dt_dbHelper = new DetailDataBaseHelper(context);
        alarmHelper = new AlarmHelper(context);
    }

    public void refresh() {
        routines.clear();
        Cursor cursor = rt_dbHelper.getAllData();
        while(cursor.moveToNext()) {
            Routine routine = new Routine(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7) , cursor.getInt(8), cursor.getInt(9));
            routines.add(routine);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return routines.size();
    }

    @Override
    public Object getItem(int i) {
        return routines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.routine_list, viewGroup, false);

        //루틴 타이틀 표시
        TextView textview_routine_title = view.findViewById(R.id.textview_title);
        textview_routine_title.setText(routines.get(i).getTitle());

        //루틴 시각 표시
        TextView routine_time = view.findViewById(R.id.textview_time);
        routine_time.setText(timeToText(routines.get(i).getStart_hour(), routines.get(i).getStart_minutes(), routines.get(i).getEnd_hour(), routines.get(i).getEnd_minutes()));

        //루틴 요일 표시
        String days = routines.get(i).getDays();

        TextView sun = view.findViewById(R.id.day_Sun_textView);
        TextView mon = view.findViewById(R.id.day_Mon_textView);
        TextView tue = view.findViewById(R.id.day_Tue_textView);
        TextView wed = view.findViewById(R.id.day_Wed_textView);
        TextView thu = view.findViewById(R.id.day_Thu_textView);
        TextView fri = view.findViewById(R.id.day_Fri_textView);
        TextView sat = view.findViewById(R.id.day_Sat_textView);

        TextView[] days_textview = { sun, mon, tue, wed, thu, fri, sat };

        for (int index = 0; index < days.length(); index++) {
            if(days.charAt(index) == '1') {
                days_textview[index].setTypeface(null, Typeface.BOLD);
                days_textview[index].setTextColor(R.color.main);
            }
        }

        //메뉴 버튼
        ImageView btn_menu = view.findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.del:
                                Log.d("TEST", "CLICK!!!!!");
                                Log.d("TEST", routines.get(i).getId()+"");
                                rt_dbHelper.deleteData(routines.get(i).getId());
                                dt_dbHelper.deleteDataFromRoutine(routines.get(i).getId());
                                alarmHelper.unregist_alarm(routines.get(i));
                                refresh();
                                break;
                        }

                        return false;
                    }
                });
            }
        });

        return view;
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

        return String.format("%02d:%02d%s ~ %02d:%02d%s", start_hour, start_minute, start_ampm, end_hour, end_minute, end_ampm);
    }
}
