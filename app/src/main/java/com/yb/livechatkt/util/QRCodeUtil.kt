package com.yb.livechatkt.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.apache.lucene.portmobile.charset.StandardCharsets

object QRCodeUtil {

    var result: String = ""

    fun createQRImage(
        content: String?, widthPix: Int, heightPix: Int
    ): Bitmap? {
        try {
            val hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = StandardCharsets.UTF_8.name()
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 1
            var bitMatrix: BitMatrix? = null
            try {
                bitMatrix = QRCodeWriter().encode(
                    content, BarcodeFormat.QR_CODE, widthPix,
                    heightPix, hints
                )
            } catch (e: WriterException) {
            }
            val pixels = IntArray(widthPix * heightPix)
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix != null) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * widthPix + x] = -0x1000000
                        } else {
                            pixels[y * widthPix + x] = -0x1
                        }
                    } else {
                        return null
                    }
                }
            }
            val bitmap: Bitmap? = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap!!.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            return bitmap
        } catch (e: Exception) {

        }
        return null
    }
}