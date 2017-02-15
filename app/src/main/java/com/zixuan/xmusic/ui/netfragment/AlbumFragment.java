package com.zixuan.xmusic.ui.netfragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.bean.AlbumBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.model.AlbumModel;
import com.zixuan.xmusic.ui.activity.PlaylistActivity;
import com.zixuan.xmusic.ui.widget.SpaceItemDecoration;
import com.zixuan.xmusic.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixuan on 2016/11/30.
 */

public class AlbumFragment extends BaseLazyFragment implements AlbumModel.OnAlbumListener {

    private static final String TAG = "AlbumFragment";


    private RecyclerView rv_album;
    private AlbumModel model;
    private int offset = 0;
    private ArrayList<AlbumBean> mData;
    private boolean canLoadMore;
    private LoadMoreWrapper loadMoreWrapper;


    @Override
    protected void lazyLoad() {
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album;
    }

    protected void initView(){
        rv_album = (RecyclerView) rootView.findViewById(R.id.rv_album);
    }
    protected void initListener() {
        mData = new ArrayList<>();
        model = new AlbumModel();

        CommonAdapter<AlbumBean> adapter = new CommonAdapter<AlbumBean>(getContext(), R.layout.item_album,
                mData) {
            @Override
            protected void convert(ViewHolder holder, final AlbumBean albumBean, int position) {
                holder.setText(R.id.tv_recommend_title, albumBean.getTitle());
                holder.setText(R.id.tv_recommend_ablum_author, albumBean.getAuthor());
                Glide.with(getContext()).load(albumBean.getPic_small()).placeholder(R.drawable.placeholder_music)
                        .into((ImageView) holder.getView(R.id.iv_recommend_album));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlaylistActivity.class);
                        intent.putExtra(Constant.EXTRA_ALBUM,albumBean);
                        startActivity(intent);
                    }
                });
            }
        };

        rv_album.setLayoutManager(new GridLayoutManager(getContext(), 3));
        int px_num = DensityUtils.dip2px(getContext(), 10);
        rv_album.addItemDecoration(new SpaceItemDecoration(px_num,
                px_num,px_num,px_num ));
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        loadMoreWrapper.setLoadMoreView(R.layout.loadmore);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (canLoadMore) {
                    model.getAlbumData(offset, 12, AlbumFragment.this);
                    canLoadMore = false;
                }
            }
        });
        rv_album.setAdapter(loadMoreWrapper);
        rv_album.setHasFixedSize(true);
    }

    @Override
    protected void retryAction() {
        showLoading();
        model.getAlbumData(offset,12,this);
    }

    private void initData(){
        showLoading();
        model = new AlbumModel();
        model.getAlbumData(offset,12,this);
    }


    @Override
    public void OnAlbumData(List<AlbumBean> data) {
        showContent();
        mData.addAll(data);
        offset += data.size();
        loadMoreWrapper.notifyDataSetChanged();
        canLoadMore = true;

    }

    @Override
    public void OnNoMoreData() {
        showContent();
        canLoadMore = false;
        loadMoreWrapper.setLoadMoreView(R.layout.nomore);
        rv_album.setAdapter(loadMoreWrapper);
    }
}
