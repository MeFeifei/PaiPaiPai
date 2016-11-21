package entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.feifei.paipaipai.R;

import java.io.File;
import java.util.List;

/**
 * Created by feifei on 16/10/13.
 */

public class VideoListAdapter extends BaseAdapter {
    private static String LOG_TAG="VideoListAdapter";

    private List<VideoItemBean>mlist;
    private LayoutInflater mInflater;

    public VideoListAdapter(Context context, List<VideoItemBean>list){
        mlist=list;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.list_item,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.list_title);
            viewHolder.video= (ImageView) convertView.findViewById(R.id.list_imageView_show);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        VideoItemBean videoItemBean =mlist.get(position);

        setView(convertView,viewHolder.video);
        viewHolder.title.setText(videoItemBean.ItemVideoName);
        viewHolder.video.setImageBitmap(getBitmap(videoItemBean.ItemVideoPath));

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        ImageView video;
    }

    /**
     * 根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
     * @param v
     */
    private void setView(View v,ImageView imageView){
        DisplayMetrics dm = v.getResources().getDisplayMetrics();
        int width = dm.widthPixels;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = width * 5 / 4;
        imageView.setLayoutParams(layoutParams);

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
}

