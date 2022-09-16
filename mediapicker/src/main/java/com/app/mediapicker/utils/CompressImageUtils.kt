package com.app.mediapicker.utils

import android.graphics.*
import android.os.Environment
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

object CompressImageUtils {
    var imgRatio = 0f
    var maxRatio = 0f

    /**
     * @param filePath
     * @return compressedImage filepath
     */
    @JvmStatic
    fun compressImage(foldername: String?, filePath: String?, filenames: String): String {
        var filename = ""
        try {
            if (filePath != null) {
                var scaledBitmap: Bitmap? = null
                val options = BitmapFactory.Options()
                //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
                //      you try the use the bitmap here, you will get null.
                options.inJustDecodeBounds = true
                var bmp = BitmapFactory.decodeFile(filePath, options)
                var actualHeight = options.outHeight
                var actualWidth = options.outWidth
                if (actualHeight > 0 && actualWidth > 0) {
                    try {
                        //      max Height and width values of the compressed image is taken as 816x612
                        val maxHeight = 816.0f
                        val maxWidth = 612.0f
                        try {
                            imgRatio = (actualWidth / actualHeight).toFloat()
                            maxRatio = maxWidth / maxHeight
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //      width and height values are set maintaining the aspect ratio of the image
                        if (actualHeight > maxHeight || actualWidth > maxWidth) {
                            if (imgRatio < maxRatio) {
                                imgRatio = maxHeight / actualHeight
                                actualWidth = (imgRatio * actualWidth).toInt()
                                actualHeight = maxHeight.toInt()
                            } else if (imgRatio > maxRatio) {
                                imgRatio = maxWidth / actualWidth
                                actualHeight = (imgRatio * actualHeight).toInt()
                                actualWidth = maxWidth.toInt()
                            } else {
                                actualHeight = maxHeight.toInt()
                                actualWidth = maxWidth.toInt()
                            }
                        }
                        //      setting inSampleSize value allows to load a scaled down version of the original image
                        options.inSampleSize =
                            calculateInSampleSize(options, actualWidth, actualHeight)
                        //      inJustDecodeBounds set to false to load the actual bitmap
                        options.inJustDecodeBounds = false
                        //      this options allow android to claim the bitmap memory if it runs low on memory
                        options.inPurgeable = true
                        options.inInputShareable = true
                        options.inTempStorage = ByteArray(16 * 1024)
                        try {
                            //          load the bitmap from its path
                            bmp = BitmapFactory.decodeFile(filePath, options)
                        } catch (exception: OutOfMemoryError) {
                            exception.printStackTrace()
                        }
                        try {
                            scaledBitmap = Bitmap.createBitmap(
                                actualWidth,
                                actualHeight,
                                Bitmap.Config.ARGB_8888
                            )
                        } catch (exception: OutOfMemoryError) {
                            exception.printStackTrace()
                        }
                        val ratioX = actualWidth / options.outWidth.toFloat()
                        val ratioY = actualHeight / options.outHeight.toFloat()
                        val middleX = actualWidth / 2.0f
                        val middleY = actualHeight / 2.0f
                        val scaleMatrix = Matrix()
                        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
                        var canvas: Canvas? = null
                        if (scaledBitmap != null) {
                            canvas = Canvas(scaledBitmap)
                        }
                        if (canvas != null) {
                            canvas.setMatrix(scaleMatrix)
                            canvas.drawBitmap(
                                bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(
                                    Paint.FILTER_BITMAP_FLAG
                                )
                            )
                        }

                        //      check the rotation of the image and display it properly
                        val exif: ExifInterface
                        try {
                            println("Filepath-->$filePath")
                            exif = ExifInterface(filePath)
                            val orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, 0
                            )
                            Log.d("EXIF", "Exif: $orientation")
                            val matrix = Matrix()
                            if (orientation == 6) {
                                matrix.postRotate(90f)
                                Log.d("EXIF", "Exif: $orientation")
                            } else if (orientation == 3) {
                                matrix.postRotate(180f)
                                Log.d("EXIF", "Exif: $orientation")
                            } else if (orientation == 8) {
                                matrix.postRotate(270f)
                                Log.d("EXIF", "Exif: $orientation")
                            }
                            if (scaledBitmap != null) {
                                scaledBitmap = Bitmap.createBitmap(
                                    scaledBitmap, 0, 0,
                                    scaledBitmap.width, scaledBitmap.height, matrix,
                                    true
                                )
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        var out: FileOutputStream? = null
                        val file = File(Environment.getExternalStorageDirectory().path, foldername)
                        if (!file.exists()) {
                            file.mkdirs()
                        }
                        filename = file.absolutePath + "/" + filenames + ".jpg"
                        println("filename-->$filePath")
                        try {
                            out = FileOutputStream(filename)
                            scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filename
    }
    //    /**
    //     *
    //     * @return filename
    //     */
    //    public static String getFilename(String pathname) {
    //        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
    //        if (!file.exists()) {
    //            file.mkdirs();
    //        }
    //        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
    //        return uriSting;
    //    }
    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return imagesize
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }
}