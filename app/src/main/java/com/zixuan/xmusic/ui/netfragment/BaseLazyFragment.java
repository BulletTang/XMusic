package com.zixuan.xmusic.ui.netfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zixuan.xmusic.R;

import me.xdj.view.MultiStateView;


public abstract class BaseLazyFragment extends Fragment{

    protected boolean isPrepared;
    protected boolean isVisible;
    protected boolean hasLoad;
    protected MultiStateView contentContainer;
    protected View rootView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser){
            onVisible();
        }else {
            onInVisible();
        }
    }

    protected void onInVisible() {

    }

    protected void onVisible() {
        if (isPrepared && !hasLoad){

            lazyLoad();
            hasLoad = true;
        }
    }

    protected abstract void lazyLoad() ;
    protected abstract int getLayoutId();

    protected abstract void initView();
    protected abstract void initListener();
    protected abstract void retryAction();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentContainer == null){
            contentContainer = (MultiStateView) inflater.inflate(R.layout.load_container,container,false);
            rootView = inflater.inflate(getLayoutId(),container,false);
            contentContainer.addView(rootView);
            contentContainer.setOnInflateListener(new MultiStateView.OnInflateListener() {
                @Override
                public void onInflate(int state, View view) {
                    if (state == MultiStateView.STATE_FAIL){
                        view.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retryAction();
                            }
                        });
                    }
                }
            });
            initView();
            initListener();
        }
        isPrepared = true;
        return contentContainer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isVisible && !hasLoad) {
            lazyLoad();
            hasLoad = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) contentContainer.getParent();
        if (parent != null){
            parent.removeView(contentContainer);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showContent(){
        contentContainer.setViewState(MultiStateView.STATE_CONTENT);
    }

    public void showLoading(){
        contentContainer.setViewState(MultiStateView.STATE_LOADING);
    }

    public void showError(){
        contentContainer.setViewState(MultiStateView.STATE_FAIL);
    }
}
