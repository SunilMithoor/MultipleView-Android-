package com.app.data.local.assets

import android.content.Context
import com.app.domain.entity.response.Datas
import com.app.domain.entity.response.MultipleViewData
import com.app.domain.model.RawCallResponse
import com.app.presentation.extension.AppRaw
import com.app.presentation.extension.loadJSONFromRaw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class RawDataSource @Inject constructor(private val context: Context) {


//    suspend fun <T : Any> readDataFromRaw(): Flow<IOTaskResult<T>> {
//        return flow {
//            val readData = context.loadJSONFromRaw(AppRaw.multipleview)
//            val jsonObject = JSONObject(readData)
//            val datas = Datas()
//            datas.id = jsonObject.getInt("id")
//            datas.form = jsonObject.getString("form")
//            datas.formId = jsonObject.getString("formId")
//            val jsonArray = jsonObject.getJSONArray("data")
//            val lists: MutableList<MultipleViewData> = ArrayList()
//            for (i in 0 until jsonArray.length()) {
//                val jsonObjects = jsonArray.getJSONObject(i)
//                val multipleViewData = MultipleViewData()
//                multipleViewData.id = jsonObjects.getInt("id")
//                multipleViewData.question = jsonObjects.getString("question")
//                multipleViewData.questionId = jsonObjects.getInt("questionId")
//                multipleViewData.questionType = jsonObjects.getString("questionType")
//                val jsonArray = jsonObjects.getJSONArray("questionChoices")
//                if (jsonArray != null && jsonArray.length() > 0) {
//                    val questionChoicesList = ArrayList<String>()
//                    for (i in 0 until jsonArray.length()) {
//                        val jsonObjectData=jsonArray.getJSONObject(i)
//                        questionChoicesList.add(jsonObjectData.toString())
//                    }
//                    multipleViewData.questionChoices = questionChoicesList
//                }
//                lists.add(multipleViewData)
//            }
//            datas.data = lists
//            Timber.d("result data::$lists")
//            emit(IOTaskResult.OnSuccess(datas))
//            return@flow
//        }.catch {
//            Timber.d("Error exception")
//            emit(IOTaskResult.OnFailed(Exception("Error")))
//            return@catch
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun readData(): Flow<RawCallResponse<Datas>>? {
        return flow {
            try {
                val readData = context.loadJSONFromRaw(AppRaw.multipleview)
                val jsonObject = JSONObject(readData)
                val datas = Datas()
                datas.id = jsonObject.getInt("id")
                datas.form = jsonObject.getString("form")
                datas.formId = jsonObject.getString("formId")
                val jsonArray = jsonObject.getJSONArray("data")
                val lists: MutableList<MultipleViewData> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val jsonObjects = jsonArray.getJSONObject(i)
                    val multipleViewData = MultipleViewData()
                    multipleViewData.id = jsonObjects.optInt("id")
                    multipleViewData.question = jsonObjects.optString("question")
                    multipleViewData.questionId = jsonObjects.optInt("questionId")
                    multipleViewData.questionType = jsonObjects.optString("questionType")
                    val jsonArray = jsonObjects.optJSONArray("questionChoices")
                    if (jsonArray != null && jsonArray.length() > 0) {
                        val questionChoicesList = ArrayList<String>()
                        for (i in 0 until jsonArray.length()) {
                            questionChoicesList.add(jsonArray.getString(i))
                        }
                        multipleViewData.questionChoices = questionChoicesList
                    }
                    lists.add(multipleViewData)
                }
                datas.data = lists
                Timber.d("result data::$lists")
                emit(RawCallResponse.OnSuccess(datas))
            } catch (e: Exception) {
                Timber.d("Exception::$e")
                emit(RawCallResponse.OnFailed(e))
            }
            return@flow
        }.flowOn(Dispatchers.IO)
    }
}

