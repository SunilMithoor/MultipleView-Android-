package com.app.presentation.ui.viewholder

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import com.app.databinding.*
import com.app.domain.entity.response.MultipleViewData
import com.app.domain.entity.response.SelectedChoice
import com.app.domain.extension.showDatePicker
import com.app.domain.extension.showTimePicker
import com.app.presentation.base.BaseViewHolder
import com.app.presentation.extension.AppString
import com.app.presentation.extension.click
import com.app.presentation.ui.adapter.MultiChoiceSelectionAdapter
import com.app.presentation.ui.adapter.SingleChoiceSpinnerAdapter
import com.app.presentation.ui.adapter.SurveyImageAdapter
import com.app.presentation.ui.adapter.SurveyVideoAdapter
import com.app.presentation.util.ImageController
import timber.log.Timber


class LongAnswerViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = LongAnswerItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()
        binding.root.click {
            itemClickListener?.invoke(position, item)
        }
        binding.etAnswer.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                item.answer = s.toString()
            }
        })
    }
}

class ShortAnswerViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = ShortAnswerItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }
        binding.etAnswer.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                item.answer = s.toString()
            }
        })
    }

}


class VideoViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = VideoItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

//        binding.root.click {
//            itemClickListener?.invoke(position, item)
//        }

        binding.imgVideoAdd.click {
            Timber.d("clicked")
            Timber.d("itemViewClickListener:: $itemViewClickListener")
            itemViewClickListener?.invoke(id, position, item)
        }

        Timber.d("get video path:: ${item.uriPaths}")
        if (item.uriPaths != null) {
            val withActivityController = ImageController(binding.imgVideoView)
            val surveyVideoAdapter =
                SurveyVideoAdapter(context, withActivityController, item.uriPaths as ArrayList<Uri>)
            binding.recyclerView.adapter = surveyVideoAdapter
            binding.recyclerView.visibility = View.VISIBLE
            binding.imgVideoView.visibility = View.GONE
        }

    }

}

class ImageViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = ImageItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

//        binding.root.click {
//            itemClickListener?.invoke(position, item)
//        }

        binding.imgImageAdd.click {
            Timber.d("clicked")
            Timber.d("itemViewClickListener:: $itemViewClickListener")
            itemViewClickListener?.invoke(id, position, item)
        }

        Timber.d("get image path:: ${item.uriPaths}")
        if (item.uriPaths != null) {
            val withActivityController = ImageController(binding.imgImageView)
            val surveyImageAdapter =
                SurveyImageAdapter(context, withActivityController, item.uriPaths as ArrayList<Uri>)
            binding.recyclerView.adapter = surveyImageAdapter
            binding.recyclerView.visibility = View.VISIBLE
            binding.imgImageView.visibility = View.GONE
        }
    }

}


class MultipleChoiceViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = MultipleChoiceItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }
        if (item.questionChoices != null && item.questionChoices!!.isNotEmpty()) {
            val itemList = mutableListOf<SelectedChoice>()
            val itemSelectedList = mutableListOf<SelectedChoice>()
            if (itemList.isEmpty()) {
                for (i in 0 until item.questionChoices!!.size) {
                    itemList.add(SelectedChoice(false, item.questionChoices!![i].toString()))
                }
            }
            val multiChoiceSelectionAdapter = MultiChoiceSelectionAdapter(
                context, itemList, itemSelectedList
            )
            binding.recyclerView.adapter = multiChoiceSelectionAdapter
        }
    }

}

class SingleChoiceViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = SingleChoiceItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }

        var selectedChoicePosition = 0
        val listQuestionChoices: MutableList<String> = ArrayList()
        if (item.questionChoices != null && item.questionChoices!!.isNotEmpty()) {
            listQuestionChoices.add(context.getString(AppString.select_item_from_list))
            for (i in 0 until item.questionChoices?.size!!) {
                listQuestionChoices.add(item.questionChoices!![i].toString())
            }
            val singleChoiceSpinnerAdapter = SingleChoiceSpinnerAdapter(
                context, listQuestionChoices
            )
            binding.spinner.adapter = singleChoiceSpinnerAdapter
            binding.spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedChoicePosition = id.toInt()
//                        item.answer = item.questionChoices!![position]
                        Timber.d("Selected pos:: $position")
                        Timber.d("Selected item:: $id")

                    }
                }
        }
    }
}


class DateViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = DatePickerItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }

        binding.etAnswer.click {
            context.showDatePicker(
                {
                    binding.etAnswer.setText(it)
                    item.answer = it.toString()
                }, 0, "", ""
            )

        }
    }
}

class TimeViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = TimePickerItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()
        binding.tvQuestion.text = item.question.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }

        binding.etAnswer.click {
            context.showTimePicker(
                {
                    binding.etAnswer.setText(it)
                    item.answer = it.toString()
                }, 0, "", ""
            )

        }
    }
}

class NoDataViewHolder(val view: View) : BaseViewHolder<MultipleViewData>(view) {

    override fun bind(position: Int, item: MultipleViewData) {
        val binding = NoDataItemBinding.bind(view)
        binding.tvTitle.text = item.questionType.toString()

        binding.root.click {
            itemClickListener?.invoke(position, item)
        }
    }

}

