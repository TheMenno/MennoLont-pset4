package com.example.menno_000.mennolont_pset4;

/**
 * Created by menno_000 on 27-9-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Static strings.
    private static final String DATABASE_NAME = "ContactDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COMPLETED = "completed";
    private static final String TABLE = "contactTable";

    // Constructor.
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE
                + " TEXT NOT NULL," + KEY_COMPLETED + " TEXT NOT NULL)";

        db.execSQL(CREATE_DB);
    }

    // Upgrade the database when a helper object is made and there is one already.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }


    // CRUD methods: Create, Read, Update, Delete

    public void create(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        //onUpgrade(db, 1, 1);
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_COMPLETED, todo.getCompleted());
        db.insert(TABLE, null, values);
        db.close();
    }

    public ArrayList<ToDo> read() {
        SQLiteDatabase db = getReadableDatabase();

        // A list of custom objects, to store our data.
        ArrayList<ToDo> todoes = new ArrayList<>();

        // Create a query to give to the cursor.
        String query = "SELECT " + KEY_ID + ", " + KEY_TITLE + ", " + KEY_COMPLETED + " FROM " + TABLE;
        Cursor cursor = db.rawQuery(query, null);

        // Set cursor to the beginning of the database.
        if (cursor.moveToFirst()){
            do {
                // add id, done-status and next item from current row to NextItemList.
                String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                String completed = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED));
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));

                // Create a to do object with the newly retrieved data.
                ToDo todo = new ToDo(title, completed, id);
                todoes.add(todo);
            }
            // While there is still a next entry.
            while (cursor.moveToNext());
        }

        // Close the database and the cursor.
        cursor.close();
        db.close();
        return todoes;
    }

    public int update(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_COMPLETED, todo.getCompleted());

        // Return whether it has succeeded or not.
        return db.update(TABLE, values, KEY_ID + " = ? ", new String[] { String.valueOf(todo.getID())});
    }

    public void delete(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " " + KEY_ID + " = ? ", new String[] {String.valueOf(todo.getID())});
        db.close();
    }
}
