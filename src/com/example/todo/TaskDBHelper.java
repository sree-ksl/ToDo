package com.example.todo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "taskManager";
	public static final String TABLE_TASKS = "tasks";
	
	//column names
	public static final String KEY_ID = "id";
	public static final String KEY_TASKNAME = "taskname";
	public static final String KEY_STATUS = "status";

	public TaskDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " + KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASKNAME + "TEXT" + KEY_STATUS + "INTEGER)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    //drop older table if exists 
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASKS);
	    //create new table again
		onCreate(db);
		
	}
	
	public void addTask(Task task)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_TASKNAME,task.getTaskName()); //task name
		// Status of tasks can be 0 for not done and 1 for done
		cv.put(KEY_STATUS, task.getStatus());
		
		//insert row
		db.insert(TABLE_TASKS, null, cv);
		db.close(); //close database connection
	}
	
	// Read operation
	
	public List<Task>getAllTasks()
	{
		List<Task> taskList = new ArrayList<Task>();
		//Select all query
		String selectQuery = "SELECT * FROM" +TABLE_TASKS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//loop through all rows and add to list
		if(cursor.moveToFirst()){
			do{
				Task task = new Task();
				task.setId(cursor.getInt(0));
				task.setTaskName(cursor.getString(1));
				task.setStatus(cursor.getInt(2));
				
				//Adding contact to list
				taskList.add(task);
			}while(cursor.moveToNext());
		}
		return taskList;	
	}
	
	//update status of task
	
	public void updateTask(Task task)
	{
	  SQLiteDatabase dataB = this.getWritableDatabase();
	  ContentValues cv = new ContentValues();
	  cv.put(KEY_TASKNAME, task.getTaskName());
	  cv.put(KEY_STATUS, task.getStatus());
	  dataB.update(TABLE_TASKS, cv, KEY_ID + " = ? ", new String[]{String.valueOf(task.getId())});
	  dataB.close();
	}

}
