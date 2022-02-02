package com.example.lostfound.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {

    val PREFS_NAME = "com.lostfound.sharedpreferences"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)


    var SHARED_TELEFON = "t"
    var telefonUsuari: String
        get() = prefs.getString(SHARED_TELEFON, "")!!
        set(value) = prefs.edit().putString(SHARED_TELEFON, value).apply()


    var SHARED_USUARI= "u"
    var nomUsuari: String
        get() = prefs.getString(SHARED_USUARI, "")!!
        set(value) = prefs.edit().putString(SHARED_USUARI, value).apply()


}
