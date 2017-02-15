package com.zixuan.xmusic.ui.netfragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.bean.GedanBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.model.GedanModel;
import com.zixuan.xmusic.ui.activity.PlaylistActivity;
import com.zixuan.xmusic.ui.widget.SpaceItemDecoration;
import com.zixuan.xmusic.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


public class NetPlaylistFragment extends BaseLazyFragment implements GedanModel.OnLoadGedanListener {
    private final String TAG = "NetPlaylistFragment";
    private int nextPage = 1;
    private RecyclerView rv_playlist;
    private GedanModel model;

    private boolean canLoadMore;
    private List<GedanBean> mData;
    private LoadMoreWrapper gedanLoadMoreWrapper;


    @Override
    protected void lazyLoad() {
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_net_playlist;
    }

    protected void initView(){
        rv_playlist = (RecyclerView) rootView.findViewById(R.id.recycler_view);

    }

    protected void initListener(){
        rv_playlist.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mData = new ArrayList<>();
        model = new GedanModel();
        CommonAdapter<GedanBean> gedanAdapter= new CommonAdapter<GedanBean>(getContext(), R.layout.item_gedan,mData) {
            @Override
            protected void convert(ViewHolder holder, GedanBean gedanBean, int position) {
                holder.setText(R.id.playlist_listen_count,gedanBean.getListenum());
                holder.setText(R.id.playlist_name,gedanBean.getTitle());
                Glide.with(getContext()).load(gedanBean.getPic_w300()).placeholder(R.drawable.placeholder_music)
                        .into((ImageView) holder.getView(R.id.playlist_art));
            }
        };
        gedanAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                GedanBean gedanBean = mData.get(position);
                Intent intent = new Intent(getContext(), PlaylistActivity.class);
                intent.putExtra(Constant.EXTRA_GEDAN,gedanBean);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        gedanLoadMoreWrapper = new LoadMoreWrapper(gedanAdapter);
        gedanLoadMoreWrapper.setLoadMoreView(R.layout.loadmore);
        gedanLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                     if (canLoadMore){
                         model.getGedanData(nextPage,10,NetPlaylistFragment.this);
                         canLoadMore = false;
                     }
            }
        });
        rv_playlist.setAdapter(gedanLoadMoreWrapper);
        int px_num = DensityUtils.dip2px(getContext(), 10);
        rv_playlist.addItemDecoration(new SpaceItemDecoration(px_num,
                px_num,px_num,px_num ));
        rv_playlist.setHasFixedSize(true);

    }

    @Override
    protected void retryAction() {
        showLoading();
        model.getGedanData(nextPage,10,this);
    }

    private void initData(){
        Log.d(TAG, "initData: ");
        showLoading();
        model.getGedanData(nextPage,10,this);
    }

    @Override
    public void onSuccess(List<GedanBean> data, boolean hasMore) {
        Log.d(TAG, "onSuccess: ");
        showContent();
        this.canLoadMore = hasMore;
        nextPage++;
        mData.addAll(data);
        gedanLoadMoreWrapper.notifyDataSetChanged();

    }

    @Override
    public void onFail(String msg) {
        Log.d(TAG, "onFail: "+msg);
        showError();
    }




}
