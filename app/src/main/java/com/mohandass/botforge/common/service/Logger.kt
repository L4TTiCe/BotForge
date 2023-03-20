package com.mohandass.botforge.common.service

interface Logger {
    fun log(tag: String, message: String)
    fun logVerbose(tag: String, message: String)
    fun logError(tag: String, message: String)
    fun logError(tag:String, message: String, throwable: Throwable)
}
