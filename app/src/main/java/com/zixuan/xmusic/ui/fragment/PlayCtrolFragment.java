package com.zixuan.xmusic.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.ui.activity.SongPlayingActivity;
import com.zixuan.xmusic.ui.adapter.BottomSheetAdapter;
import com.zixuan.xmusic.utils.LogHelper;

import java.util.List;

/**
 * Created by zixuan on 2016/11/29.
 */
public class PlayCtrolFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "PlayCtrolFragment";
    private View rootView;
    private MediaControllerCompat mController;
    private TextView tvPlaybarTitle;
    private TextView tvPlaybarSinger;
    private ImageView btnPlay;
    private ImageView btnNext;
    private ImageView ivSongArt;
    private PlaybackStateCompat mState;
    private MediaMetadataCompat mMetadata;
    private List<MediaSessionCompat.QueueItem> mPlayingList;
    private BottomSheetDialog playlistDialog;
    private int mCurrentIndex;

    private MediaControllerCompat.Callback mControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            Log.d(TAG, "onPlaybackStateChanged: "+state.getState());
            mState = state;
            updateUiByPlayback(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            mMetadata = metadata;
            updateUiByMetaData(metadata);
        }

    };
    private RecyclerView rvNowPlaylist;
    private BottomSheetAdapter mPlayinglistAdapter;
    private ImageView btnPrev;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        MediaSessionCompat.Token mToken = bundle.getParcelable("token");
        try {
            if (mToken != null) {
                mController = new MediaControllerCompat(getContext(), mToken);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){

            rootView = inflater.inflate(R.layout.fragment_playcontrol, container, false);
            tvPlaybarTitle = (TextView) rootView.findViewById(R.id.tv_playbar_title);
            tvPlaybarSinger = (TextView) rootView.findViewById(R.id.tv_playbar_author);
            ivSongArt = (ImageView) rootView.findViewById(R.id.iv_song_art);
            btnPlay = (ImageView) rootView.findViewById(R.id.iv_playbar_play);
            btnNext = (ImageView) rootView.findViewById(R.id.iv_playbar_next);
            btnPrev = (ImageView) rootView.findViewById(R.id.iv_playbar_prev);


        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        ivSongArt.setOnClickListener(this);
        tvPlaybarSinger.setOnClickListener(this);
        tvPlaybarTitle.setOnClickListener(this);
        mController.registerCallback(mControllerCallback);

    }

    private void updateUiByMetaData(MediaMetadataCompat metadata){
        LogHelper.d(TAG, "updateUiByMetaData : " + metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI));
        tvPlaybarTitle.setText(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        tvPlaybarSinger.setText(metadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        Glide.with(this).load(metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)).placeholder(R.drawable.placeholder_music)
                .dontAnimate()
                .into(ivSongArt);
        mCurrentIndex = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER);
    }

    private void updateUiByPlayback(PlaybackStateCompat state){
        switch (state.getState()){
            case PlaybackStateCompat.STATE_CONNECTING:
                btnPlay.setImageResource(R.drawable.btn_pause);
                toast(getString(R.string.song_connecting_tips));
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                btnPlay.setImageResource(R.drawable.btn_pause);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                btnPlay.setImageResource(R.drawable.btn_play);
                break;
            case PlaybackStateCompat.STATE_NONE:
                btnPlay.setImageResource(R.drawable.btn_play);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null){
            parent.removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.unregisterCallback(mControllerCallback);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_playbar_next:
                mController.getTransportControls().skipToNext();
                break;
            case R.id.iv_playbar_play:
                if (mState.getState() == PlaybackStateCompat.STATE_PLAYING){
                    mController.getTransportControls().pause();
                }else if (mState.getState() == PlaybackStateCompat.STATE_PAUSED){
                    mController.getTransportControls().play();
                }
                break;
            case R.id.tv_playbar_title:
            case R.id.tv_playbar_author:
            case R.id.iv_song_art:
                Intent intent = new Intent(getContext(), SongPlayingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_playbar_prev:
                mController.getTransportControls().skipToPrevious();
                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMetadata = mController.getMetadata();
        mState = mController.getPlaybackState();
        updateUiByMetaData(mMetadata);
        updateUiByPlayback(mState);
    }


    private void toast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

   /* private void togglePlaylist() {
        if (playlistDialog == null){
            playlistDialog = new BottomSheetDialog(getContext());
            playlistDialog.setContentView(R.layout.bottom_sheet_playlist);
            playlistDialog.setCancelable(true);
            playlistDialog.setCanceledOnTouchOutside(true);

            rvNowPlaylist = (RecyclerView) playlistDialog.findViewById(R.id.rv_nowPlaylist);
            rvNowPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
            rvNowPlaylist.setHasFixedSize(true);
            mPlayingList = mController.getQueue();
            mPlayinglistAdapter = new BottomSheetAdapter(getContext(), R.layout.item_bottom_sheet_playlist,mPlayingList);
            rvNowPlaylist.setAdapter(mPlayinglistAdapter);

        }
        if (!playlistDialog.isShowing()){
            mPlayingList = mController.getQueue();
            mPlayinglistAdapter.setCurrentIndex(mCurrentIndex);
            mPlayinglistAdapter.notifyDataSetChanged();
            rvNowPlaylist.scrollToPosition(mCurrentIndex);
            playlistDialog.show();
        }else {
            playlistDialog.cancel();
        }
    }*/



}
