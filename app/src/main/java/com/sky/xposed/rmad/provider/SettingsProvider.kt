/*
 * Copyright (c) 2017 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.rmad.provider

import android.content.*
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.preference.PreferenceManager

/**
 * Created by sky on 17-11-2.
 */
class SettingsProvider : ContentProvider() {

    private val authority = "com.sky.xposed.rmad.settings.preference"

    private val stringPath = "string"
    private val intPath = "int"
    private val booleanPath = "boolean"
    private val longPath = "long"
    private val floatPath = "float"

    private val stringCode = 1
    private val intCode = 2
    private val booleanCode = 3
    private val longCode = 4
    private val floatCode = 5

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    private lateinit var mPreferences: SharedPreferences

    init {
        uriMatcher.addURI(authority, stringPath, stringCode)
        uriMatcher.addURI(authority, intPath, intCode)
        uriMatcher.addURI(authority, booleanPath, booleanCode)
        uriMatcher.addURI(authority, longPath, longCode)
        uriMatcher.addURI(authority, floatPath, floatCode)
    }

    override fun onCreate(): Boolean {
        // 初始化
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?,
                       selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        if (selection == null) return null

        when(uriMatcher.match(uri)) {
            stringCode -> return matrixCursor(mPreferences.getString(selection, ""))
            intCode -> return matrixCursor(mPreferences.getInt(selection, 0))
            booleanCode -> return matrixCursor(mPreferences.getBoolean(selection, true))
            longCode -> return matrixCursor(mPreferences.getLong(selection, 0L))
            floatCode -> return matrixCursor(mPreferences.getFloat(selection, 0.0F))
        }
        return null
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("No external updates")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("No external updates")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("No external updates")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("No external updates")
    }

    private inline fun <reified T> matrixCursor(value: T): Cursor {

        val cursor = MatrixCursor(arrayOf("value"), 1)

        // 添加数据
        cursor.addRow(arrayOf(value))

        return cursor
    }
}