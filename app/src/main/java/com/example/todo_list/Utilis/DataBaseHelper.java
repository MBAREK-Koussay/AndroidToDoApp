package com.example.todo_list.Utilis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo_list.Model.TodoMoadel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final int DATABASE_VERSION = 1;
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " INTEGER)";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertTask(TodoMoadel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, model.getTask());
        contentValues.put(COL_3, 0); // Default status is 0
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateTask(int id, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, status);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)});
        db.close();
    }
        public List<TodoMoadel> getAllTasks() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            List<TodoMoadel> modelList = new ArrayList<>();

            db.beginTransaction(); // Begin transaction
            try {
                // Query to fetch all rows from the table
                cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) { // Check if the cursor has rows
                        do {
                            // Create a new ToDoMoadel instance for each row
                            TodoMoadel toDoMoadel = new TodoMoadel();
                            // Safely retrieve values using cursor methods
                            toDoMoadel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1))); // Set ID
                            toDoMoadel.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2))); // Set Task
                            toDoMoadel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3))); // Set Status

                            modelList.add(toDoMoadel); // Add the model to the list
                        } while (cursor.moveToNext()); // Move to the next row
                    }
                }
                db.setTransactionSuccessful(); // Mark transaction as successful
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Always close the cursor and end the transaction
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                db.endTransaction();
                db.close(); // Close the database
            }

            return modelList; // Return the list of tasks
        }

    public void updateTaskStatus(int id, int i) {
    }
}
