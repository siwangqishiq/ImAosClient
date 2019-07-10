package com.xinlan.imsdk.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xinlan.imsdk.support.GlideApp;

public class UrlImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String THUMB_SUFFIX = "!thumb";
    private static final String THUMB_HEAD = "!thumbtiny";

    public interface ILoadListener{
        void onSuccess();
        void onFailed();
    }

    public UrlImageView(Context context) {
        super(context);
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImageThumb(String imgUrl, int defaultImageId) {
        GlideApp.with(getContext()).load(imgUrl + THUMB_SUFFIX).placeholder(defaultImageId).into(this);
    }

    public void loadImageThumb(String imgUrl) {
        GlideApp.with(getContext()).load(imgUrl + THUMB_SUFFIX).into(this);
    }

    public void loadImageThumb(final String imgUrl ,final ILoadListener listener){
        GlideApp.with(getContext()).load(imgUrl + THUMB_SUFFIX).listener(new RequestListener<Drawable>(){
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if(listener != null){
                    listener.onFailed();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if(listener != null){
                    listener.onSuccess();
                }
                return false;
            }
        }).into(this);
    }

    public void loadImage(String imgUrl) {
        GlideApp.with(getContext()).load(imgUrl).into(this);
    }

    public void loadImageAvatar(String imgUrl) {
        GlideApp.with(getContext()).load(imgUrl + THUMB_HEAD).into(this);
    }
}
