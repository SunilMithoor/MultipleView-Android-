package com.app.presentation.ui.dashboard

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import com.app.databinding.ActivityMainBinding
import com.app.domain.entity.response.Datas
import com.app.domain.entity.response.MultipleViewData
import com.app.domain.model.ViewState
import com.app.mediapicker.MediaItem
import com.app.mediapicker.MediaOptions
import com.app.mediapicker.activities.MediaPickerActivity.Companion.getMediaItemSelected
import com.app.mediapicker.activities.MediaPickerActivity.Companion.open
import com.app.presentation.base.BaseAppCompatActivity
import com.app.presentation.extension.*
import com.app.presentation.ui.Constants.IMAGE
import com.app.presentation.ui.Constants.VIDEO
import com.app.presentation.ui.RequestCode.REQUEST_EXTRA_STORAGE_PERMISSION
import com.app.presentation.ui.RequestCode.REQUEST_IMAGE
import com.app.presentation.ui.RequestCode.REQUEST_VIDEO
import com.app.presentation.ui.adapter.MultipleViewAdapter
import com.app.presentation.vm.MultipleViewViewModel
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.io.File
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseAppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<MultipleViewViewModel>()
    private var fileTypeGlobal = ""
    private var adapterPos = -1
    private var adapterData: MultipleViewData? = null

    private val adapter by lazy {
        MultipleViewAdapter()
    }

    override fun layout() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBackToolbar(binding.toolBar)
        binding.recyclerView.adapter = adapter
        init()
    }


    private fun init() {
        adapter.iconClick { id, pos, data ->
            Timber.d("adapter data:: $id,$pos,$data")
            when (id) {
                Id.imgImageAdd -> {
                    fileTypeGlobal = IMAGE
                    adapterPos = pos
                    adapterData = data
                    askPermission(IMAGE)
                }
                Id.imgVideoAdd -> {
                    fileTypeGlobal = VIDEO
                    adapterPos = pos
                    adapterData = data
                    askPermission(VIDEO)
                }
            }
        }
        getMultipleViewData()
        binding.btnSubmit.setOnClickListener {
            viewModel.getAnswerData()
        }
    }

    private fun getMultipleViewData() {
        showLoader()
        viewModel.fetchMultipleViewData()
    }

    override fun observeLiveData() {

        viewModel.observeMultipleViewDataLive.observe(this) { viewState ->
            Timber.d("Observer viewState:::$viewState")
            when (viewState) {
                is ViewState.Loading -> {
                    Timber.d("Observer Loading")
                }
                is ViewState.RenderFailure -> {
                    Timber.d("Observer Failure ")
                    hideLoader()
                    binding.constraintLayout.snackBar(viewState.throwable.message)
                }
                is ViewState.RenderSuccess -> {
                    hideLoader()
                    Timber.d("Observer Success :: ${viewState.output}")
                    Timber.d("Observer Success :: ${viewState.output.data}")
                    setData(viewState.output)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        }


        viewModel.observeMultipleViewFinalDataLive.observe(this) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    Timber.d("Observer Loading")
                }
                is ViewState.RenderFailure -> {
                    Timber.d("Observer Failure ")
                    hideLoader()
                    binding.constraintLayout.snackBar(viewState.throwable.message)
                }
                is ViewState.RenderSuccess -> {
                    hideLoader()
                    Timber.d("Observer Success :: ${viewState.output}")
                    setFinalData(viewState.output)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        }

    }

    private fun setData(datas: Datas) {
        if (datas != null) {
            val lists = datas.data
            adapter.submitList(lists)
        }
    }

    private fun updateData(
        pos: Int?,
        multipleViewData: MultipleViewData?,
        mediaItemList: List<MediaItem>?
    ) {
        val lists = adapter.currentList
        val pathData: MutableList<String> = ArrayList()
        val uriData: MutableList<Uri> = ArrayList()
        if (fileTypeGlobal == IMAGE) {
            for (i in mediaItemList!!.indices) {
//                pathData.add(mediaItemList[i].getPathCropped(this)!!)
                uriData.add(mediaItemList[i].uriCropped!!)
            }
            if (multipleViewData?.uriPaths != null) {
                for (j in multipleViewData.uriPaths?.indices!!) {
                    uriData.add(multipleViewData.uriPaths!![j])
                }
            }
        } else if (fileTypeGlobal == VIDEO) {
            for (i in mediaItemList!!.indices) {
//                pathData.add(mediaItemList[i].getPathOrigin(this)!!)
                uriData.add(mediaItemList[i].uriOrigin!!)
            }
            if (multipleViewData?.uriPaths != null) {
                for (j in multipleViewData.uriPaths?.indices!!) {
                    uriData.add(multipleViewData.uriPaths!![j])
                }
            }
        }

        lists[pos!!].answerChoices = pathData
        lists[pos].uriPaths = uriData
        adapter.submitList(lists)
        adapter.notifyItemChanged(pos)
    }

    private fun setFinalData(datas: Datas) {
        if (datas != null) {
            Timber.d("Final Data:: $datas")
            //call api to submit data
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun askPermission(type: String) {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    resources.getString(AppString.permission_message),
                    resources.getString(AppString.ok),
                    resources.getString(AppString.cancel)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    resources.getString(AppString.allow_permission_message),
                    resources.getString(AppString.ok),
                    resources.getString(AppString.cancel)
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    if (type == IMAGE) {
                        startImageActivity()
                    } else if (type == VIDEO) {
                        startVideoActivity()
                    }
                } else {
                    Toast.makeText(
                        this,
                        String.format(
                            "%s %s",
                            resources.getString(AppString.permission_denied),
                            deniedList
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun openPhotoMedia() {
        val builder = MediaOptions.Builder()
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Random().nextLong().toString() + ".jpg"
        )
        val options: MediaOptions = builder.setIsCropped(true)
            .setFixAspectRatio(true).setCroppedFile(file).build()
        open(this, REQUEST_IMAGE, options)
    }

    private fun openVideoMedia() {
        val builder = MediaOptions.Builder()
        val options: MediaOptions = builder.selectVideo()
            .canSelectMultiVideo(false).build()
        open(this, REQUEST_VIDEO, options)
    }

    private fun startImageActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                openPhotoMedia()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_EXTRA_STORAGE_PERMISSION)
            }
        } else {
            openPhotoMedia()
        }
    }

    private fun startVideoActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                openVideoMedia()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_EXTRA_STORAGE_PERMISSION)
            }
        } else {
            openVideoMedia()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                var mMediaSelectedList: List<MediaItem>? = null
                mMediaSelectedList = getMediaItemSelected(data)
                if (mMediaSelectedList != null && mMediaSelectedList.isNotEmpty()) {
//                    for (mediaItem in mMediaSelectedList) {
//                        Timber.d("mediaItem:: ${mediaItem.getPathCropped(this)}")
//                        Timber.d("mediaItem:: ${mediaItem.getPathOrigin(this)}")
//                        updateData(mediaItem.uriCropped,mediaItem.getPathCropped(this))
//                    }

                    updateData(adapterPos, adapterData, mMediaSelectedList)

                } else {
                    Timber.d("Error to get media, NULL")
                }
            }
        } else if (requestCode == REQUEST_VIDEO) {
            if (resultCode == RESULT_OK) {
                var mMediaSelectedList: List<MediaItem>? = null
                mMediaSelectedList = getMediaItemSelected(data)
                if (mMediaSelectedList != null && mMediaSelectedList.isNotEmpty()) {
//                    for (mediaItem in mMediaSelectedList) {
//                        Timber.d("mediaItem:: ${mediaItem.getPathCropped(this)}")
//                        Timber.d("mediaItem :: ${mediaItem.getPathOrigin(this)}")
//                    }
                    updateData(adapterPos, adapterData, mMediaSelectedList)
                } else {
                    Timber.d("Error to get media, NULL")
                }

            }
        } else if (requestCode == REQUEST_EXTRA_STORAGE_PERMISSION) {
            if (resultCode == RESULT_OK) {
                if (fileTypeGlobal == IMAGE) {
                    startImageActivity()
                } else if (fileTypeGlobal == VIDEO) {
                    startVideoActivity()
                }
            }
        }
    }
}