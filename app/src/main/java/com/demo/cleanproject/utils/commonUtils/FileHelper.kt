package com.demo.cleanproject.utils.commonUtils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.core.graphics.scale
import androidx.core.graphics.toColorInt
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileHelper @Inject constructor(@param:ApplicationContext private val context: Context) {

    val imageDirectory: String = "CleanProject"
    var imgFormat: String = "png"

    @Throws(IOException::class)
    fun saveImage(myBitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        myBitmap?.compress(
            if (imgFormat.equals(
                    "jpg",
                    ignoreCase = true
                )
            ) Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG, 100, stream
        )

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/ThumbnailMaker"
        val wallpaperDirectory = File(storageDir, imageDirectory)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory,
                Calendar.getInstance().timeInMillis.toString() + "." + imgFormat
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(stream.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            return f.absolutePath
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return ""
    }

    @Throws(IOException::class)
    fun savePDF(myBitmap: Bitmap? ): String {
        var myBitmap = myBitmap
        val stream = ByteArrayOutputStream()
        myBitmap?.compress(
            if (imgFormat.equals("jpg", ignoreCase = true)
            ) Bitmap.CompressFormat.JPEG
            else Bitmap.CompressFormat.PNG, 100, stream
        )

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/ThumbnailMaker"

        val wallpaperDirectory = File(storageDir, imageDirectory)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory,
                Calendar.getInstance().timeInMillis.toString() + "." + imgFormat
            )

            val document = PdfDocument()
            try {
                val pageInfo = PageInfo.Builder(myBitmap!!.width, myBitmap.height, 1).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint()
                paint.color = "#ffffff".toColorInt()
                canvas.drawPaint(paint)
                myBitmap = myBitmap.scale(myBitmap.width, myBitmap.height)
                canvas.drawBitmap(myBitmap, 0f, 0f, null)
                document.finishPage(page)
                document.writeTo(FileOutputStream(f))
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                document.close()
            }
            return f.absolutePath
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return ""
    }


    @Throws(IOException::class)
    fun saveImageGalleryVisible(myBitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        myBitmap?.compress(
            if (imgFormat.equals("jpg", ignoreCase = true)
            ) Bitmap.CompressFormat.JPEG
            else Bitmap.CompressFormat.PNG, 100, stream
        )

        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

        val wallpaperDirectory = File(storageDir, imageDirectory)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory,
                Calendar.getInstance().timeInMillis.toString() + "." + imgFormat
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(stream.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            return f.absolutePath
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return ""
    }

    @Throws(IOException::class)
    fun savePDFGalleryVisible(myBitmap: Bitmap?): String {
        var myBitmap = myBitmap
        val stream = ByteArrayOutputStream()
        myBitmap?.compress(
            if (imgFormat.equals("jpg", ignoreCase = true))
                Bitmap.CompressFormat.JPEG
            else Bitmap.CompressFormat.PNG, 100, stream
        )

        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

        val wallpaperDirectory = File(storageDir, imageDirectory)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory,
                Calendar.getInstance().timeInMillis.toString() + "." + imgFormat
            )

            val document = PdfDocument()
            try {
                val pageInfo = PageInfo.Builder(myBitmap!!.width, myBitmap.height, 1).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint()
                paint.color = "#ffffff".toColorInt()
                canvas.drawPaint(paint)
                myBitmap = myBitmap.scale(myBitmap.width, myBitmap.height)
                canvas.drawBitmap(myBitmap, 0f, 0f, null)
                document.finishPage(page)
                document.writeTo(FileOutputStream(f))
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                document.close()
            }
            return f.absolutePath
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return ""
    }

    fun isFileExist(file: File): Boolean {
        return file.exists()
    }

    fun loadJSONFromAssets(fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    fun getMediaDurationInMillis(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        } finally {
            retriever.release()
        }
    }

}