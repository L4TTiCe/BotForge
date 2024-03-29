// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common

import com.google.gson.Gson
import com.mohandass.botforge.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * A class that contains utility functions
 */
class Utils {
    companion object {
        fun validateEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun getAppVersion(): String {
            return BuildConfig.VERSION_NAME
        }

        fun getAppVersionCode(): Int {
            return BuildConfig.VERSION_CODE
        }

        // Parses the stack trace of a throwable to get the error message from OpenAI API
        fun parseStackTraceForErrorMessage(throwable: Throwable): Throwable {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))

            val stackTrace = stringWriter.toString()
            for (line in stackTrace.split("\n")) {
                if (line.contains(INTERRUPT_ERROR_MESSAGE)) {
                    return Throwable(INTERRUPTED_ERROR_MESSAGE)
                }
                if (line.contains("message")) {
                    val messageAsJson = "{${line.substring(0, line.length - 1)}}"
                    val map = Gson().fromJson(messageAsJson, Map::class.java)

                    val message = map["message"] as String

                    return if (message.contains("Invalid API key")) {
                        Throwable(INVALID_API_KEY_ERROR_MESSAGE)
                    } else {
                        Throwable(message)
                    }
                }
            }

            return Throwable("")
        }

        // Checks if a string contains markdown
        fun containsMarkdown(string: String): Boolean {
            return markdownRegex.containsMatchIn(string)
        }

        fun formatTimeLong(time: Long = System.currentTimeMillis()): String {
            val format = SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a z")
            return format.format(time)
        }

        // Randomly generates an emoji
        fun randomEmojiUnicode(): String {
            // A list of ranges of code points for emoji blocks
            val emojiRanges = listOf(
                0x1F600..0x1F64F, // Emoticons
                0x1F300..0x1F5FF, // Miscellaneous Symbols and Pictographs
                0x1F680..0x1F6FF, // Transport and Map Symbols
                0x2600..0x26FF,   // Miscellaneous Symbols
                0x2700..0x27BF,   // Dingbats
                0xFE00..0xFE0F,   // Variation Selectors
                0x1F900..0x1F9FF, // Supplemental Symbols and Pictographs
                // 0x1F1E6..0x1F1FF  // Flags
            )
            val random = java.util.Random()
            val range = emojiRanges[random.nextInt(emojiRanges.size)]

            val codePoint = random.nextInt(range.last - range.first + 1) + range.first

            return Character.toChars(codePoint).joinToString("")
        }

        // Sanitizes a search query for the search API
        fun sanitizeSearchQuery(query: String?): String {
            if (query == null) {
                return ""
            }
            val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
            return "*$queryWithEscapedQuotes*"
        }

        fun formatDuration(time: Long): String {
            val localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(time),
                ZoneId.systemDefault()
            )

            val formatter = DateTimeFormatter.ofPattern(
                "s",
                Locale.getDefault()
            )

            return localDateTime.format(formatter)
        }

        private const val TAG = "Utils"
        private const val INTERRUPT_ERROR_MESSAGE =
            "com.mohandass.botforge.chat.ui.viewmodel.ChatViewModel.interruptRequest"
        const val INVALID_API_KEY_ERROR_MESSAGE = "invalid_api_key"
        const val INTERRUPTED_ERROR_MESSAGE = "interrupted"
        private val markdownRegex = Regex(
            """(^#+\s.+|[*_].+?[*_]|!\[.+?\]\(.+?\)|\[.+?\]\(.+?\)|(```|~~~).+?(```|~~~)|`[^`]+`|^\s{4}.+|^\s*\*|\d+\.)|^\s*\|(.+\|)+\s*$""",
            RegexOption.MULTILINE
        )
    }
}