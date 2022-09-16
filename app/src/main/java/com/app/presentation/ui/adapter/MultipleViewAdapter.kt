package com.app.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.utils.MultipleViewItems
import com.app.data.utils.MultipleViewTypes.DATE_CODE
import com.app.data.utils.MultipleViewTypes.IMAGE_CODE
import com.app.data.utils.MultipleViewTypes.LONG_ANSWER_CODE
import com.app.data.utils.MultipleViewTypes.MULTIPLE_CHOICE_CODE
import com.app.data.utils.MultipleViewTypes.SHORT_ANSWER_CODE
import com.app.data.utils.MultipleViewTypes.SINGLE_CHOICE_CODE
import com.app.data.utils.MultipleViewTypes.TIME_CODE
import com.app.data.utils.MultipleViewTypes.VIDEO_CODE
import com.app.databinding.*
import com.app.domain.entity.response.MultipleViewData
import com.app.presentation.base.BaseListAdapter
import com.app.presentation.base.DifCallback
import com.app.presentation.ui.viewholder.*


class MultipleViewAdapter :
    BaseListAdapter<RecyclerView.ViewHolder, MultipleViewData>(DifCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.questionType) {
            MultipleViewItems.LONG_ANSWER -> {
                LONG_ANSWER_CODE
            }
            MultipleViewItems.SHORT_ANSWER -> {
                SHORT_ANSWER_CODE
            }
            MultipleViewItems.VIDEO -> {
                VIDEO_CODE
            }
            MultipleViewItems.IMAGE -> {
                IMAGE_CODE
            }
            MultipleViewItems.MULTIPLE_CHOICE -> {
                MULTIPLE_CHOICE_CODE
            }
            MultipleViewItems.SINGLE_CHOICE -> {
                SINGLE_CHOICE_CODE
            }
            MultipleViewItems.DATE -> {
                DATE_CODE
            }
            MultipleViewItems.TIME -> {
                TIME_CODE
            }
            else -> {
                -1
            }
        }
    }

    override fun onCreateItemViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            LONG_ANSWER_CODE -> {
                LongAnswerViewHolder(
                    LongAnswerItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            SHORT_ANSWER_CODE -> {
                ShortAnswerViewHolder(
                    ShortAnswerItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            VIDEO_CODE -> {
                VideoViewHolder(
                    VideoItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            IMAGE_CODE -> {
                ImageViewHolder(
                    ImageItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            MULTIPLE_CHOICE_CODE -> {
                MultipleChoiceViewHolder(
                    MultipleChoiceItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            SINGLE_CHOICE_CODE -> {
                SingleChoiceViewHolder(
                    SingleChoiceItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            DATE_CODE -> {
                DateViewHolder(
                    DatePickerItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            TIME_CODE -> {
                TimeViewHolder(
                    TimePickerItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
            }
            else -> {
                NoDataViewHolder(
                    NoDataItemBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    ).root
                )
//                throw RuntimeException("The type has to be 1 or 2")
            }
        }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.questionType) {
            MultipleViewItems.LONG_ANSWER -> {
                (holder as LongAnswerViewHolder).bind(position, item)
            }
            MultipleViewItems.SHORT_ANSWER -> {
                (holder as ShortAnswerViewHolder).bind(position, item)
            }
            MultipleViewItems.VIDEO -> {
                (holder as VideoViewHolder).bind(position, item)
                holder.itemViewClickListener=itemViewClickListener
            }
            MultipleViewItems.IMAGE -> {
                (holder as ImageViewHolder).bind(position, item)
                holder.itemViewClickListener=itemViewClickListener
            }
            MultipleViewItems.MULTIPLE_CHOICE -> {
                (holder as MultipleChoiceViewHolder).bind(position, item)
            }
            MultipleViewItems.SINGLE_CHOICE -> {
                (holder as SingleChoiceViewHolder).bind(position, item)
            }
            MultipleViewItems.DATE -> {
                (holder as DateViewHolder).bind(position, item)
            }
            MultipleViewItems.TIME -> {
                (holder as TimeViewHolder).bind(position, item)
            }
            else -> {
                (holder as NoDataViewHolder).bind(position, item)
            }
        }
    }

}