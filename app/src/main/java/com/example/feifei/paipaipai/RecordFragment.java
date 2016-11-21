package com.example.feifei.paipaipai;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements View.OnClickListener{
    private static String LOG_TAG = "Record Fragment";
    private Index mActivity;

    private static final int PLAY_PROGRESS = 110;

    private VideoView videoViewShow;
    private ImageView imageViewShow;
    private Button buttonDone;
    private RelativeLayout rlBottomRoot;
    private Button buttonPlay;
    private TextView textViewCountDown;
    private ProgressBar progressVideo;
    /**
     * 视频路径
     */
    private String path;

    private int currentTime;
    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_PROGRESS:
//                    LogUtil.e(LOG_TAG, "getDuration：" + videoViewShow.getDuration() + "...getCurrentPosition：" + videoViewShow.getCurrentPosition());
                    /*视频时间*/
                    int time = (videoViewShow.getDuration() + 1000) / 1000;
                    currentTime = (videoViewShow.getCurrentPosition() + 1500) / 1000;
                    progressVideo.setMax(videoViewShow.getDuration());
//                    LogUtil.e(LOG_TAG, time + "..time：" + currentTime);
                    progressVideo.setProgress(videoViewShow.getCurrentPosition());
                    if (currentTime < 10) {
                        textViewCountDown.setText("00:0" + currentTime);
                    } else {
                        textViewCountDown.setText("00:" + currentTime);
                    }
                    //currentTime++;
                    //达到指定时间，停止播放
                    if (!videoViewShow.isPlaying() && time > 0) {
                        progressVideo.setProgress(videoViewShow.getDuration());
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                    break;
            }
        }
    };


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        assignViews(view);
        initViews(view);
        initData();
        return view;
    }

    /**
     * 初始化对象
     * @param v
     */
    private void assignViews(View v) {
        videoViewShow = (VideoView) v.findViewById(R.id.videoView_show);
        imageViewShow = (ImageView) v.findViewById(R.id.imageView_show);
        buttonDone = (Button) v.findViewById(R.id.button_done);
        rlBottomRoot = (RelativeLayout) v.findViewById(R.id.rl_bottom_root);
        buttonPlay = (Button) v.findViewById(R.id.button_play);
        textViewCountDown = (TextView) v.findViewById(R.id.textView_count_down);
        progressVideo = (ProgressBar) v.findViewById(R.id.progressBar_loading);

        mActivity = (Index) getActivity();
    }

    /**
     * 自定义界面值
     * @param v
     */
    private void initViews(View v){
        buttonDone.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);

        textViewCountDown.setText("00:00");

        DisplayMetrics dm = v.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewShow.getLayoutParams();
        layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        //LogUtil.e(LOG_TAG, "mSurfaceViewWidth:" + width + "...mSurfaceViewHeight:" + layoutParams.height);
        videoViewShow.setLayoutParams(layoutParams);
        imageViewShow.setLayoutParams(layoutParams);

        FrameLayout.LayoutParams rlBottomRootLayoutParams = (FrameLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = width / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);
    }

    /**
     * 初始化视频地址
     */
    private void initData(){
        path = mActivity.getVideoPath();
        try {
            if (TextUtils.isEmpty(path)){
                Log.i(LOG_TAG,"获取视频地址为空");
            }else {
                /*要上传的视频文件*/
                File file = new File(path);
                //获取第一帧图片，预览使用
                if (file.length() != 0) {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(path);
                    Bitmap bitmap = media.getFrameAtTime();
                    imageViewShow.setImageBitmap(bitmap);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.i("Error","视频预览初始化数据出错");
        }

    }
    /**
     * 播放视频
     */
    private void playVideo() {
        textViewCountDown.setText("00:00");
        progressVideo.setProgress(0);

//        //视频控制面板，不需要可以不设置
//        MediaController controller = new MediaController(view.getContext());
//        controller.setVisibility(View.GONE);
//        videoViewShow.setMediaController(controller);
        videoViewShow.setVideoPath(path);
        videoViewShow.start();
        videoViewShow.requestFocus();
        videoViewShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!videoViewShow.isPlaying()) {
                    buttonPlay.setVisibility(View.VISIBLE);
                }
            }
        });

        currentTime = 0;//时间计数器重新赋值
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(PLAY_PROGRESS);
            }
        }, 0, 100);
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_done:
                mActivity.takePicture();
                break;
            case R.id.button_play:
                buttonPlay.setVisibility(View.GONE);
                imageViewShow.setVisibility(View.GONE);
                playVideo();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoViewShow.stopPlayback();
    }
}
