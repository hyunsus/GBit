package kr.ac.hs.beet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Pocketpet.db";

    //DIARY_db
    private static final String SQL_CREATE_DIARY =
            "CREATE TABLE " + Diary.TABLE_NAME + " (" +
                    Diary.DIARY_ID + " INTEGER PRIMARY KEY," +
                    Diary.SENTENCE + " TEXT," +
                    Diary.DATE + " TEXT," +
                    Diary.Image + " TEXT)";




    private static final String SQL_DELETE_DIARY =
            "DROP TABLE IF EXISTS " + Diary.TABLE_NAME;


    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DIARY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DIARY);

        onCreate(sqLiteDatabase);
    }
}
