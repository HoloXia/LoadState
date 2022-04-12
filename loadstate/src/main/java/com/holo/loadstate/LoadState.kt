package com.holo.loadstate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.holo.loadstate.rv.RVLoadShimmer
import com.holo.loadstate.view.ViewLoadShimmer

/**
 *
 *
 * @Author holo
 * @Date 2022/3/9
 */
object LoadState {

    /**
     * 注册加载服务
     * 注意：Fragment.onViewCreated中初始化时[view]不可传入父布局id！！！
     *
     * @param view View 待替换布局
     * @param retryAction Function0<Unit> 错误重试回调
     * @return LoadService
     */
    fun register(view: View, retryAction: LoadService.() -> Unit): LoadService {
        return ViewLoadShimmer(view, retryAction)
    }

    /**
     * RecyclerView注册加载服务
     * @param view RecyclerView 被注册的RecyclerView
     * @param adapter Adapter<*> RecyclerView的原始Adapter
     * @param retryAction Function0<Unit> 错误重试回调
     * @return LoadService
     */
    fun register(view: RecyclerView, adapter: RecyclerView.Adapter<*>, retryAction: LoadService.() -> Unit): LoadService {
        return RVLoadShimmer(view, adapter, retryAction)
    }
}