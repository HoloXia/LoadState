package com.example.loadstate.data

/**
 *
 *
 * @Author holo
 * @Date 2022/4/11
 */
data class AppException(
    var errorMsg: String, //错误消息
    var errCode: Int = 0, //错误码
    var errorLog: String = "", //错误日志
    var throwable: Throwable? = null
) : RuntimeException(errorMsg) {

    constructor(msg: String, code: Int) : this(msg, code, "", null)

    override fun toString(): String {
        return "AppException(errorMsg='$errorMsg', errCode=$errCode, errorLog='$errorLog', throwable=${throwable?.message})"
    }


}