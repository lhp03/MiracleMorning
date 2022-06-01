package swdm2016.gachon.mr_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DetailDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DetailDataBaseHelper";

    private static final String DATABASE_NAME = "routine.db";
    private static final String DETAIL_TABLE_NAME = "detail";

    //컬럼
    private static final String COL_1 = "detail_id";
    private static final String COL_2 = "title";
    private static final String COL_3 = "type";
    private static final String COL_4 = "hour";
    private static final String COL_5 = "minute";
    private static final String COL_6 = "runtime";
    private static final String COL_7 = "isDone";
    private static final String COL_8 = "routine_id";

    public DetailDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DETAIL_TABLE_NAME + " ("
                + COL_1 + " INTEGER primary key autoincrement, "
                + COL_2 + " TEXT, "
                + COL_3 + " TEXT, "
                + COL_4 + " INTEGER, "
                + COL_5 + " INTEGER, "
                + COL_6 + " INTEGER, "
                + COL_7 + " INTEGER, "
                + COL_8 + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists " + DETAIL_TABLE_NAME;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DETAIL_TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllDataFromRoutine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DETAIL_TABLE_NAME + " where " + COL_8 + " = ?", new String[]{String.valueOf(id)});
        return cursor;
    }

    public long insertData(RoutineDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, detail.getTitle());
        contentValues.put(COL_3, detail.getType());

        switch(detail.getType()) {
            case "alarm" :
                contentValues.put(COL_4, detail.getHour());
                contentValues.put(COL_5, detail.getMinutes());
                break;
            case "timer":
                contentValues.put(COL_6, detail.getRuntime());
                break;
            default:
                Log.d(TAG, "unknown type");
        }

        contentValues.put(COL_7, detail.getIsDone());
        contentValues.put(COL_8, detail.getRoutine_id());

        long result = db.insert(DETAIL_TABLE_NAME, null, contentValues);

        return result;
    }

    public int deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DETAIL_TABLE_NAME,  COL_1 + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteDataFromRoutine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DETAIL_TABLE_NAME,  COL_8 + " = ?", new String[]{String.valueOf(id)});
    }

    public void updateIsDone(int id, int isDone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, isDone);
        db.update(DETAIL_TABLE_NAME, contentValues, "detail_id = ?", new String[]{String.valueOf(id)});
    }

    public void initIsDone(int routine_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, 0);
        db.update(DETAIL_TABLE_NAME, contentValues, "routine_id = ?", new String[]{String.valueOf(routine_id)});
    }
}
