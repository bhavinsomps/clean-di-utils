package com.demo.cleanproject.utils.commonUtils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.roundToInt

@ActivityScoped
class ImageHelper @Inject constructor(@param:ActivityContext private val context: Context) {

    var imgFormat: String = "png"
    var myBitmap: Bitmap? = null
    var imgQuality: String = ""

    fun takeScreenShot(
        view: View,
        format: String,
        quality: String,
    ): Bitmap {
        val verySmall = 400
        val lowQualitySize = 600
        val highQualitySize = 1800

        imgFormat = format
        imgQuality = quality

        myBitmap = viewToBitmap(view)

        myBitmap = if (imgQuality.equals("high", ignoreCase = true)) {
            scaleBitmapRatio(myBitmap!!, highQualitySize)
        } else if (imgQuality.equals("low", ignoreCase = true)) {
            scaleBitmapRatio(myBitmap!!, lowQualitySize)
        } else {
            scaleBitmapRatio(myBitmap!!, verySmall)
        }

        return myBitmap as Bitmap
    }

    fun scaleBitmapRatio(bitmap: Bitmap, desireSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = desireSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = desireSize
            width = (height * bitmapRatio).toInt()
        }

        return scaleBitmap(bitmap, width, height)
    }

    fun scaleBitmap(bitmap: Bitmap, wantedWidth: Int, wantedHeight: Int): Bitmap {
        val output = createBitmap(wantedWidth, wantedHeight)
        val canvas = Canvas(output)
        val m = Matrix()
        m.setScale(wantedWidth.toFloat() / bitmap.width, wantedHeight.toFloat() / bitmap.height)
        canvas.drawBitmap(bitmap, m, Paint())
        Log.d("newBitmap", "run: $myBitmap")
        return output
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return image.scale(width, height)
    }

    fun viewToBitmap(v: View): Bitmap {
        val b = createBitmap(v.layoutParams.width, v.layoutParams.height)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    fun captureScreen(v: View?, scaleFactor: Float): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            if (v != null) {
                screenshot = createBitmap(
                    (v.measuredWidth * scaleFactor).roundToInt(),
                    (v.measuredHeight * scaleFactor).roundToInt()
                )

                val canvas = Canvas(screenshot)
                v.draw(canvas)
            }
        } catch (e: java.lang.Exception) {
        }
        return screenshot
    }

    fun dpToPixel(myDpValue: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            myDpValue.toFloat(),
            context.resources.displayMetrics
        ).toFloat()
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            return ""
        }
    }

    fun base64ToBitmap(str: String?): Bitmap {
        val decodedString = Base64.decode(str, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }

}