package com.app.domain.entity.response

import android.net.Uri


data class Datas(
    var id: Int? = null,
    var form: String? = null,
    var formId: String? = null,
    var data: List<MultipleViewData>? = null
)

data class MultipleViewData(
    var id: Int? = null,
    var questionId: Int? = null,
    var questionLabel: String? = null,
    var question: String? = null,
    var questionType: String? = null,
    var questionChoices: List<String>? = null,
    var answer: String? = null,
    var answerChoices: List<String>? = null,
    var isMandatory: Boolean? = false,
    var isAnswered: Boolean? = false,
    var regexPattern: String? = null,
    var uriPaths: List<Uri>? = null

    )

data class SelectedChoice(
    var choiceSelected: Boolean,
    var choice: String
)