package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import entity.Person;

/**
 * Created by feifei on 2016/11/13.
 */

public class UserDAO  {
    private MyDbOpenHelper myDbOpenHelper;
    public  UserDAO (Context context){
        myDbOpenHelper = new MyDbOpenHelper(context);
    }

    /**
     * 插入一行数据
     * @param person
     * @return
     */
    public boolean insert(Person person){
        boolean isInsert = false;
        SQLiteDatabase db = myDbOpenHelper.getWritableDatabase();
        String sql = "insert into user(username,password,phone) values(?,?,?)";
        try {
            db.execSQL(sql,new String[]{person.getUsername(),person.getPassword(),person.getPhone()});
            db.close();
            isInsert = true;
            Log.i("Insert:",person.getUsername()+","+person.getPassword()+","+person.getPhone());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("insert error",e.getMessage());
        }
        return isInsert;

    }

    /**
     * 更新本地数据库
     * @param person
     * @return
     */
    public boolean update(Person person){
        SQLiteDatabase db = myDbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化ContentValues
        //添加要更改的字段及内容
        Log.i("更新时:",person.getPhone()+","+person.getUsername()+","+person.getPersonalSign());
        cv.put("username",person.getUsername());
        cv.put("password",person.getPassword());
        cv.put("phone",person.getPhone());
        cv.put("gender",person.getGender());
        cv.put("personalsign",person.getPersonalSign());
        cv.put("email","824542613@qq.com");
        cv.put("videopath",person.getVideoPath());
        String whereClause = "phone=?";//修改条件
        String[] whereArgs = {person.getPhone()};//修改条件的参数
        try {
            db.update("user",cv,whereClause,whereArgs);//执行修改
            cv.clear();
            db.close();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("update error",e.getMessage());
            return false;
        }

    }

    /**
     * 读取Person类
     * @param person
     * @return
     */
    public boolean read(Person person){
        SQLiteDatabase db = myDbOpenHelper.getReadableDatabase();
        String sql = "select * from user where phone=?";
        //查询获得游标
        Cursor cursor = db.rawQuery(sql,new String[]{person.getPhone()});
        //Cursor cursor = db.query("user",new String[]{"username","password","phone","gender","personalsign","email","videopath"},"id=?",new String[]{"1"},null,null,null);

        try {
            if (cursor.moveToFirst()){
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                String personalsign = cursor.getString(cursor.getColumnIndex("personalsign"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String videopath = cursor.getString(cursor.getColumnIndex("videopath"));

                person.setAllInfo(username,password,phone,gender,personalsign,email,videopath);

            }
            cursor.close();
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("read error",e.getMessage());
            return false;
        }
    }

    /**
     * 判断person类是否已在数据库中
     * @param phone
     * @return
     */
    public boolean personIsExist(String phone){
        SQLiteDatabase sdb=myDbOpenHelper.getReadableDatabase();
        String sql="select * from user where phone=?";
        Cursor cursor=sdb.rawQuery(sql, new String[]{phone});
        if((cursor.moveToFirst())){
            cursor.close();
            sdb.close();
            return true;
        }
        return false;
    }

}
