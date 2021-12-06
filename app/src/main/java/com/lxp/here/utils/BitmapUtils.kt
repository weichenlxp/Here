package com.lxp.here.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/* 功能描述
 * @author lixianpeng
 * @since 2021-12-04
 */

object BitmapUtils {
    fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }
    fun byteArrayToBitmap(b:ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(b, 0, b.size);
    }
}