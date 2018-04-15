package com.neher.ecl.learningapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 3/25/2018.
 */

public class QuestionDB extends SQLiteOpenHelper {

    private static final String TAG = "QuestionDB";

    static final int VERSION = 2;
    static final String DB_NAME = "learning";
    static final String TABLE_NAME = "questions";
    static final String COL_ID = "_id";
    static final String COL_SUB = "sub";

    static final String COL_QUESTION = "question";
    static final String COL_OPTION_A = "option_a";
    static final String COL_OPTION_B = "option_b";
    static final String COL_OPTION_C = "option_c";
    static final String COL_OPTION_D = "option_d";
    static final String COL_ANS = "ans";
    static final String COL_WEIGHT = "col_weight";
    static final String COL_STATUS = "col_status";

    private final String SQL_CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
            +COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_SUB+" INTEGER, "
            +COL_QUESTION+" VARCHAR, "
            +COL_OPTION_A+" VARCHAR, "
            +COL_OPTION_B+" VARCHAR, "
            +COL_OPTION_C+" VARCHAR, "
            +COL_OPTION_D+" VARCHAR, "
            +COL_ANS+" VARCHAR, "
            +COL_WEIGHT+" INTEGER, "
            +COL_STATUS+" BOOLEAN);";

    private final String ALL_QSNS = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_WEIGHT+" > 0";


    public QuestionDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        Log.d(TAG, "Database Create Successfully!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "Database Update Successfully!");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        // call onCreate method

    }



    public Cursor index()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery(ALL_QSNS,null);
        return result;
    }

    public Cursor getQuestion()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursors = db.rawQuery("SELECT * FROM TABLE_NAME WHERE COL_STATUS = false Limit 1", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+ " WHERE " +COL_STATUS+ " = 1 Limit 1", null);
        return cursor;
    }

    public long store(Question question){

        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionDB.COL_QUESTION, question.getQuestion());
        contentValues.put(QuestionDB.COL_SUB, question.getSubject());
        contentValues.put(QuestionDB.COL_OPTION_A, question.getOption_1());
        contentValues.put(QuestionDB.COL_OPTION_B, question.getOption_2());
        contentValues.put(QuestionDB.COL_OPTION_C, question.getOption_3());
        contentValues.put(QuestionDB.COL_OPTION_D, question.getOption_4());
        contentValues.put(QuestionDB.COL_ANS, question.getAns());
        contentValues.put(QuestionDB.COL_WEIGHT, question.getWeight());
        contentValues.put(QuestionDB.COL_STATUS, question.getStatus());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        Log.d(TAG, id+": "+question.getQuestion());

        return id;
    }

    public void update(int id)
    {
        String row =String.valueOf(id);
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, false);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long row_id = sqLiteDatabase.update(TABLE_NAME, cv, COL_ID+" = ?", new String[]{row});

        Log.d(TAG, "Update row: "+row_id);
    }
}
