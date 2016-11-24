package entity;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.feifei.paipaipai.R;

import java.io.File;
import java.util.List;

import controller.ImageLoader;

/**
 * 自定义适配器
 * Created by feifei on 16/10/13.
 */

public class VideoListAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private static String LOG_TAG="VideoListAdapter";

    private List<VideoItemBean>mList;
    private int mStart,mEnd;
    private boolean first;//是否是第一次加载
    private ImageLoader imageLoader;
    private Context mContext;

    public static String imagePath[];//视频地址

    public VideoListAdapter(Context context, List<VideoItemBean>list,ListView listView){
        this.mList = list;
        this.mContext = context;
        first = true;

        imageLoader = new ImageLoader(listView);
        imagePath = new String[list.size()];
        for (int i=0;i<list.size();i++){
            imagePath[i] = list.get(i).getItemVideoPath();
        }
        listView.setOnScrollListener(this);

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder =new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.list_title);
            viewHolder.video= (ImageView) convertView.findViewById(R.id.list_imageView_show);
            viewHolder.comment= (LinearLayout) convertView.findViewById(R.id.div_comm);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VideoItemBean videoItemBean =mList.get(position);

        setView(convertView, viewHolder.video);
        viewHolder.title.setText(videoItemBean.getItemVideoName());
        //异步加载预览图片
        String urlTag = videoItemBean.getItemVideoPath();
        viewHolder.video.setTag(urlTag);

        DeleteListener deleteListener = new DeleteListener(position);
        viewHolder.video.setOnClickListener(deleteListener);

        return convertView;
    }

    /**
     * 滑动监听器，滑动时停止所有任务保证流畅性，滑动结束时加载数据
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滑动结束时加载预览图片
        if (scrollState ==SCROLL_STATE_IDLE){
            imageLoader.loadImage(mStart,mEnd);
        }else {//滑动时停止任务
            imageLoader.cancelAllTask();
        }

    }

    /**
     * 提供滑动时上下界限
     * 第一次加载时实现预加载
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = mStart + visibleItemCount;
        //第一次调用时预加载
        if (first && visibleItemCount >0){
            imageLoader.loadImage(mStart,mEnd);
        }

    }

    private class ViewHolder{
        TextView title;
        ImageView video;
        LinearLayout comment;
    }
    private class DeleteListener implements View.OnClickListener{
        private int m_position;

        DeleteListener(int pos) {
            m_position = pos;
        }
        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG,"line:"+m_position+":"+mList.get(m_position).ItemVideoName);
            mList.remove(m_position);
            notifyDataSetChanged();
        }
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


}

