package edu.hebut.here.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {
    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
