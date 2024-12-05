package com.example.recordkeeper

import android.content.Context
import android.content.SharedPreferences

object FileService {
    private var map = mutableMapOf<String, SharedPreferences>()
    fun getRunningFile(context: Context, distance: String): SharedPreferences? {
        return getFile(context,"running $distance")
    }
    fun getCyclingFile(context: Context, distance: String): SharedPreferences? {
        return getFile(context,"cycling $distance")
    }
    fun getFile(context: Context, filename: String): SharedPreferences? {
        if (map[filename] == null) {
            map[filename] = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        }
        return map[filename]
    }
}