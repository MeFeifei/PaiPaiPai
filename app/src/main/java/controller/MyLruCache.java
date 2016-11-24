package controller;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 重写LruCache缓存算法
 * Created by feifei on 2016/11/24.
 */

class MyLruCache extends LruCache<String,Bitmap> {
    private static MyLruCache instance;
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private MyLruCache(int maxSize) {
        super(maxSize);
    }

    /**
     * 获取可用最大内存
     * @param maxSize
     * @return
     */
    static MyLruCache getInstance(int maxSize){
        if (instance == null){
            instance = new MyLruCache(maxSize);
        }
        return instance;
    }

    /**
     * 获取图片大小
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }
}
