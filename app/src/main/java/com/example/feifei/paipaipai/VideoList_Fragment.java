package com.example.feifei.paipaipai;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import entity.VideoItemBean;
import entity.VideoListAdapter;


public class VideoList_Fragment extends Fragment {
    private static String LOG_TAG= "VideoList";
    private List<VideoItemBean> datalist;
    private Context context;
    private Index mActivity;

    public VideoList_Fragment() {
        // Required empty public constructor
    }

    public VideoList_Fragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        // 自定义BaseAdapter实现列表

        ListView listView = (ListView) view.findViewById(R.id.list_video);
        mActivity = (Index) getActivity();
        datalist = new ArrayList<>();
        listView.setAdapter(new VideoListAdapter(context,getData(),listView,mActivity));
        return view;
    }

    private List<VideoItemBean> getData() {
        ArrayList<String> videoPathList = mActivity.getVideoPathList();
        ArrayList<String> videoNameList = mActivity.getVideoNameList();
        if (videoPathList != null && videoNameList != null){
            for (int i = 0; i < videoPathList.size(); i++) {
                datalist.add(new VideoItemBean(videoNameList.get(i),videoPathList.get(i)));
            }
        }

        return datalist;
    }


}
