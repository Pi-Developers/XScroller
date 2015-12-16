package com.pi_developers.xscroller.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;


public class Utils {
    public static boolean isModuleActive() {
        return false;
    }

    public static Bitmap resizeBitmapAccordingToDevice(Context context,int id_res) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id_res);
        return getResizedBitmap(bitmap,displayMetrics.widthPixels,displayMetrics.heightPixels);
    }
    public  static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);


        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
