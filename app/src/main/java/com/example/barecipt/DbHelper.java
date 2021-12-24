package com.example.barecipt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    Context context;

    private static final String DATABASE_NAME = "db_resep";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tb_resep (id INTEGER PRIMARY KEY AUTOINCREMENT, nama_resep TEXT, lama_masakan TEXT," +
                "status_lama_masakan TEXT, pilihan TEXT, jenis TEXT, bahan TEXT, langkah TEXT)");
        db.execSQL("CREATE TABLE tb_user (id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT, email TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_resep");
        db.execSQL("DROP TABLE IF EXISTS tb_user");
    }

    public boolean cekEmail(String email){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_user WHERE email = ?", new String[]{email});
        if (cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean cekPassword(String password){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_user WHERE password = ?", new String[]{password});
        if (cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    public int cekEmailnPassword(String email, String password){
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_user WHERE email = ? AND password = ?", new String[]{email, password});
        while (cursor.moveToNext()) {
            id = Integer.valueOf(cursor.getString(0));
        }
        return id;
    }

    public boolean insertData (ReciptHandler resepHandler){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama_resep", resepHandler.getNamaResep());
        cv.put("lama_masakan",resepHandler.getLamaMemasak());
        cv.put("status_lama_masakan",resepHandler.getStatusLamaMemasak());
        cv.put("pilihan",resepHandler.getPilihan());
        cv.put("jenis",resepHandler.getJenis());
        cv.put("bahan",resepHandler.getBahan());
        cv.put("langkah",resepHandler.getLangkah());
        return db.insert("tb_resep", null, cv) > 0;
    }

    public Cursor showData(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM tb_resep", null);
    }

    public Cursor showDetail(int id_resep) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from tb_resep where id = " + id_resep, null);
    }

    public boolean editData(ReciptHandler resepHandler, int id_resep){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama_resep", resepHandler.getNamaResep());
        cv.put("lama_masakan",resepHandler.getLamaMemasak());
        cv.put("status_lama_masakan",resepHandler.getStatusLamaMemasak());
        cv.put("pilihan",resepHandler.getPilihan());
        cv.put("jenis",resepHandler.getJenis());
        cv.put("bahan",resepHandler.getBahan());
        cv.put("langkah",resepHandler.getLangkah());
        return db.update("tb_resep", cv, "id" + "=" + id_resep, null) > 0;
    }

    public boolean deleteData(int id_resep){
        SQLiteDatabase db = getReadableDatabase();
        return db.delete("tb_resep", "id" + "=" + id_resep, null) > 0;
    }

    public void deleteSemuaData (){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM tb_resep");
        db.close();
    }
}
