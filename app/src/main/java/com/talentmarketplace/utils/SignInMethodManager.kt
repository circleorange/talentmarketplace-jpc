package com.talentmarketplace.utils

import android.content.SharedPreferences

class SignInMethodManager(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val SIGN_IN_METHOD_KEY = "sign_in_method"
        const val GOOGLE = "GOOGLE"
        const val BASIC = "BASIC"
        const val UNKNOWN = "UNKNOWN"
    }

    fun setSignInMethod(method: String) {
        sharedPreferences.edit().putString(SIGN_IN_METHOD_KEY, method).apply()
    }

    fun getSignInMethod(): String {
        return sharedPreferences.getString(SIGN_IN_METHOD_KEY, UNKNOWN) ?: UNKNOWN
    }
}