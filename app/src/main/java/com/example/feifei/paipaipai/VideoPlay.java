package com.example.feifei.paipaipai;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlay extends AppCompatActivity implements View.OnClickListener{
    private static final String LOG_TAG = "VideoPreviewActivity";
    private static final int RES_CODE = 111;

    /**
     * 播放进度
     */
    private static final int PLAY_PROGRESS = 110;

    private VideoView videoViewShow;
    private RelativeLayout rlBottomRoot;
    private Button buttonPlay;
    private TextView textViewCountDown;
    private ProgressBar progressVideo;
    /**
     * 视频路径
     */
    private String path;
    /**
     * 视频时间
     */
    private int time;
    private int currentTime;
    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_PROGRESS:
                    time = (videoViewShow.getDuration() + 1000) / 1000;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        path = getIntent().getStringExtra("videoPlay");
        assignViews();
        initView();

    }
    private void assignViews() {
        videoViewShow = (VideoView) findViewById(R.id.play_videoView_show);
        rlBottomRoot = (RelativeLayout) findViewById(R.id.play_rl_bottom_root);
        buttonPlay = (Button) findViewById(R.id.play_button_play);
        textViewCountDown = (TextView) findViewById(R.id.play_textView_count_down);
        progressVideo = (ProgressBar) findViewById(R.id.play_progressBar_loading);
    }
    public void initView() {
        (findViewById(R.id.play_return)).setOnClickListener(this);
        buttonPlay.setOnClickListener(this);

        textViewCountDown.setText("00:00");

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewShow.getLayoutParams();
        layoutParams.height = width * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        videoViewShow.setLayoutParams(layoutParams);

        FrameLayout.LayoutParams rlBottomRootLayoutParams = (FrameLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = width / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        textViewCountDown.setText("00:00");
        progressVideo.setProgress(0);

        //视频控制面板，不需要可以不设置
        MediaController controller = new MediaController(this);
        controller.setVisibility(View.GONE);
        videoViewShow.setMediaController(controller);
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
        switch (v.getId()) {
            case R.id.play_button_play:
                buttonPlay.setVisibility(View.GONE);
                playVideo();
                break;
            case R.id.play_return:
                finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoViewShow.stopPlayback();
    }

}
