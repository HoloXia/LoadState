package com.holo.loadstate.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.holo.loadstate.LoadConfig
import com.holo.loadstate.LoadService
import com.holo.loadstate.R

/**
 * View 骨架-闪烁加载，带错误状态
 *
 * @Author holo
 * @Date 2022/3/9
 */
class ViewLoadShimmer(
    private val actualView: View,
    private val retryAction: LoadService.() -> Unit
) : LoadService {

    companion object {
        private val TAG = ViewLoadShimmer::class.java.name
    }

    private val mConfig: LoadConfig = LoadConfig()

    private val mViewReplacer: ViewReplacer = ViewReplacer(actualView)
    private var mSkeletonResID: Int = mConfig.mLoadingLayoutId

    override fun config(config: LoadConfig.() -> Unit): LoadService {
        config.invoke(mConfig)
        return this
    }

    /**
     * 用于显示加载闪烁骨骼布局
     * @param shimmer Boolean
     */
    override fun showLoading(shimmer: Boolean) {
        //看是否已经替换过，有则先替换回原本布局
        hide()
        this.mSkeletonResID = mConfig.mLoadingLayoutId
        val skeletonLoadingView = generateSkeletonView(shimmer)
        mViewReplacer.replace(skeletonLoadingView)
    }

    /**
     * 显示空布局
     * @return View
     */
    override fun showEmpty(): View {
        hide()
        this.mSkeletonResID = mConfig.mEmptyLayoutId
        val skeletonView = generateSkeletonView()
        mViewReplacer.replace(skeletonView)
        return skeletonView
    }

    /**
     * 显示错误/失败布局
     * @return View
     */
    override fun showFailed(): View {
        hide()
        this.mSkeletonResID = mConfig.mFailedLayoutId
        val skeletonView = generateSkeletonView()
        mConfig.mRetryViewId?.also {
            skeletonView.findViewById<View>(it).setOnClickListener { retryAction.invoke(this) }
        } ?: skeletonView.setOnClickListener { retryAction.invoke(this) }
        mViewReplacer.replace(skeletonView)
        return skeletonView
    }

    override fun hide() {
        if (mViewReplacer.getTargetView() is ShimmerFrameLayout) {
            (mViewReplacer.getTargetView() as ShimmerFrameLayout).stopShimmer()
        }
        mViewReplacer.restore()
    }

    /**
     * 生成ShimmerLayout布局
     * @param parentView ViewGroup 父布局
     * @return ShimmerFrameLayout
     */
    private fun generateShimmerContainerLayout(parentView: ViewGroup): ShimmerFrameLayout {
        val shimmerLayout: ShimmerFrameLayout =
            LayoutInflater.from(actualView.context).inflate(R.layout.layout_shimmer, parentView, false) as ShimmerFrameLayout
        shimmerLayout.setShimmer(mConfig.mShimmer)
        val innerView = LayoutInflater.from(actualView.context).inflate(mSkeletonResID, shimmerLayout, false)
        val lp = innerView.layoutParams
        if (lp != null) {
            shimmerLayout.layoutParams = lp
        }
        shimmerLayout.addView(innerView)
        shimmerLayout.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                shimmerLayout.startShimmer()
            }

            override fun onViewDetachedFromWindow(v: View) {
                shimmerLayout.stopShimmer()
            }
        })
        shimmerLayout.startShimmer()
        return shimmerLayout
    }

    private fun generateSkeletonView(shimmer: Boolean = false): View {
        val viewParent = actualView.parent
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view")
            throw RuntimeException("LoadShimmer ----> the source view have not attach to any view")
        }
        val parentView = viewParent as ViewGroup
        return if (shimmer) {
            generateShimmerContainerLayout(parentView)
        } else LayoutInflater.from(actualView.context).inflate(mSkeletonResID, parentView, false)
    }

}