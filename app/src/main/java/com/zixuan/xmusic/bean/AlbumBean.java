package com.zixuan.xmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;


//专辑实体
public class AlbumBean implements Parcelable{

    private String album_id;
    private String author;
    private String title;
    private String publishcompany;
    private String country;
    private String language;
    private String songs_total;  //歌曲总数
    private String info;  //专辑描述
    private String styles;
    private String publishtime;
    private String pic_small;
    private String pic_big;
    private String pic_radio;
    private String pic_s500;
    private String listen_num;

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishcompany() {
        return publishcompany;
    }

    public void setPublishcompany(String publishcompany) {
        this.publishcompany = publishcompany;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSongs_total() {
        return songs_total;
    }

    public void setSongs_total(String songs_total) {
        this.songs_total = songs_total;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getPic_radio() {
        return pic_radio;
    }

    public void setPic_radio(String pic_radio) {
        this.pic_radio = pic_radio;
    }

    public String getPic_s500() {
        return pic_s500;
    }

    public void setPic_s500(String pic_s500) {
        this.pic_s500 = pic_s500;
    }

    public String getListen_num() {
        return listen_num;
    }

    public void setListen_num(String listen_num) {
        this.listen_num = listen_num;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.album_id);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.publishcompany);
        dest.writeString(this.country);
        dest.writeString(this.language);
        dest.writeString(this.songs_total);
        dest.writeString(this.info);
        dest.writeString(this.styles);
        dest.writeString(this.publishtime);
        dest.writeString(this.pic_small);
        dest.writeString(this.pic_big);
        dest.writeString(this.pic_radio);
        dest.writeString(this.pic_s500);
        dest.writeString(this.listen_num);
    }

    public AlbumBean() {
    }

    protected AlbumBean(Parcel in) {
        this.album_id = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.publishcompany = in.readString();
        this.country = in.readString();
        this.language = in.readString();
        this.songs_total = in.readString();
        this.info = in.readString();
        this.styles = in.readString();
        this.publishtime = in.readString();
        this.pic_small = in.readString();
        this.pic_big = in.readString();
        this.pic_radio = in.readString();
        this.pic_s500 = in.readString();
        this.listen_num = in.readString();
    }

    public static final Creator<AlbumBean> CREATOR = new Creator<AlbumBean>() {
        @Override
        public AlbumBean createFromParcel(Parcel source) {
            return new AlbumBean(source);
        }

        @Override
        public AlbumBean[] newArray(int size) {
            return new AlbumBean[size];
        }
    };
}
