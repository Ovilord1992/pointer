package com.pointer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class BitmapDescriptor {

    public static com.google.android.gms.maps.model.BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDravable = ContextCompat.getDrawable(context,vectorResId);
        vectorDravable.setBounds(0,0,vectorDravable.getIntrinsicWidth(),vectorDravable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDravable.getIntrinsicWidth(),vectorDravable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDravable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
