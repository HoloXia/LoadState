package com.holo.loadstate

import android.view.View

/**
 *
 *
 * @Author holo
 * @Date 2022/3/9
 */
interface LoadService {

    fun config(config: LoadConfig.() -> Unit): LoadService

    fun showLoading(shimmer: Boolean = true)

    fun showEmpty(): View

    fun showFailed(): View

    fun hide()
}