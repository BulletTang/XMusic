package com.zixuan.xmusic.ui.netfragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.bean.BillBoardBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.model.BillboardModel;
import com.zixuan.xmusic.ui.activity.PlaylistActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络音乐-排行榜tab标签页面
 */
public class BillboardFragment extends BaseLazyFragment implements BillboardModel.OnLoadBillboardListener{
    private final String TAG = "BillboardFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_billboard;
    private BillboardModel model;
    private boolean mbIsRefreshing;

    private List<BillBoardBean> mData;
    private CommonAdapter<BillBoardBean> billboardAdapter;


    @Override
    protected void lazyLoad() {
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_billboard;
    }

    protected void initView(){

        rv_billboard = (RecyclerView) rootView.findViewById(R.id.rv_billboard);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

    }
    protected void initListener(){

        rv_billboard.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        model = new BillboardModel();
        billboardAdapter = new CommonAdapter<BillBoardBean>(getContext(), R.layout.item_billboard,mData) {
            @Override
            protected void convert(ViewHolder holder, final BillBoardBean billBoardBean, int position) {
                holder.setText(R.id.tv_billname,billBoardBean.getName());
                holder.setText(R.id.tv_first_song,billBoardBean.getHotsong().get(0));
                holder.setText(R.id.tv_second_song,billBoardBean.getHotsong().get(1));
                holder.setText(R.id.tv_third_song,billBoardBean.getHotsong().get(2));
                Glide.with(getContext()).load(billBoardBean.getPic_s192()).placeholder(R.drawable.placeholder_music)
                        .into((ImageView) holder.getView(R.id.iv_bill));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlaylistActivity.class);
                        intent.putExtra(Constant.EXTRA_BILLBOARD,billBoardBean);
                        startActivity(intent);
                    }
                });
            }
        };
        rv_billboard.setAdapter(billboardAdapter);
        rv_billboard.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.loadBillboardData(BillboardFragment.this);
                mbIsRefreshing = true;
            }
        });


    }

    @Override
    protected void retryAction() {
        showLoading();
        model.loadBillboardData(this);
    }

    private void initData(){
        Log.d(TAG, "initData: ");
        showLoading();
        model.loadBillboardData(this);
    }


    @Override
    public void onSuccess(List<BillBoardBean> dataList) {
        Log.d(TAG, "onSuccess: " + dataList);
        showContent();
        if (mbIsRefreshing){
            mbIsRefreshing = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        mData.clear();
        mData.addAll(dataList);
        billboardAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFail(String errMsg) {
        Log.d(TAG, "onFail: ");
        showError();
    }



}
