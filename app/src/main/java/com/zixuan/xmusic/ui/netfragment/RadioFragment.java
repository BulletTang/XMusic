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
import com.zixuan.xmusic.bean.RadioBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.model.RadioModel;
import com.zixuan.xmusic.ui.activity.PlaylistActivity;
import com.zixuan.xmusic.ui.widget.SpaceItemDecoration;
import com.zixuan.xmusic.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


public class RadioFragment extends BaseLazyFragment implements RadioModel.OnRadioListener {

    private final String TAG = "RadioFragment";

    private RecyclerView rv_radio;
    private RadioModel model;
    private int nextpage = 1;
    private boolean canLoadMore ;
    private List<RadioBean> mData;
    private LoadMoreWrapper loadMoreWrapper;


    @Override
    protected void lazyLoad() {
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_radio;
    }

    protected void initView(){
        rv_radio = (RecyclerView) rootView.findViewById(R.id.rv_radio);
    }
    protected void initListener(){
        setUpRecyclerview();
        model = new RadioModel();
    }

    @Override
    protected void retryAction() {
        showLoading();
        model.getRadioData(nextpage,12,this);
    }

    private void initData(){
        showLoading();
        model.getRadioData(nextpage,12,this);
    }

    private void setUpRecyclerview(){
        int px_num = DensityUtils.dip2px(getContext(), 10);
        SpaceItemDecoration decoration = new SpaceItemDecoration(px_num,
                px_num, px_num, px_num);
        mData = new ArrayList<>();
        CommonAdapter<RadioBean> adapter = new CommonAdapter<RadioBean>(getContext(),R.layout.item_radio,mData) {
            @Override
            protected void convert(ViewHolder holder, final RadioBean radioBean, int position) {
                holder.setText(R.id.tv_rd_radio,radioBean.getName());
                Glide.with(mContext).load(radioBean.getThumb()).placeholder(R.drawable.placeholder_music)
                        .into((ImageView) holder.getView(R.id.iv_rd_radio));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlaylistActivity.class);
                        intent.putExtra(Constant.EXTRA_RADIO,radioBean);
                        startActivity(intent);
                    }
                });
            }
        };
        rv_radio.setLayoutManager(new GridLayoutManager(getActivity(),3));
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        loadMoreWrapper.setLoadMoreView(R.layout.loadmore);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (canLoadMore){
                    model.getRadioData(nextpage,12,RadioFragment.this);
                }
            }
        });
        rv_radio.setAdapter(loadMoreWrapper);
        rv_radio.addItemDecoration(decoration);
        rv_radio.setHasFixedSize(true);

    }

    @Override
    public void OnRadioData(List<RadioBean> data , int maxCount) {
        showContent();
        nextpage++;
        mData.addAll(data);
        loadMoreWrapper.notifyDataSetChanged();
        if (loadMoreWrapper.getItemCount()-1 >= maxCount ){
            canLoadMore = false;
            loadMoreWrapper.setLoadMoreView(R.layout.nomore);
            rv_radio.setAdapter(loadMoreWrapper);
        }else{
            canLoadMore = true;
        }
    }

    @Override
    public void OnFail(String msg) {
        showError();
    }


}
