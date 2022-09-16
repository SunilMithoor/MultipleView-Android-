package com.app.presentation.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.NavigationRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.app.databinding.ActivityBaseBinding
import com.app.presentation.extension.*
import com.app.presentation.module.Navigator
import com.app.presentation.util.NavigationCommand
import timber.log.Timber
import javax.inject.Inject

/**
 * Base Activity class for the app
 *
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    private val binding by lazy {
        ActivityBaseBinding.inflate(layoutInflater).apply {
            containerView.addView(layout())
        }
    }

    val navController by lazy {
        //Navigation.findNavController(this, R.id.containerHostFragment)
        navHost().navController
    }


    val bundle by lazy {
        intent.extras ?: Bundle()
    }

    /***
     *  Add UI View
     */
    abstract fun layout(): View

    /**
     *  Observe LiveData/Flow
     */
    open fun observeLiveData() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.hideKeyboard()
        observeLiveData()
    }

    override fun onPause() {
        super.onPause()
        binding.root.hideKeyboard()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            navController.navigateUp()
        } else {
            super.onBackPressed()
        }
    }

    fun showLoader() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        binding.progress.visible()
    }

    fun hideLoader() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding.progress.gone()
    }

    fun snackBar(msg: String) {
        binding.root.snackBar(msg)
    }

    fun snackBar(@StringRes resID: Int) {
        binding.root.snackBar(resID)
    }

    /*fun changeGraph(@NavigationRes resId: Int) {
        val graph = navController.navInflater.inflate(resId)
        graph.startDestination = R.id.otpFragment
        navController.graph = graph
    }*/

    fun changeGraph(@NavigationRes resId: Int, block: (NavGraph) -> Unit) {
        val graph = navController.navInflater.inflate(resId)
        block(graph)
        navController.graph = graph
    }

    fun handleNavigation(command: NavigationCommand) {
        when (command) {

            is NavigationCommand.ToDirection -> {
                navController?.navigate(command.directions)
            }
//            is NavigationCommand.ToFailureCallback -> {
//                handleFailure(command.errorType, command.callback)
//            }
//            is NavigationCommand.ToPopup -> {
//                showPopup(command.popupUiModel, command.callbackPositive, command.callbackNegative)
//            }
//            is NavigationCommand.ToDirectionActivity -> {
//                navigator.startActivity(this, command.className, command.flags, command.isClearTask)
//            }

            is NavigationCommand.ToDeepLink -> {

            }
            is NavigationCommand.LoadingDialog -> {
                Timber.d("Loading Trigger : ${command.isShow}")

            }
            is NavigationCommand.Back -> {
                navController?.let {
                    if (!it.popBackStack()) {
                        finish()
                    }
                }
            }
        }
    }

}



fun AppCompatActivity.showLoader() {
    castAs<BaseAppCompatActivity> {
        showLoader()
    }
}

fun AppCompatActivity.hideLoader() {
    castAs<BaseAppCompatActivity> {
        hideLoader()
    }
}

fun AppCompatActivity.snackBar(msg: String) {
    castAs<BaseAppCompatActivity> {
        snackBar(msg)
    }
}

fun AppCompatActivity.snackBar(@StringRes resID: Int) {
    castAs<BaseAppCompatActivity> {
        snackBar(resID)
    }
}

fun AppCompatActivity.childFragments() =
    (supportFragmentManager.fragments.first() as? NavHostFragment)?.childFragmentManager?.fragments

fun AppCompatActivity.navHost() = supportFragmentManager.fragments.first() as NavHostFragment