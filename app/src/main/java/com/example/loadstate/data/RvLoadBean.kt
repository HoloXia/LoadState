package com.example.loadstate.data

import android.os.Parcelable
import com.example.loadstate.R
import kotlinx.parcelize.Parcelize

/**
 *
 *
 * @Author holo
 * @Date 2022/4/11
 */
@Parcelize
data class RvLoadBean(
    var faceUrl: Int = R.mipmap.ic_shogun,
    var name: String = "",
    var introduction: String = "",
    var from: String = "",
    var details: String = ""
) : Parcelable