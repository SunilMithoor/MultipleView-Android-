package com.app.mediapicker.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.GLException
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.app.mediapicker.cropper.Constants.APP_DOCUMENTS_DIRECTORY_NAME
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.IntBuffer
import java.text.SimpleDateFormat
import java.util.*
import javax.microedition.khronos.opengles.GL10


class FileUtil {

    companion object {
        // Returns the File for a photo stored on disk given the fileName
        fun getPhotoFileUri(context: Context, fileName: String): File? {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            val mediaStorageDir =
                File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "App_Photo_folder"
                )

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                // Log.d(TAG, "failed to create directory")
            }
            // Return the file target for the photo based on filename
            return File(mediaStorageDir.path + File.separator + fileName)
        }

        val getBase64String = { bitmap: Bitmap ->
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos)
            val imageBytes = baos.toByteArray()
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }

        /* val getFilename = { photoFile!!.absolutePath }*/

        fun getFileProvider(context: Context, photoFile: File): Uri {
            return FileProvider.getUriForFile(
                context, context.packageName + ".provider",
                photoFile
            )
        }


        fun createImageFiles(foldername: String, filename: String): File? {
            val loc =
                Environment.getExternalStorageDirectory().toString() + File.separator + foldername
            val path = File(loc)
            // Make sure the path directory exists.
            if (!path.exists()) {
                // Make it, if it doesn't exit
                path.mkdirs()
            }
            //        final File file = new File(path, "config.txt");
            val file = File(path, "$filename.jpg")
            // Save your stream, don't forget to flush() it before closing it.
            try {
                if (!file.exists()) {
                    file.createNewFile()
                }
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: " + e.toString())
            }
            return file
        }

        fun deleteDirectory(path: File): Boolean {
            try {
                if (path.exists()) {
                    val files = path.listFiles() ?: return true
                    for (file in files) {
                        if (file.isDirectory) {
                            deleteDirectory(file)
                        } else {
                            file.delete()
                        }
                    }
                }
                return path.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun deleteFolder(folderName: String): Boolean {
            return try {
                val dir =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + folderName)
                if (dir.isDirectory) {
                    val children = dir.list()
                    for (aChildren in children) {
                        File(dir, aChildren).delete()
                    }
                }
                true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                false
            }
        }

        fun bitmapToFile(context: Context?, bitmap: Bitmap, filename: String): File? {
            // File name like "image.png"
            //create a file to write bitmap data
            try {
                val loc = Environment.getExternalStorageDirectory()
                    .toString() + File.separator + APP_DOCUMENTS_DIRECTORY_NAME
                val path = File(loc)
                // Make sure the path directory exists.
                if (!path.exists()) {
                    // Make it, if it doesn't exit
                    path.mkdirs()
                }
                val file = File(path, "$filename.jpg")
                if (!file.exists()) {
                    file.createNewFile()
                }
                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 99, bos) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                return file
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }


        fun getAndroidImageFolder(): File? {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }

        fun getImageFilePath(): String? {
            return getAndroidImageFolder()?.absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(
                Date()
            ) + "GPUCameraRecorder.png"
        }

        fun createBitmapFromGLSurface(w: Int, h: Int, gl: GL10): Bitmap? {
            val bitmapBuffer = IntArray(w * h)
            val bitmapSource = IntArray(w * h)
            val intBuffer = IntBuffer.wrap(bitmapBuffer)
            intBuffer.position(0)
            try {
                gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer)
                var offset1: Int
                var offset2: Int
                var texturePixel: Int
                var blue: Int
                var red: Int
                var pixel: Int
                for (i in 0 until h) {
                    offset1 = i * w
                    offset2 = (h - i - 1) * w
                    for (j in 0 until w) {
                        texturePixel = bitmapBuffer[offset1 + j]
                        blue = texturePixel shr 16 and 0xff
                        red = texturePixel shl 16 and 0x00ff0000
                        pixel = texturePixel and -0xff0100 or red or blue
                        bitmapSource[offset2 + j] = pixel
                    }
                }
            } catch (e: GLException) {
                Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.message, e)
                return null
            }
            return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888)
        }

        fun getVideoFilePath(): String? {
            return getAndroidMoviesFolder()?.absolutePath + "/" + SimpleDateFormat("yyyy_MM_dd-HHmmss").format(
                Date()
            ) + "_VideoRecorder.mp4"
        }

        fun exportMp4ToGallery(context: Context, filePath: String) {
            val values = ContentValues(2)
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            values.put(MediaStore.Video.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values
            )
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://$filePath")
                )
            )
        }

        fun getAndroidMoviesFolder(): File? {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }

    }
}