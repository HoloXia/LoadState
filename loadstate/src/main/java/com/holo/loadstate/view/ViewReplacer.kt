package com.holo.loadstate.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 *
 * @Author holo
 * @Date 2022/3/9
 */
class ViewReplacer(private val sourceView: View) {

    private var mTargetView: View? = null
    private var mTargetViewResID = -1
    private var mCurrentView: View? = sourceView
    private var mSourceParentView: ViewGroup? = null
    private var mSourceViewLayoutParams: ViewGroup.LayoutParams = sourceView.layoutParams
    private var mSourceViewIndexInParent = 0
    private val mSourceViewId: Int = sourceView.id

    companion object {
        private val TAG = ViewReplacer::class.java.name
    }

    fun replace(targetViewResID: Int) {
        if (mTargetViewResID == targetViewResID) {
            return
        }
        if (init()) {
            mTargetViewResID = targetViewResID
            replace(LayoutInflater.from(sourceView.context).inflate(mTargetViewResID, mSourceParentView, false))
        }
    }

    fun replace(targetView: View) {
        if (mCurrentView === targetView) {
            return
        }
        if (targetView.parent != null) {
            (targetView.parent as ViewGroup).removeView(targetView)
        }
        if (init()) {
            mTargetView = targetView
            mSourceParentView!!.removeView(mCurrentView)
            mTargetView!!.id = mSourceViewId
            mSourceParentView!!.addView(mTargetView, mSourceViewIndexInParent, mSourceViewLayoutParams)
            mCurrentView = mTargetView
        }
    }

    fun restore() {
        if (mSourceParentView != null && mCurrentView !== sourceView) {
            mSourceParentView!!.removeView(mCurrentView)
            mSourceParentView!!.addView(sourceView, mSourceViewIndexInParent, mSourceViewLayoutParams)
            mCurrentView = sourceView
            mTargetView = null
            mTargetViewResID = -1
        }
    }

    fun getTargetView(): View? {
        return mTargetView
    }

    fun getCurrentView(): View? {
        return mCurrentView
    }

    private fun init(): Boolean {
        if (mSourceParentView == null) {
            mSourceParentView = sourceView.parent as ViewGroup
            if (mSourceParentView == null) {
                Log.e(TAG, "the source view have not attach to any view")
                return false
            }
            val count = mSourceParentView!!.childCount
            for (index in 0 until count) {
                if (sourceView === mSourceParentView!!.getChildAt(index)) {
                    mSourceViewIndexInParent = index
                    break
                }
            }
        }
        return true
    }
}