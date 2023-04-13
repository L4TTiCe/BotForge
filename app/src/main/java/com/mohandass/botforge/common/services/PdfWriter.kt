// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.mohandass.botforge.common.Utils
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tables.TableTheme

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

        text = "Time: ${Utils.formatTimeLong()}"
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
