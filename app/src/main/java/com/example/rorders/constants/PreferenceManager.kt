package com.example.rorders.constants

import android.content.Context


class PreferenceManager {

    companion object{
        private val PREFSNAME = "RORDERS"

        /*************************App First Launch************************/
        fun isFirstLaunch(context: Context, isFirstLaunch: Boolean)
        {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean("is_first_launch", isFirstLaunch)
            editor.apply()
        }
        fun getIsFirstLaunch(context: Context): Boolean
        {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            return prefs.getBoolean("is_first_launch", false)
        }
        fun setUserCode(context: Context, usercode: String?) {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("user_code", usercode)
            editor.apply()
        }
        fun getUserCode(context: Context): String? {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            return prefs.getString("user_code", "")
        }
        fun setAdminValue(context: Context, adminvalue: String?) {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("adminvalue", adminvalue)
            editor.apply()
        }
        fun getAdminValue(context: Context): String? {
            val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
            return prefs.getString("adminvalue", "")
        }

    }






}