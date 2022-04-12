package com.holo.loadstate.rv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.holo.loadstate.LoadConfig
import com.holo.loadstate.LoadService
import com.holo.loadstate.view.ViewReplacer

/**
 * RecyclerView 骨架-闪烁加载，带错误状态
 *
 * @Author holo
 * @Date 2022/3/10
 */
class RVLoadShimmer(
    private var recyclerView: RecyclerView,
    private var actualAdapter: RecyclerView.Adapter<*>,
    private val retryAction: LoadService.() -> Unit
) : LoadService {

    companion object {
        private val TAG = RVLoadShimmer::class.java.name
    }

    private val mConfig: LoadConfig = LoadConfig()

    private val mViewReplacer: ViewReplacer = ViewReplacer(recyclerView)

    private var mSkeletonAdapter: ShimmerAdapter = ShimmerAdapter(mConfig, mConfig.mLoadingLayoutId)

    override fun config(config: LoadConfig.() -> Unit): LoadService {
        config.invoke(mConfig)
        return this
    }

    override fun showLoading(shimmer: Boolean) {
        mViewReplacer.restore()
        mSkeletonAdapter = ShimmerAdapter(mConfig, mConfig.mLoadingLayoutId, shimmer)
        recyclerView.adapter = mSkeletonAdapter
        if (!recyclerView.isComputingLayout && mConfig.mFrozen) {
            recyclerView.suppressLayout(true)
        }
    }

    override fun showEmpty(): View {
        mViewReplacer.restore()
        val skeletonView = generateSkeletonView(mConfig.mEmptyLayoutId)
        mViewReplacer.replace(skeletonView)
        return skeletonView
    }

    override fun showFailed(): View {
        mViewReplacer.restore()
        val skeletonView = generateSkeletonView(mConfig.mFailedLayoutId)
        mConfig.mRetryViewId?.also {
            skeletonView.findViewById<View>(it).setOnClickListener { retryAction.invoke(this) }
        } ?: skeletonView.setOnClickListener { retryAction.invoke(this) }
        mViewReplacer.replace(skeletonView)
        return skeletonView
    }

    override fun hide() {
        mViewReplacer.restore()
        recyclerView.adapter = actualAdapter
    }

    private fun generateSkeletonView(@LayoutRes skeletonResID: Int): View {
        val viewParent = recyclerView.parent
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view")
            throw RuntimeException("LoadShimmer ----> the source view have not attach to any view")
        }
        val parentView = viewParent as ViewGroup
        return LayoutInflater.from(recyclerView.context).inflate(skeletonResID, parentView, false)
    }

}