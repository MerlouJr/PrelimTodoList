package com.example.cosain.prelimtodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskerDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "taskerManager";
	private static final String TABLE_TASKS = "tasks";
	private static final String KEY_ID = "id";
	private static final String KEY_TASKNAME = "taskName";
	private static final String KEY_STATUS = "status";

	public TaskerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASKNAME
				+ " TEXT, " + KEY_STATUS + " INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		onCreate(db);
	}


	public void addTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName());

		values.put(KEY_STATUS, task.getStatus());

		db.insert(TABLE_TASKS, null, values);
		db.close();
	}

	public List<Task> getAllTasks() {
		List<Task> taskList = new ArrayList<Task>();
		String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Task task = new Task();
				task.setId(cursor.getInt(0));
				task.setTaskName(cursor.getString(1));
				task.setStatus(cursor.getInt(2));

				taskList.add(task);
			} while (cursor.moveToNext());
		}

		return taskList;

	}

	public void updateTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName());
		values.put(KEY_STATUS, task.getStatus());
		db.update(TABLE_TASKS, values, KEY_ID + " = ?",new String[] {String.valueOf(task.getId())});
		db.close();
	}
	

	

}
