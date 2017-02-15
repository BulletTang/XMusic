package com.zixuan.xmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

//歌曲实体
public class SongBean implements Parcelable{

    private String pic_huge;
    private String pic_premium;
    private String author;
    private String song_id;
    private String artist_id;
    private String lrclink;
    private String pic_big;
    private String album_id;
    private String album_title;
    private String pic_radio;
    private String title;
    private String pic_small;
    private String album_no;
    private long file_size;
    private int file_duration;
    private String file_link;

    public String getPic_huge() {
        return pic_huge;
    }

    public void setPic_huge(String pic_huge) {
        this.pic_huge = pic_huge;
    }

    public String getPic_premium() {
        return pic_premium;
    }

    public void setPic_premium(String pic_premium) {
        this.pic_premium = pic_premium;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public int getFile_duration() {
        return file_duration;
    }

    public void setFile_duration(int file_duration) {
        this.file_duration = file_duration;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getPic_radio() {
        return pic_radio;
    }

    public void setPic_radio(String pic_radio) {
        this.pic_radio = pic_radio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getAlbum_no() {
        return album_no;
    }

    public void setAlbum_no(String album_no) {
        this.album_no = album_no;
    }

    @Override
    public String toString() {
        return "SongBean{" +
                "title='" + title + '\'' +
                ", song_id='" + song_id + '\'' +
                ", file_link='" + file_link + '\'' +
                ", file_size=" + file_size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pic_huge);
        dest.writeString(this.pic_premium);
        dest.writeString(this.author);
        dest.writeString(this.song_id);
        dest.writeString(this.artist_id);
        dest.writeString(this.lrclink);
        dest.writeString(this.pic_big);
        dest.writeString(this.album_id);
        dest.writeString(this.album_title);
        dest.writeString(this.pic_radio);
        dest.writeString(this.title);
        dest.writeString(this.pic_small);
        dest.writeString(this.album_no);
        dest.writeLong(this.file_size);
        dest.writeInt(this.file_duration);
        dest.writeString(this.file_link);
    }

    public SongBean() {
    }

    protected SongBean(Parcel in) {
        this.pic_huge = in.readString();
        this.pic_premium = in.readString();
        this.author = in.readString();
        this.song_id = in.readString();
        this.artist_id = in.readString();
        this.lrclink = in.readString();
        this.pic_big = in.readString();
        this.album_id = in.readString();
        this.album_title = in.readString();
        this.pic_radio = in.readString();
        this.title = in.readString();
        this.pic_small = in.readString();
        this.album_no = in.readString();
        this.file_size = in.readLong();
        this.file_duration = in.readInt();
        this.file_link = in.readString();
    }

    public static final Creator<SongBean> CREATOR = new Creator<SongBean>() {
        @Override
        public SongBean createFromParcel(Parcel source) {
            return new SongBean(source);
        }

        @Override
        public SongBean[] newArray(int size) {
            return new SongBean[size];
        }
    };
}
