package com.holo.loadstate.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.holo.loadstate.LoadConfig

/**
 *
 *
 * @Author holo
 * @Date 2022/3/10
 */
class ShimmerAdapter(val config: LoadConfig, val layoutReference: Int, val shimmer: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (shimmer) {
            ShimmerViewHolder(inflater, parent, layoutReference)
        } else {
            object : RecyclerView.ViewHolder(inflater.inflate(layoutReference, parent, false)) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (shimmer) {
            val layout: ShimmerFrameLayout = holder.itemView as ShimmerFrameLayout
            layout.setShimmer(config.mShimmer)
        }
    }

    override fun getItemCount(): Int {
        return config.mItemCount
    }

}