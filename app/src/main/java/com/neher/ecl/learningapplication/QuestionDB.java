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

    static final int VERSION = 7;
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
            +COL_STATUS+" INTEGER);"; // 0=unread, 1 = read, 2 = error answer, 3 = right answer, 4= worm up qsn unread, 5 = worm up question read



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

        this.onCreate(sqLiteDatabase);

    }


    public Cursor index()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }

    public Cursor getWormUpQuestion()
    {
        Log.d(TAG, "getWormUpQuestion is calling");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_STATUS+" = "+Env.WORMUP_UNREAD_QUESTION+" LIMIT 1", null);
        return cursor;
    }


    public Cursor getQuestion()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_STATUS+" = "+Env.UNREAD_QUESTION+" Limit 1", null);
        if (!cursor.moveToFirst())
        {
            Log.d(TAG, "Read Question");
            cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_STATUS+" = "+Env.READ_QUESTION+" Limit 1", null);
        }

        if (!cursor.moveToFirst())
        {
            Log.d(TAG, "Error Question");
            cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_STATUS+" = "+Env.ERROR_ANS+" Limit 1", null);
        }


        return cursor;
    }

    public void update(int id, int status_code)
    {
        Log.d(TAG, "id: "+String.valueOf(id));
        String row_id =String.valueOf(id);

        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status_code);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long updated_row_id = sqLiteDatabase.update(TABLE_NAME, cv, COL_ID+" = ?", new String[]{row_id});
        Log.d(TAG, "Update row: "+updated_row_id+" Row id: "+row_id);
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
        Log.d(TAG, id+": "+question.getStatus());

        return id;
    }
}
