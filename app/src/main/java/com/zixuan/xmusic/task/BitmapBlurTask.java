package com.zixuan.xmusic.task;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.BitmapUtils;

import java.lang.ref.WeakReference;


public class BitmapBlurTask extends AsyncTask<Bitmap,Void,Bitmap> {
    private WeakReference<View> backgroundView;

    private static final String TAG = "BitmapBlurTask";

    public BitmapBlurTask(WeakReference<View> backgroundView) {
        this.backgroundView = backgroundView;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params) {
        return BitmapUtils.createBlurredImageFromBitmap(params[0],XMusicApplication.getContext(),6);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        backgroundView.get().setBackgroundDrawable(new BitmapDrawable(bitmap));

    }
}
