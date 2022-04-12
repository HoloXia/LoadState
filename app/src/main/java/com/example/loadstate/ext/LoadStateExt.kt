package com.example.loadstate.ext

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.loadstate.R
import com.example.loadstate.appContext
import com.holo.loadstate.LoadService
import com.holo.loadstate.LoadState

/**
 * LoadState扩展函数
 *
 * @Author holo
 * @Date 2022/4/11
 */

/**
 * View加载框架初始化
 * @param view View 被替换的父布局
 * @param shimmer Boolean 是否需要闪烁加载
 * @param callback Function0<Unit> 重试方法
 * @return LoadService
 */
fun loadServiceInit(view: View, shimmer: Boolean = true, callback: () -> Unit): LoadService {
    return loadServiceInit(view, R.layout.layout_loadsir_loading, shimmer, callback)
}

/**
 * View加载框架初始化
 * @param view View 被替换的父布局
 * @param loadLayoutId Int 加载布局id
 * @param shimmer Boolean 是否需要闪烁加载
 * @param callback Function0<Unit> 重试方法
 * @return LoadService
 */
fun loadServiceInit(view: View, @LayoutRes loadLayoutId: Int, shimmer: Boolean = true, callback: () -> Unit): LoadService {
    val loadService = LoadState.register(view) {
        showLoading(shimmer)
        callback.invoke()
    }.config {
        loading(loadLayoutId)
        failed(R.layout.layout_loadsir_failed, R.id.btn_retry)
        empty(R.layout.layout_loadsir_empty)
        configColorBuilder {
            setBaseColor(appContext.getColor(R.color.colorPrimaryVariant))
            setHighlightColor(appContext.getColor(R.color.white))
        }
    }
    loadService.showLoading(shimmer)
    return loadService
}

/**
 * RecyclerView加载框架初始化
 * @param recyclerView RecyclerView 需要代理加载状态的RecyclerView
 * @param adapter Adapter<*> RecyclerView的adapter
 * @param callback Function0<Unit> 重试方法
 * @return LoadService
 */
fun loadServiceInit(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, callback: () -> Unit): LoadService {
    return loadServiceInit(recyclerView, adapter, R.layout.layout_loadsir_loading, callback)
}

/**
 * RecyclerView加载框架初始化
 * @param recyclerView RecyclerView 需要代理加载状态的RecyclerView
 * @param adapter Adapter<*> RecyclerView的adapter
 * @param loadLayoutId Int 加载中布局id（RecyclerView item shimmer）
 * @param callback Function0<Unit> 重试方法
 * @return LoadService
 */
fun loadServiceInit(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, @LayoutRes loadLayoutId: Int, callback: () -> Unit): LoadService {
    val loadService = LoadState.register(recyclerView, adapter) {
        showLoading()
        callback.invoke()
    }.config {
        loading(loadLayoutId)
        failed(R.layout.layout_loadsir_failed, R.id.btn_retry)
        empty(R.layout.layout_loadsir_empty)
        configColorBuilder {
            setBaseColor(appContext.getColor(R.color.bgShimmer))
            setHighlightColor(appContext.getColor(R.color.white))
        }
    }
    loadService.showLoading()
    return loadService
}

/**
 * 设置空布局
 * @receiver LoadService
 * @param emptyTip String? 数据为空的提示语
 * @param icon Int? 数据为空的图标
 */
fun LoadService.showEmpty(emptyTip: String? = null, @DrawableRes icon: Int? = null) {
    this.showEmpty().apply {
        emptyTip?.let {
            findViewById<TextView>(R.id.tv_empty).text = it
        }
        icon?.let {
            findViewById<ImageView>(R.id.iv_empty).setImageResource(it)
        }
    }
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService.showFailed(message: String? = null) {
    this.showFailed().apply {
        message?.let {
            this.findViewById<TextView>(R.id.tv_failed).text = message
        }
    }
}
