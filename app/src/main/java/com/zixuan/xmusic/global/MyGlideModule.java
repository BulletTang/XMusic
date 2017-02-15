package com.zixuan.xmusic.global;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;


public class MyGlideModule implements GlideModule {

    //Glide磁盘缓存大小
    private static final int GLIDE_DISKCACHE_MAX_SIZE = 250 * 1024 * 1024;


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //使用内部储存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,"glide_cache",GLIDE_DISKCACHE_MAX_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
