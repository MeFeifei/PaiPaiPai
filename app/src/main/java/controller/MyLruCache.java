package controller;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Lru缓存功能
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
    static MyLruCache getInstance(int maxSize){
        if (instance == null){
            instance = new MyLruCache(maxSize);
        }
        return instance;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }
}
