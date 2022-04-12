package com.example.loadstate.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loadstate.data.RvLoadBean
import com.example.loadstate.databinding.ItemRvLoadBinding

/**
 *
 *
 * @Author holo
 * @Date 2022/4/11
 */
class RvLoadAdapter(private val onItemClick: (bean: RvLoadBean) -> Unit) : RecyclerView.Adapter<RvLoadAdapter.LoadViewHolder>() {

    var dataList = mutableListOf<RvLoadBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadViewHolder {
        val binding = ItemRvLoadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.ivAvatar.setImageResource(item.faceUrl)
        holder.binding.tvIntroduction.text = item.introduction
        holder.binding.tvFrom.text = item.from

        holder.binding.itemRoot.setOnClickListener { onItemClick.invoke(item) }
    }

    override fun getItemCount(): Int = dataList.size

    inner class LoadViewHolder(val binding: ItemRvLoadBinding) : RecyclerView.ViewHolder(binding.root)
}