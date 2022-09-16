package com.app.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.presentation.extension.activityCompat
import com.app.presentation.extension.hideKeyboard
import com.app.presentation.util.NavigationCommand


abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {


    val navController by lazy {
        findNavController()
    }

    private var isViewCreated = false

    val bundle by lazy {
        arguments ?: Bundle()
    }

    val fragmentActivity by lazy {
        if (activity is AppCompatActivity) {
            activityCompat
        } else {
            null
        }
    }

    fun handleNavigation(command: NavigationCommand) {
        activity?.let {
            if (it is BaseAppCompatActivity) it.handleNavigation(command)
        }
    }

    open fun observeLiveData() {}

//    @SuppressLint("RestrictedApi")
//    open fun onSupportNavigateUp() {
//        if (navController.backStack.size > 2) {
//            navController.navigateUp()
//        } else {
//            finish()
//        }
//    }

    abstract fun onCreate(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.hideKeyboard()
        if (!isViewCreated) {
            isViewCreated = true
            onCreate(view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.hideKeyboard()
    }

    open fun finish() {
        activityCompat.finish()
    }


}





