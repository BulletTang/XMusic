package com.zixuan.xmusic.ui.adapter;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zixuan.xmusic.R;

import java.util.List;

/**
 * Created by zixuan on 2016/12/21.
 */

public class BottomSheetAdapter extends CommonAdapter<MediaSessionCompat.QueueItem> {
    private int mCurrentIndex;

    public BottomSheetAdapter(Context context, int layoutId, List<MediaSessionCompat.QueueItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MediaSessionCompat.QueueItem item, int position) {
        if (position == mCurrentIndex) {
            holder.setVisible(R.id.iv_songPlaying, true);
            holder.setTextColor(R.id.tv_songtitle, mContext.getResources().getColor(R.color.primary));
            holder.setTextColor(R.id.tv_artist, mContext.getResources().getColor(R.color.primary));
        } else {
            holder.setVisible(R.id.iv_songPlaying, false);
            holder.setTextColor(R.id.tv_songtitle, mContext.getResources().getColor(R.color.primary_text_dark));
            holder.setTextColor(R.id.tv_artist, mContext.getResources().getColor(R.color.secondary_text_dark));
        }
        holder.setText(R.id.tv_songtitle, item.getDescription().getTitle().toString());
        holder.setText(R.id.tv_artist, item.getDescription().getSubtitle().toString());
    }

    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
        notifyDataSetChanged();
    }
}
