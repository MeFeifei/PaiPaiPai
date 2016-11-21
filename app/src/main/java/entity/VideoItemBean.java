package entity;


import cn.bmob.v3.BmobObject;

/**
 * Created by feifei on 16/10/13.
 */

public class VideoItemBean extends BmobObject{
    public String ItemVideoName;
    public String ItemVideoPath;
    private String videoOwner;


    public VideoItemBean() {
    }

    public VideoItemBean(String itemTitle, String itemVideo) {
        ItemVideoName = itemTitle;
        ItemVideoPath = itemVideo;
    }

    public VideoItemBean(String itemVideoName, String itemVideoPath, String videoOwner) {
        ItemVideoName = itemVideoName;
        ItemVideoPath = itemVideoPath;
        this.videoOwner = videoOwner;
    }

    public String getItemVideoName() {
        return ItemVideoName;
    }

    public void setItemVideoName(String itemVideoName) {
        ItemVideoName = itemVideoName;
    }

    public String getItemVideoPath() {
        return ItemVideoPath;
    }

    public void setItemVideoPath(String itemVideoPath) {
        ItemVideoPath = itemVideoPath;
    }

    public String getVideoOwner() {
        return videoOwner;
    }

    public void setVideoOwner(String videoOwner) {
        this.videoOwner = videoOwner;
    }

    public void setAll(String itemVideoName,String itemVideoPath,String videoOwner){
        this.ItemVideoName = itemVideoName;
        this.ItemVideoPath = itemVideoPath;
        this.videoOwner = videoOwner;
    }
}
