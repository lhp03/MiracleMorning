package swdm2016.gachon.mr_demo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import swdm2016.gachon.mr_demo.Routine;

public class RoutineDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "RoutineDataBaseHelper";

    private static final String DATABASE_NAME = "routine.db";
    private static final String ROUTINE_TABLE_NAME = "routine";

    //컬럼
    private static final String COL_1 = "routine_id";
    private static final String COL_2 = "routine_title";
    private static final String COL_3 = "start_hour";
    private static final String COL_4 = "start_minute";
    private static final String COL_5 = "end_hour";
    private static final String COL_6 = "end_minute";
    private static final String COL_7 = "days";
    private static final String COL_8 = "start_alarm";
    private static final String COL_9 = "end_alarm";
    private static final String COL_10 = "end_alarm_time";

    public RoutineDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + ROUTINE_TABLE_NAME + " ("
                + COL_1 + " INTEGER primary key autoincrement, "
                + COL_2 + " TEXT, "
                + COL_3 + " INTEGER, "
                + COL_4 + " INTEGER, "
                + COL_5 + " INTEGER, "
                + COL_6 + " INTEGER, "
                + COL_7 + " TEXT, "
                + COL_8 + " INTEGER, "
                + COL_9 + " INTEGER, "
                + COL_10 + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists " + ROUTINE_TABLE_NAME;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ROUTINE_TABLE_NAME, null);
        return cursor;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + ROUTINE_TABLE_NAME + " where routine_id=" + id, null);
        return cursor;
    }

    public long insertData(Routine routine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, routine.getTitle());
        contentValues.put(COL_3, routine.getStart_hour());
        contentValues.put(COL_4, routine.getStart_minutes());
        contentValues.put(COL_5, routine.getEnd_hour());
        contentValues.put(COL_6, routine.getEnd_minutes());
        contentValues.put(COL_7, routine.getDays());
        contentValues.put(COL_8, routine.getStart_alarm());
        contentValues.put(COL_9, routine.getEnd_alarm());
        contentValues.put(COL_10, routine.getEnd_alarm_time());

        long result = db.insert(ROUTINE_TABLE_NAME, null, contentValues);

        return result;
    }

    public int deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ROUTINE_TABLE_NAME,  COL_1 + " = ?", new String[]{String.valueOf(id)});
    }
}
