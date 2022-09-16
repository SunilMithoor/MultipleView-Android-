package com.app.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.domain.entity.response.SelectedChoice
import kotlinx.android.synthetic.main.multiple_choice_selection_view.view.*


class MultiChoiceSelectionAdapter(
    var context: Context,
    private val itemList: MutableList<SelectedChoice>,
    private var selectedItemList: MutableList<SelectedChoice>
) :
    RecyclerView.Adapter<MultiChoiceSelectionAdapter.MultiChoiceItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiChoiceItemHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.multiple_choice_selection_view, parent, false)
        return MultiChoiceItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MultiChoiceItemHolder, position: Int) {
        holder.checkBox.text = itemList[position].choice
//        holder.checkBox.isChecked = choiceList[position].answer_choice

        if (selectedItemList.size > 0 && selectedItemList[position].choiceSelected) {
            holder.checkBox.isChecked = true
        } else {
            if (itemList[position].choiceSelected) {
                holder.checkBox.isChecked = itemList[position].choiceSelected
            } else {
                holder.checkBox.isChecked = false
            }
        }

    }

    inner class MultiChoiceItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener {
        val checkBox = itemView.checkBox!!


        init {
            checkBox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(p0: CompoundButton?, status: Boolean) {
//            selectedItemList[adapterPosition].choiceSelected = status
            itemList[adapterPosition].choiceSelected = status
        }

    }
}