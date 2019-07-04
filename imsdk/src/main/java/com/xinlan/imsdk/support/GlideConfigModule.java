package com.xinlan.imsdk.support;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class GlideConfigModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.format(DecodeFormat.DEFAULT);
        builder.setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}//end class
