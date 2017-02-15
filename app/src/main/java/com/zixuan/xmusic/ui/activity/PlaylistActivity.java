package com.zixuan.xmusic.ui.activity;

import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.bean.AlbumBean;
import com.zixuan.xmusic.bean.BillBoardBean;
import com.zixuan.xmusic.bean.GedanBean;
import com.zixuan.xmusic.bean.RadioBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.model.PlaylistModel;
import com.zixuan.xmusic.ui.fragment.PlayCtrolFragment;
import com.zixuan.xmusic.ui.listener.AppBarStateChangeListener;

import java.util.ArrayList;
import java.util.List;

import me.xdj.view.MultiStateView;

public class PlaylistActivity extends BaseMusicActivity implements  View.OnClickListener, PlaylistModel.OnPlaylistInfoListener {
    public static final String TAG = "PlaylistActivity";

    public static final int TYPE_GEDAN = 1;
    public static final int TYPE_ALBUM =2 ;
    public static final int TYPE_BILLBOARD = 3;
    public static final int TYPE_RADIO = 4;

    private RecyclerView rv_playlist;
    private PlaylistModel model;
    private int mCurrentDataType;

    private GedanBean currentGedan;
    private AlbumBean currentAlbumBean;
    private BillBoardBean currentBillboardBean;
    private RadioBean currentRadioBean;

    private ImageView toolbar_header;
    private TextView tv_playlist_title;
    private TextView tv_playlist_desc;
    private List<MediaSessionCompat.QueueItem> mData;

    private PlayCtrolFragment mCtrolFragment;

    private MultiStateView playlistContainer;
    private View headerView;
    private HeaderAndFooterWrapper headerWrapper;
    private AppBarLayout appbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        currentGedan = getIntent().getParcelableExtra(Constant.EXTRA_GEDAN);
        currentAlbumBean = getIntent().getParcelableExtra(Constant.EXTRA_ALBUM);
        currentBillboardBean = getIntent().getParcelableExtra(Constant.EXTRA_BILLBOARD);
        currentRadioBean = getIntent().getParcelableExtra(Constant.EXTRA_RADIO);

        if (currentGedan != null){
            mCurrentDataType = TYPE_GEDAN;
        }else if (currentAlbumBean != null){
            mCurrentDataType = TYPE_ALBUM;
        }else if (currentBillboardBean != null){
            mCurrentDataType = TYPE_BILLBOARD;
        }else {
            mCurrentDataType = TYPE_RADIO;
        }

        initToolbar();
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpListHeader(mCurrentDataType);
        initListener();
        initData(mCurrentDataType);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mCurrentDataType == TYPE_GEDAN) {
            toolbar.setTitle(R.string.toolbar_title_gedan);
        } else if (mCurrentDataType == TYPE_ALBUM) {
            toolbar.setTitle(R.string.toolbar_title_album);
        } else if (mCurrentDataType == TYPE_RADIO) {
            toolbar.setTitle(R.string.toolbar_title_radio);
        } else if (mCurrentDataType == TYPE_BILLBOARD) {
            toolbar.setTitle(R.string.toolbar_title_billboard);
        } else {
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);

    }

    private void initView(){
        toolbar_header = (ImageView) findViewById(R.id.toolbar_header);
        playlistContainer = (MultiStateView) findViewById(R.id.playlist_container);
        rv_playlist = (RecyclerView) findViewById(R.id.rv_playlist);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_playlist, (ViewGroup) rv_playlist.getParent(), false);
        tv_playlist_desc = (TextView) headerView.findViewById(R.id.tv_playlist_desc);
        tv_playlist_title = (TextView) headerView.findViewById(R.id.tv_playlist_title);


    }

    private void setUpListHeader(int type){
        String pic_url;
        String title;
        String desc;
        if (type == TYPE_GEDAN){
            pic_url = currentGedan.getPic_300();
            title = currentGedan.getTitle();
            desc = currentGedan.getDesc();
        }else if (type == TYPE_ALBUM){
            pic_url = currentAlbumBean.getPic_radio();
            title = currentAlbumBean.getTitle();
            desc = currentAlbumBean.getAuthor();
        }else if (type == TYPE_BILLBOARD){
            pic_url =currentBillboardBean.getPic_s192();
            title = currentBillboardBean.getName();
            desc = currentBillboardBean.getComment();
        }else {
            pic_url = currentRadioBean.getThumb();
            title = currentRadioBean.getName();
            desc = currentRadioBean.getCate_sname();
        }

        Glide.with(this).load(pic_url)
                .placeholder(R.drawable.placeholder_music)
                .into(toolbar_header);

        tv_playlist_title.setText(title);
        tv_playlist_desc.setText(desc);
    }
    private void initListener(){

        mData = new ArrayList<>();
        rv_playlist.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter playlistAdapter = new CommonAdapter<MediaSessionCompat.QueueItem>(this, R.layout.item_playlist_detail, mData) {
            @Override
            protected void convert(ViewHolder holder, final MediaSessionCompat.QueueItem item, int position) {
                holder.setText(R.id.tv_songtitle, item.getDescription().getTitle().toString());
                holder.setText(R.id.tv_artist, item.getDescription().getSubtitle().toString());
                holder.setText(R.id.tv_postion, String.valueOf((position)));
            }
        };

        playlistAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mediaPlayerHelper.setPlaylist(mData);
                mediaPlayerHelper.playQueueItem(position - 1);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        headerWrapper = new HeaderAndFooterWrapper(playlistAdapter);
        headerWrapper.addHeaderView(headerView);
        rv_playlist.setAdapter(headerWrapper);
        rv_playlist.setHasFixedSize(true);
        model = new PlaylistModel();
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {

                    //展开状态
                    toolbar.setBackgroundColor(Color.parseColor("#33000000")); //给toolbar加上暗纱


                } else if (state == State.COLLAPSED) {

                    //折叠状态
                    toolbar.setBackgroundColor(Color.TRANSPARENT);

                } else {

                    //中间状态

                }
            }
        });

    }

    private void initData(int type){
        playlistContainer.setViewState(MultiStateView.STATE_LOADING);
        if (type == TYPE_GEDAN){
            //获取歌单歌曲
            model.getGedanInfo(currentGedan.getListid(),this);
        }else if (type == TYPE_ALBUM){
            //获取专辑里的歌曲
            model.getAlbumInfo(currentAlbumBean.getAlbum_id(),this);
        }else if (type == TYPE_BILLBOARD){
            //获取排行榜歌曲
            model.getBillBoardInfo(currentBillboardBean.getType(),0,15,this);
        }else {
            //获取电台歌曲
            model.getRadioInfo(currentRadioBean.getCh_name(),15,this);
        }

    }

    @Override
    public void OnSongDetailInfo(List<MediaSessionCompat.QueueItem> data) {
        playlistContainer.setViewState(MultiStateView.STATE_CONTENT);
        mData.addAll(data);
        headerWrapper.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {

    }

    private void showControl(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCtrolFragment == null){
            mCtrolFragment = new PlayCtrolFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("token",mToken);
            mCtrolFragment.setArguments(bundle);
            transaction.add(R.id.music_container, mCtrolFragment);
            transaction.commitAllowingStateLoss();

        }else {
            transaction.show(mCtrolFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void hideControl() {
        if (mCtrolFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(mCtrolFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onMetaDataChanged(MediaMetadataCompat metadata) {
        super.onMetaDataChanged(metadata);
        showControl();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        if (mState.getState() == PlaybackStateCompat.STATE_PAUSED || mState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            showControl();
        }
    }
}
