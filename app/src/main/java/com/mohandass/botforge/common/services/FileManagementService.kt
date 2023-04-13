// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.common.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.content.FileProvider
import com.mohandass.botforge.R
import com.mohandass.botforge.common.services.snackbar.SnackbarManager
import java.io.File
import java.io.FileWriter


class FileManagementService {

    companion object {
        private const val TAG = "FileManagementService"

        fun exportChatAsJson(
            title: String? = "ChatExport",
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

            // An intent to share the file with other apps
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_STREAM, jsonUri)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, jsonString)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val chooser = Intent.createChooser(shareIntent, "Share File")

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
                    jsonUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            context.startActivity(chooser)
        }
    }
}
