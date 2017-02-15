package com.zixuan.xmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


//排行榜实体
public class BillBoardBean implements Parcelable {

    private String name;  //排行榜名称
    private int type;    //排行榜参数
    private String pic_s192;  //排行榜图片
    private List<String> hotsong;  //排行榜热门歌曲
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic_s192() {
        return pic_s192;
    }

    public void setPic_s192(String pic_s192) {
        this.pic_s192 = pic_s192;
    }

    public List<String> getHotsong() {
        return hotsong;
    }

    public void setHotsong(List<String> hotsong) {
        this.hotsong = hotsong;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.pic_s192);
        dest.writeStringList(this.hotsong);
        dest.writeString(this.comment);
    }

    public BillBoardBean() {
    }

    protected BillBoardBean(Parcel in) {
        this.name = in.readString();
        this.type = in.readInt();
        this.pic_s192 = in.readString();
        this.hotsong = in.createStringArrayList();
        this.comment = in.readString();
    }

    public static final Creator<BillBoardBean> CREATOR = new Creator<BillBoardBean>() {
        @Override
        public BillBoardBean createFromParcel(Parcel source) {
            return new BillBoardBean(source);
        }

        @Override
        public BillBoardBean[] newArray(int size) {
            return new BillBoardBean[size];
        }
    };
}
