package com.app.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.app.R
import com.google.android.material.textview.MaterialTextView


class SingleChoiceSpinnerAdapter(
    context: Context,
    private var itemList: MutableList<String>
) :
    BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.single_choice_spinner_view, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        vh.tvCategory.text = itemList[position].toString()
        return view
    }

    override fun getItem(position: Int): Any? {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    private inner class ItemRowHolder(row: View?) {
        val tvCategory: MaterialTextView = row?.findViewById(R.id.tvItem) as MaterialTextView
    }

}