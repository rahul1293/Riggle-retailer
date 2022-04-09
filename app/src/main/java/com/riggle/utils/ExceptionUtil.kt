package com.riggle.utils

import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter

object ExceptionUtil {
    private fun convertStackTraceToString(throwable: Throwable): String {
        try {
            StringWriter().use { sw ->
                PrintWriter(sw).use { pw ->
                    throwable.printStackTrace(pw)
                    return sw.toString()
                }
            }
        } catch (ioe: IOException) {
            throw IllegalStateException(ioe)
        }
    }

    @JvmStatic
    fun convertStackTraceToString(stackTrace: Array<StackTraceElement?>): String {
        val sw = StringWriter()
        printStackTrace(stackTrace, PrintWriter(sw))
        return sw.toString()
    }

    fun printStackTrace(stackTrace: Array<StackTraceElement?>, pw: PrintWriter) {
        for (stackTraceEl in stackTrace) {
            pw.println(stackTraceEl)
        }
    }
}