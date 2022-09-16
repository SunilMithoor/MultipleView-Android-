package com.app.presentation.extension

import android.content.Context
import java.io.*


fun Context.loadJSONFromRaw(rawFilePath: Int?): String? {
    var json: String? = null
    val `is`: InputStream = resources.openRawResource(rawFilePath!!)
    val writer: Writer = StringWriter()
    val buffer = CharArray(1024)
    `is`.use { `is` ->
        val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }
    json = writer.toString()
    return json
}
