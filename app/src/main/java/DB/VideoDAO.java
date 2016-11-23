package DB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import entity.Person;
import entity.VideoItemBean;

/**
 * Created by feifei on 2016/11/20.
 */

public class VideoDAO {
    private static String LOG_TAG = "VideoDAO";
    private MyDbOpenHelper myDbOpenHelper;
    public VideoDAO(Context context){
        myDbOpenHelper = new MyDbOpenHelper(context);
    }

    /**
     * 插入信息到视频表
     * @param person
     * @return
     */
    public boolean insert(Person person){
        boolean isInsert = false;
        SQLiteDatabase db = myDbOpenHelper.getWritableDatabase();
        String sql = "Insert into video(videoname,videopath,owner) values(?,?,?)";
        try {
            db.execSQL(sql,new String[]{person.getVideoName(),person.getVideoPath(),person.getUsername()});
            db.close();
            isInsert = true;
            Log.i(LOG_TAG,"insert Info:"+person.getVideoName()+","+person.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Insert Error"+e.getMessage());
        }

        return isInsert;
    }

    /**
     * 读取视频列表
     * @param person
     * @return
     */
    public boolean getVideoList(Person person){
        boolean isRead = false;
        ArrayList<String> videoNameList = new ArrayList<>();
        ArrayList<String> videoPathList = new ArrayList<>();
        SQLiteDatabase db = myDbOpenHelper.getReadableDatabase();
        String sql = "select * from video where owner=?";

        //查询获得游标
        Cursor cursor = db.rawQuery(sql,new String[]{person.getUsername()});
        try {
            while (cursor.moveToNext()){
                String videoName = cursor.getString(cursor.getColumnIndex("videoname"));
                String videoPath = cursor.getString(cursor.getColumnIndex("videopath"));
                videoNameList.add(videoName);
                videoPathList.add(videoPath);
            }
            isRead = true;
            Log.i(LOG_TAG,"getVideoList Success");
            person.setVideoNameList(videoNameList);
            person.setVideoPathList(videoPathList);
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"getVideoList Error"+e.getMessage());
        }

        return isRead;
    }

    /**
     * 删除视频
     * @param person
     * @param path
     * @return
     */
    public boolean deleteVideo(Person person,String path){
        boolean isDelete = false;
        SQLiteDatabase db = myDbOpenHelper.getWritableDatabase();
        try {
            db.delete("video","path=?",new String[]{path});
            isDelete = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Delete Error"+e.getMessage());
        }
        return isDelete;
    }

}
