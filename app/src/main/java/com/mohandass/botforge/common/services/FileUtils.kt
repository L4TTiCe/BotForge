// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ExportedChat
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.common.Utils
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * This class contains methods to export chat data to a file
 */
class FileUtils {

    companion object {
        private const val TAG = "FileManagementService"

        // Share a URI with other apps on the device
        private fun shareUri(
            context: Context,
            uri: Uri,
            type: String,
            title: String,
            chooserTitle: String = "Share File"
        ) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = type
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val chooser = Intent.createChooser(shareIntent, chooserTitle)

            // Reference:
            // https://stackoverflow.com/questions/57689792/permission-denial-while-sharing-file-with-fileprovider
            // queryIntentActivities() returns a list of apps that can handle the intent
            // We need to grant permission to all of them
            //
            // context.packageManager.queryIntentActivities(intent, Int) is deprecated in API 33
            val resInfoList: List<ResolveInfo> =
                if (
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
                ) {
                    context.packageManager.queryIntentActivities(
                        chooser,
                        PackageManager.ResolveInfoFlags.of(
                            PackageManager.MATCH_DEFAULT_ONLY.toLong()
                        )
                    )
                } else {
                    // Deprecated in API 33
                    context.packageManager.queryIntentActivities(
                        chooser,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                }

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            context.startActivity(chooser)
        }

        fun exportBitmapAsPng(
            title: String = "Image",
            bitmap: Bitmap,
            context: Context
        ) {
            val cacheDir = context.cacheDir

            val pngFile =
                File(cacheDir, "${title}_${System.currentTimeMillis()}.png")
            val pngUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pngFile
            )

            val fileOutputStream = FileOutputStream(pngFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()

            shareUri(context, pngUri, "image/png", title)
        }

        // Export a chat as a PDF file
        // Constructs a PDF file using the PdfWriter class
        fun exportChatAsPdf(
            title: String = "ChatExport",
            chatInfo: ExportedChat,
            context: Context
        ) {
            val pdfDocument = PdfDocument()

            val textSize = 14f
            val headingSize = 18f

            val margin = 72f
            val padding = 20f

            val pageWidth = 595
            val pageHeight = 842

            val messagePaint = TextPaint()
            messagePaint.textSize = textSize
            messagePaint.isAntiAlias = true

            val headingPaint = TextPaint()
            headingPaint.textSize = headingSize
            headingPaint.isFakeBoldText = true


            val rolePaint = TextPaint()
            rolePaint.isFakeBoldText = true

            val pdfWriter = PdfWriter(
                pdfDocument = pdfDocument,
                margin = margin,
                pageWidth = pageWidth,
                pageHeight = pageHeight,
            )

            if (chatInfo.chatInfo != null) {
                pdfWriter.write("Chat Details", headingPaint)

                pdfWriter.write("Title: ${chatInfo.chatInfo.name}", messagePaint)
                pdfWriter.write(
                    "Saved at: ${Utils.formatTimeLong(chatInfo.chatInfo.savedAt)}",
                    messagePaint
                )
                pdfWriter.write("Messages Count: ${chatInfo.messages.size}", messagePaint)

                pdfWriter.addPadding(16f)
            }

            pdfWriter.write("Message Log", headingPaint)

            pdfWriter.addPadding(padding)

            for (message in chatInfo.messages) {
                rolePaint.color = when (message.role) {
                    Role.USER -> context.getColor(R.color.primary)
                    Role.BOT -> context.getColor(R.color.tertiary)
                    Role.SYSTEM -> context.getColor(R.color.secondary)
                    else -> {
                        context.getColor(R.color.primary)
                    }
                }
                pdfWriter.write(message.role.toString(), rolePaint)

                // split message into multiple parts if it has newlines
                val messageParts = message.text.split("\n\n")
                for (part in messageParts) {
                    pdfWriter.writeMarkdown(part, messagePaint, context)
                    pdfWriter.addPadding(padding / 2)
                }

                pdfWriter.addPadding(padding)
            }

            pdfWriter.finish()

            val filesDir = context.filesDir
            val chatDir = File(filesDir, "exported_chats")
            if (!chatDir.exists()) {
                val created = chatDir.mkdir()
                if (!created) {
                    SnackbarManager.showMessage(R.string.export_dir_create_failed)
                }
            }

            val pdfFile =
                File(filesDir, "exported_chats/${title}_${System.currentTimeMillis()}.pdf")
            val pdfUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()

            shareUri(context, pdfUri, "application/pdf", title)
        }

        // Export a chat as a JSON file
        // Constructs a JSON string using the Gson library
        fun exportChatAsJson(
            title: String = "ChatExport",
            jsonString: String,
            context: Context
        ) {
            val filesDir = context.filesDir
            // move directory to a chat folder
            val chatDir = File(filesDir, "exported_chats")
            if (!chatDir.exists()) {
                val created = chatDir.mkdir()
                if (!created) {
                    SnackbarManager.showMessage(R.string.export_dir_create_failed)
                }
            }

            val jsonFile =
                File(filesDir, "exported_chats/${title}_${System.currentTimeMillis()}.json")
            val jsonUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                jsonFile
            )

            // A writer to write the JSON string to the file
            val writer = FileWriter(jsonFile)
            writer.write(jsonString)
            writer.close()

            shareUri(context, jsonUri, "application/json", title)
        }
    }
}
