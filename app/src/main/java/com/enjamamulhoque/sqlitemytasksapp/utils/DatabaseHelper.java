package com.enjamamulhoque.sqlitemytasksapp.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.enjamamulhoque.sqlitemytasksapp.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TASKS_DATABASE";
    private static final String TABLE_NAME = "TASKS_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertTask(TaskModel taskModel){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, taskModel.getTask());
        values.put(COL_3, 0);

        db.insert(TABLE_NAME, null, values);


    }

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);

        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)} );
    }

    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);

        db.update(TABLE_NAME, values, "ID=?" , new String[]{String.valueOf(id)});

    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?" , new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<TaskModel> getAllTasks(){
        List<TaskModel> taskModelList = new ArrayList<>();

        db = this.getWritableDatabase(); // Initialize the database here.
        Cursor cursor = null;

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        TaskModel taskModel = new TaskModel();
                        taskModel.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        taskModel.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        taskModel.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        taskModelList.add(taskModel);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }

        return taskModelList;
    }
















}
