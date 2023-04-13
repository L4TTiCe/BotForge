// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.mohandass.botforge.R
import com.mohandass.botforge.chat.model.ExportedChat
import com.mohandass.botforge.chat.model.Role
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tables.TableTheme
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat


class PdfWriter(
    val pdfDocument: PdfDocument,
    val margin: Float,
    val pageWidth: Int,
    val pageHeight: Int,
) {
    private val TAG = "PdfConfig"
    private var pageNumber: Int = 1
    private var pageInfo: PdfDocument.PageInfo =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    private var currentPage: PdfDocument.Page
    private var canvas: Canvas
    private var maxWidth: Float
    private var yAxis: Float = margin

    init {
        currentPage = pdfDocument.startPage(pageInfo)
        canvas = currentPage.canvas

        maxWidth = pageWidth - (2 * margin)

        renderHeader()
    }

    private fun renderHeader() {
        val paint = TextPaint()
        paint.isFakeBoldText = true
        paint.color = Color.BLACK
        paint.alpha = 200
        paint.textSize = 14f

        var text = "Exported Chat"
        write(text, paint)

        val format = SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a z")
        text = "Time: ${format.format(System.currentTimeMillis())}"
        write(text, paint)

        addPadding(10f)
    }

    private fun startNextPage() {
        pdfDocument.finishPage(currentPage)

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber + 1).create()
        currentPage = pdfDocument.startPage(pageInfo)
        canvas = currentPage.canvas

        pageNumber += 1
        yAxis = margin
    }

    fun write(text: String, paint: TextPaint) {
        val staticLayout: StaticLayout = StaticLayout.Builder.obtain(
            text,
            0,
            text.length,
            paint,
            maxWidth.toInt()
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .build()

        if (yAxis + staticLayout.height > pageHeight) {
            startNextPage()
        }

        canvas.save()
        canvas.translate(margin, yAxis) // position the text
        staticLayout.draw(canvas)
        canvas.restore()

        yAxis += staticLayout.height
    }

    fun writeMarkdown(
        markdown: String,
        paint: TextPaint,
        context: Context
    ) {

        val tableTheme = TableTheme.Builder()
            .tableBorderColor(Color.BLACK)
            .tableBorderWidth(0)
            .tableCellPadding(0)
            .build()

        val markwon: Markwon =  Markwon.builder(context)
            .usePlugin(TablePlugin.create(tableTheme))
            .build()
        val spanned = markwon.toMarkdown(markdown)

        val staticLayout: StaticLayout = StaticLayout.Builder.obtain(
            spanned,
            0,
            spanned.length,
            paint,
            maxWidth.toInt()
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .build()

        if (yAxis + staticLayout.height > pageHeight) {
            startNextPage()
        }

        canvas.save()
        canvas.translate(margin, yAxis) // position the text
        staticLayout.draw(canvas)
        canvas.restore()

        yAxis += staticLayout.height
    }

    fun writeImage(bitmap: Bitmap) {
        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        val ratio = imageWidth.toFloat() / imageHeight.toFloat()
        val newHeight = maxWidth / ratio

        if (yAxis + newHeight > pageHeight) {
            startNextPage()
        }

        canvas.drawBitmap(bitmap, margin, yAxis, Paint())

        yAxis += newHeight
    }

    fun addPadding(padding: Float) {
        yAxis += padding
    }

    fun finish(): PdfDocument {
        pdfDocument.finishPage(currentPage)
        return pdfDocument
    }
}

class FileManagementService {

    companion object {
        private const val TAG = "FileManagementService"
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

        fun exportChatAsPdf(
            title: String = "ChatExport",
            chatInfo: ExportedChat,
            context: Context
        ) {
            val pdfDocument = PdfDocument()

            val messagePaint = TextPaint()
            messagePaint.textSize = 14f
            messagePaint.isAntiAlias = true

            val rolePaint = TextPaint()
            rolePaint.isFakeBoldText = true

            val pdfWriter = PdfWriter(
                pdfDocument = pdfDocument,
                margin = 72f,
                pageWidth = 595,
                pageHeight = 842,
            )

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
//                val messageParts = message.text.split("\n\n")
//                for (messagePart in messageParts) {
//                    bgPaint.color = when (message.role) {
//                        Role.USER -> context.getColor(R.color.primary)
//                        Role.BOT -> context.getColor(R.color.tertiary)
//                        Role.SYSTEM -> context.getColor(R.color.secondary)
//                        else -> {
//                            context.getColor(R.color.primary)
//                        }
//                    }
//                    bgPaint.alpha = 50
//
//                    pdfConfig.writeMarkdown(
//                        markdown = messagePart,
//                        paint = messagePaint,
//                        bgPaint = bgPaint,
//                        context = context
//                    )
//                    pdfConfig.addPadding(10f)
//                }


                pdfWriter.writeMarkdown(
                    markdown = message.text,
                    paint = messagePaint,
                    context = context
                )

                pdfWriter.addPadding(20f)
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

            val pdfFile = File(filesDir, "exported_chats/${title}_${System.currentTimeMillis()}.pdf")
            val pdfUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()

            shareUri(context, pdfUri, "application/pdf", title)
        }

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

            val jsonFile = File(filesDir, "exported_chats/${title}_${System.currentTimeMillis()}.json")
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
