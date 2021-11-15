package com.diman.sipenguji.util

import android.content.Context

class SharedPreferences (val context: Context) {

    companion object {
        private const val FIRST_INSTALL = "FIRST_INSTALL"
        private const val USER_SIGNATURE = "USER_SIGNATURE"
        private const val USER_CURRENT_LAT = "USER_CURRENT_LAT"
        private const val USER_CURRENT_LNG = "USER_CURRENT_LNG"
    }

    private val p = context.getSharedPreferences(context.packageName + "_Preferences", Context.MODE_PRIVATE)

    var firstInstall = p.getBoolean(FIRST_INSTALL, false)
        set(value) = p.edit().putBoolean(FIRST_INSTALL, value).apply()

    var userSignature = p.getString(USER_SIGNATURE, "")
        set(value) = p.edit().putString(USER_SIGNATURE, value).apply()

    var userCurrentLat = p.getString(USER_CURRENT_LAT, "")
        set(value) = p.edit().putString(USER_CURRENT_LAT, value).apply()

    var userCurrentLng = p.getString(USER_CURRENT_LNG, "")
        set(value) = p.edit().putString(USER_CURRENT_LNG, value).apply()
}