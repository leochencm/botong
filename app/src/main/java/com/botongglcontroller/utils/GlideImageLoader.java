package com.botongglcontroller.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.botongglcontroller.R;

import android.content.Context;

import android.widget.ImageView;

public class GlideImageLoader {
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
//        Glide.with(context).load(path).into(imageView);

        Glide.with(context).
                load(path).
                asBitmap().
                placeholder(R.mipmap.no_photo).//加载中显示的图片
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                error(R.mipmap.plugin_camera_no_pictures).//加载失败时显示的图片
                into(imageView);
    }
}
