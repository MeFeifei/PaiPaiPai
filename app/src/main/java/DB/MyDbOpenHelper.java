package DB;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by feifei on 2016/11/13.
 */

public class MyDbOpenHelper extends SQLiteOpenHelper {
    static String dbName = "user.db";
    static int dbVersion = 1;
    public MyDbOpenHelper(Context context){
        super(context,dbName,null,dbVersion);
    }
    public MyDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, null, version);
    }

    public MyDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, dbName, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id Integer primary key autoincrement,username varchar(20),password varchar(20) not null,phone varchar(16),gender varchar(8),personalsign varchar(20),email varchar(20),videopath varchar(32))");
        String sql = "create table video(id Integer primary key autoincrement,videoname varchar(16),videopath varchar(32) not null unique,owner varchar(20) not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
