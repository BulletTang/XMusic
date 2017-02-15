package com.zixuan.xmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;


//电台实体
public class RadioBean implements Parcelable{

    /**
     "name": "经典老歌",
     "channelid": 3,
     "thumb": "http://a.hiphotos.baidu.com/ting/pic/item/060828381f30e9240a58cf564f086e061c95f7dd.jpg",
     "ch_name": "public_shiguang_jingdianlaoge",
     "value": 2,
     "cate_name": "shiguang",
     "cate_sname": "时光频道",
     "listen_num": 21220
     */

    private String name;
    private int channelid;
    private String thumb;
    private String ch_name;
    private int value;
    private String cate_name;
    private String cate_sname;
    private int listen_num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChannelid() {
        return channelid;
    }

    public void setChannelid(int channelid) {
        this.channelid = channelid;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCate_sname() {
        return cate_sname;
    }

    public void setCate_sname(String cate_sname) {
        this.cate_sname = cate_sname;
    }

    public int getListen_num() {
        return listen_num;
    }

    public void setListen_num(int listen_num) {
        this.listen_num = listen_num;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.channelid);
        dest.writeString(this.thumb);
        dest.writeString(this.ch_name);
        dest.writeInt(this.value);
        dest.writeString(this.cate_name);
        dest.writeString(this.cate_sname);
        dest.writeInt(this.listen_num);
    }

    public RadioBean() {
    }

    protected RadioBean(Parcel in) {
        this.name = in.readString();
        this.channelid = in.readInt();
        this.thumb = in.readString();
        this.ch_name = in.readString();
        this.value = in.readInt();
        this.cate_name = in.readString();
        this.cate_sname = in.readString();
        this.listen_num = in.readInt();
    }

    public static final Creator<RadioBean> CREATOR = new Creator<RadioBean>() {
        @Override
        public RadioBean createFromParcel(Parcel source) {
            return new RadioBean(source);
        }

        @Override
        public RadioBean[] newArray(int size) {
            return new RadioBean[size];
        }
    };
}
