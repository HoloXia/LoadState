package com.holo.loadstate

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.facebook.shimmer.Shimmer

/**
 *
 *
 * @Author holo
 * @Date 2022/3/9
 */
class LoadConfig {
    internal var mShimmer: Shimmer = Shimmer.ColorHighlightBuilder().build()

    private var mColorBuilder = Shimmer.ColorHighlightBuilder()
    private var mAlphaBuilder = Shimmer.AlphaHighlightBuilder()

    internal var mLoadingLayoutId: Int = R.layout.layout_base_loading
    internal var mEmptyLayoutId: Int = R.layout.layout_base_empty
    internal var mFailedLayoutId: Int = R.layout.layout_base_failed
    internal var mRetryViewId: Int? = null

    /**
     * RecyclerView使用，默认闪烁加载item数量5
     */
    internal var mItemCount: Int = 5

    /**
     * RecyclerView使用，骨架显示期间是否冻结RecyclerView
     */
    internal var mFrozen: Boolean = true

    /**
     * 加载中布局
     * @param loadingLayoutId Int 布局id
     */
    fun loading(@LayoutRes loadingLayoutId: Int) {
        mLoadingLayoutId = loadingLayoutId
    }

    /**
     * 待替换数据加载失败的布局
     * @param failedLayoutId Int 布局id
     * @param retryView Int? 重试View id
     */
    fun failed(@LayoutRes failedLayoutId: Int, @IdRes retryView: Int? = null) {
        mFailedLayoutId = failedLayoutId
        mRetryViewId = retryView
    }

    /**
     * 待替换数据为空的布局
     * @param emptyLayoutId Int 布局id
     */
    fun empty(@LayoutRes emptyLayoutId: Int) {
        mEmptyLayoutId = emptyLayoutId
    }

    /**
     * 自定义闪烁配置
     * @param shimmer Shimmer 闪烁配置
     */
    fun shimmerConfig(shimmer: Shimmer) {
        mShimmer = shimmer
    }

    /**
     * 设置颜色闪烁配置
     */
    fun configColorBuilder(config: Shimmer.ColorHighlightBuilder.() -> Unit) {
        config.invoke(mColorBuilder)
        mShimmer = mColorBuilder.build()
    }

    /**
     * 设置透明度闪烁配置
     * @param config [@kotlin.ExtensionFunctionType] Function1<AlphaHighlightBuilder, Unit>
     */
    fun configAlphaBuilder(config: Shimmer.AlphaHighlightBuilder.() -> Unit) {
        config.invoke(mAlphaBuilder)
        mShimmer = mAlphaBuilder.build()
    }

    /**
     * RecyclerView使用，默认闪烁加载item数量10
     * @param itemCount Int 显示的item数量
     */
    fun setItemCount(itemCount: Int) {
        mItemCount = itemCount
    }

    /**
     * RecyclerView使用，骨架显示期间是否冻结RecyclerView
     * @param frozen Boolean 是否冻结RecyclerView
     */
    fun setFrozen(frozen: Boolean) {
        mFrozen = frozen
    }
}