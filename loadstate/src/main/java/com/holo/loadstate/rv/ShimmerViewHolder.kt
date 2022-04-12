package com.holo.loadstate.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.holo.loadstate.R

/**
 * 闪烁加载的[RecyclerView.ViewHolder]
 *
 * @Author holo
 * @Date 2022/3/10
 */
class ShimmerViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    innerViewResId: Int
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_shimmer, parent, false)) {

    init {
        val layout = itemView as ViewGroup
        layout.layoutParams = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val view: View = inflater.inflate(innerViewResId, layout, false)
        /*val lp = view.layoutParams
        if (lp != null) {
            layout.layoutParams = lp
        }*/
        layout.addView(view)
    }
}