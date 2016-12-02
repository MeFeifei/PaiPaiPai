package controller;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

import entity.VideoListAdapter;

/**
 * Created by feifei on 2016/11/24.
 * 通过AsyncTask加载视频预览图片
 */

public class ImageLoader {
    private static String LOG_TAG = "ImageLoader";
    //用于缓存视频的预览图
    private static LruCache<String,Bitmap> imageCache;
    private ListView mListView;
    private Set<ImageAsyncTask> mTask;

    public ImageLoader(ListView listView){
        //获取运行时最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCache = maxMemory / 4 ;
        this.mListView = listView ;
        mTask = new HashSet<>();
        imageCache = MyLruCache.getInstance(mCache);
    }

    /**
     * 加载预览图片
     * @param start
     * @param end
     */
    public void loadImage(int start,int end){
        for (int i = start;i < end; i++){
            String url = VideoListAdapter.imagePath[i];
            Bitmap bitmap = imageCache.get(url);
            if (bitmap != null){
                //设置ImageView的图片
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }else {//若缓存中没有该数据，则重新获取
                ImageAsyncTask task = new ImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }
        }
    }

    /**
     * 取消所有异步任务
     */
    public void cancelAllTask(){
        if (mTask != null){
            for (ImageAsyncTask task : mTask ){
                task.cancel(false);
            }
        }
    }
    /**
     * 获取视频预览图片
     * @param path
     * @return
     */
    private Bitmap getBitmap(String path){
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(path)){
                Log.i(LOG_TAG,"获取视频地址为空");
            }else {
                File file = new File(path);
                //获取第一帧图片，预览使用
                if (file.length() != 0) {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(path);
                    bitmap = media.getFrameAtTime();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.i("Error","视频预览初始化数据出错");
        }
        return bitmap;
    }

    //异步加载预览图片
    private class ImageAsyncTask extends AsyncTask<String,Void,Bitmap>{
        private String urlTag;
        private Bitmap bitmap;

        ImageAsyncTask(String urlTag){
            this.urlTag = urlTag;
        }

        /**
         * 获取预览图片，若不为空则写入缓存中
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            String path = params[0];
            bitmap = getBitmap(path);
            if (bitmap != null){
                imageCache.put(path,bitmap);
            }
            return bitmap;
        }

        /**
         * 设置imageView预览图片
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) mListView.findViewWithTag(urlTag);
            if (imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            super.onPostExecute(bitmap);
        }
    }
}
