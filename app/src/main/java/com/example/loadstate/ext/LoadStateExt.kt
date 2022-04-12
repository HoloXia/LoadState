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
fun loadServiceInit(view: View, @LayoutRes loadLayoutId: Int, shimmer: Boolean = true, retryAction: () -> Unit): LoadService {
    val loadService = LoadState.register(view) {//View注册加载库
        showLoading(shimmer)
        retryAction.invoke()
    }.config {//配置加载页面
        loading(loadLayoutId)
        failed(R.layout.layout_loadsir_failed, R.id.btn_retry)
        empty(R.layout.layout_loadsir_empty)
        configColorBuilder {//Shimmer-android相关配置
            setBaseColor(appContext.getColor(R.color.colorPrimaryVariant))
            setHighlightColor(appContext.getColor(R.color.white))
        }
    }
    //默认进入页面直接显示加载状态
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
    val loadService = LoadState.register(recyclerView, adapter) {//RecyclerView注册加载库，需传入对应的Adapter
        showLoading()
        callback.invoke()
    }.config {//配置加载页面
        loading(loadLayoutId)
        failed(R.layout.layout_loadsir_failed, R.id.btn_retry)
        empty(R.layout.layout_loadsir_empty)
        configColorBuilder {//Shimmer-android相关配置
            setBaseColor(appContext.getColor(R.color.bgShimmer))
            setHighlightColor(appContext.getColor(R.color.white))
        }
    }
    //默认进入页面直接显示加载状态
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
        //showEmpty()会返回空布局View，获取相关元素设置内容即可
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
        //showFailed()会返回空布局View，获取相关元素设置内容即可
        message?.let {
            this.findViewById<TextView>(R.id.tv_failed).text = message
        }
    }
}
