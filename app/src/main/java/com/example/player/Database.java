package com.example.player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    Context ctx;
    static String DB_name = "musicDB" , DB_table = "musicinfo" ,DB_title = "title" ,DB_id = "id",DB_count = "count",DB_pos = "position";
    static int DB_version = 1;
    public Database(@Nullable Context context) {
        super(context, DB_name, null, DB_version);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DB_table + " (" + DB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ DB_title + " TEXT," + DB_pos + " INTEGER," + DB_count +" INTEGER DEFAULT 0);");
        Toast.makeText(ctx, "DATA BASE IS CREATED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long store(String str,int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",str);
        values.put("count",1);
        values.put("position",pos);
        long res = db.insert(DB_table,null,values);
        db.close();
        return res;
    }
    public int getCountByPosition(int position) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DB_id, DB_title, DB_pos, DB_count};
        String selection = DB_pos + " = ?";
        String[] selectionArgs = {String.valueOf(position)};

        Cursor cursor = db.query(DB_table, columns, selection, selectionArgs, null, null, null);

        int count = 0;
        int index = cursor.getColumnIndex(DB_count);
        if (cursor.moveToFirst()) {
            do {
                count += cursor.getInt(index);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return count;
    }

    public ArrayList<Integer> readAllData() {
        ArrayList<Integer> posList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DB_pos};

        Cursor cursor = db.query(DB_table, columns, null, null, null, null, DB_count + " DESC");

        int posColumnIndex = cursor.getColumnIndex(DB_pos);

        if (cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(posColumnIndex);
                posList.add(position);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return posList;
    }




    public int updateCount(int pos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_count, DB_count + " + 1"); // Use the correct SQL expression to increment the count

        String whereClause = DB_pos + " = ?";
        String[] whereArgs = {String.valueOf(pos)};

        String updateSQL = "UPDATE " + DB_table + " SET " + DB_count + " = " + DB_count + " + 1 WHERE " + whereClause;

        db.execSQL(updateSQL, whereArgs);

        db.close();

        return 1;
    }




}
