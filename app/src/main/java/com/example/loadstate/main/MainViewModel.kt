package com.example.loadstate.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadstate.R
import com.example.loadstate.appContext
import com.example.loadstate.data.AppException
import com.example.loadstate.data.RvLoadBean
import com.example.loadstate.data.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 *
 * @Author holo
 * @Date 2022/4/11
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _dataListResp = MutableSharedFlow<State<MutableList<RvLoadBean>>>()
    val dataListResp = _dataListResp.asSharedFlow()

    private var job: Job? = null

    //1.Success 2.Empty 3.Failed
    private var reqRes: Int = 1

    fun getDataList() {
        job?.cancel()
        job = viewModelScope.launch {
            flow<State<MutableList<RvLoadBean>>> {
                delay(1300)
                when (reqRes) {
                    1 -> { //Success
                        val list = mutableListOf(
                            RvLoadBean(
                                R.mipmap.ic_wendi,
                                appContext.getString(R.string.str_wd_name),
                                appContext.getString(R.string.str_wd_intro),
                                appContext.getString(R.string.str_wd_from),
                                appContext.getString(R.string.str_wd_details)
                            ),
                            RvLoadBean(
                                R.mipmap.ic_zhongli,
                                appContext.getString(R.string.str_zl_name),
                                appContext.getString(R.string.str_zl_intro),
                                appContext.getString(R.string.str_zl_from),
                                appContext.getString(R.string.str_zl_details)
                            ),
                            RvLoadBean(
                                R.mipmap.ic_shogun,
                                appContext.getString(R.string.str_ls_name),
                                appContext.getString(R.string.str_ls_intro),
                                appContext.getString(R.string.str_ls_from),
                                appContext.getString(R.string.str_ls_details)
                            )
                        )
                        list.addAll(list)
                        list.addAll(list)
                        list.addAll(list)
                        emit(State.success(list))
                    }
                    2 -> { //Empty
                        emit(State.success(mutableListOf()))
                    }
                    else -> { //Failed
                        val random = (0..2).random()
                        emit(State.error(AppException(appContext.resources.getStringArray(R.array.request_failed)[random], random)))
                    }
                }
            }
                .flowOn(Dispatchers.IO)
                .collect {
                    if (reqRes >= 3) reqRes = 1 else reqRes++
                    _dataListResp.emit(it)
                }
        }
    }
}