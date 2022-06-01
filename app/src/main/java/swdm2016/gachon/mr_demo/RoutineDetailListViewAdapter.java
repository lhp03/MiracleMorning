package swdm2016.gachon.mr_demo;

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
import android.widget.Toast;

import java.time.LocalTime;
import java.util.ArrayList;

public class RoutineDetailListViewAdapter extends BaseAdapter {
    private ArrayList<RoutineDetail> routineDetails;
    private Context context;
    private DetailDataBaseHelper dt_dbHelper;

    public RoutineDetailListViewAdapter(Context context, ArrayList<RoutineDetail> routineDetails) {
        this.context = context;
        this.routineDetails = routineDetails;
        this.dt_dbHelper = new DetailDataBaseHelper(context);
    }

    public void refresh() {
        routineDetails.clear();
        Cursor cursor = dt_dbHelper.getAllData();

        while(cursor.moveToNext()) {
            String type = cursor.getString(2);
            RoutineDetail detail = null;
            switch(type) {
                case "alarm":
                    detail = new RoutineDetail(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(6), cursor.getInt(7));
                    break;
                case "timer":
                    detail = new RoutineDetail(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
                    break;
            }
            routineDetails.add(detail);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return routineDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return routineDetails.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.routinedetail_list, viewGroup, false);


        //완료 이미지 표시
        ImageView imageView = view.findViewById(R.id.imageView);
        if(routineDetails.get(i).getIsDone() == 1) {
            imageView.setImageResource(R.drawable.check);
        } else {
            imageView.setImageResource(R.drawable.not_check);
        }

        //루틴 세부사항 타이틀 표시
        TextView textview_routine_title = view.findViewById(R.id.textview_title);
        textview_routine_title.setText(routineDetails.get(i).getTitle());

        String type = routineDetails.get(i).getType();
        TextView routine_time = view.findViewById(R.id.textview_time);

        switch(type) {
            case "alarm":
                //알람 타입 실행 시각 표시
                int hour = routineDetails.get(i).getHour();
                int minutes = routineDetails.get(i).getMinutes();

                routine_time.setText(timeToText(hour,minutes));
                break;
            case "timer":
                int runtime = routineDetails.get(i).getRuntime();
                String runtime_result = runtime +"분";
                routine_time.setText(runtime_result);
            default:
                break;
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
                                Log.d("TEST", routineDetails.get(i).getId()+"");
                                dt_dbHelper.deleteData(routineDetails.get(i).getId());
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

    private String timeToText(int hour, int minute) {
        String ampm = "am";

        if(hour > 12) {
            hour %= 12;
            ampm = "pm";
        }

        return String.format("%02d:%02d%s", hour, minute, ampm);
    }
}
