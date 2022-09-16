package com.app.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseFragmentStateAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val _list = mutableListOf<Fragment>()
    var list: List<Fragment> = mutableListOf()
        set(items) {
            field = items
            submitList(items)
        }

    fun submitList(list: List<Fragment> = emptyList()) {
        _list.clear()
        _list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = _list.size

    override fun createFragment(position: Int): Fragment {
        return _list[position]
    }
}