package com.lxp.here.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/* 功能描述
 * @author lixianpeng
 * @since 2021-12-04
 */

object ZipUtils {
    fun compress(data:String) : String {
        if (data.isEmpty()) {
            return data
        }
        val byteArrayOutputStream:ByteArrayOutputStream = ByteArrayOutputStream()
        val gzipOutputStream:GZIPOutputStream = GZIPOutputStream(byteArrayOutputStream)
        gzipOutputStream.write(data.toByteArray(StandardCharsets.UTF_8))
        gzipOutputStream.finish()
        gzipOutputStream.close()
        return byteArrayOutputStream.toString("UTF-8")
    }

    fun uncompress(data: String):String {
        val byteArrayOutputStream:ByteArrayOutputStream = ByteArrayOutputStream()
        val byteArrayInputStream:ByteArrayInputStream = ByteArrayInputStream(data.toByteArray(StandardCharsets.UTF_8))
        val gzipInputStream:GZIPInputStream = GZIPInputStream(byteArrayInputStream)
        val buffer:ByteArray = ByteArray(2048)
        var n: Int
        while (gzipInputStream.read(buffer).also { n = it } >= 0) {
            byteArrayOutputStream.write(buffer, 0, n)
        }
        return byteArrayOutputStream.toString("UTF-8")
    }
}