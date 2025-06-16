package soltani.code.taskvine.helpers

import android.content.Context
import android.content.SharedPreferences

open class PreferenceHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_WELCOME_SHOWN = "KEY_WELCOME_SHOWN"
    }

    open fun setWelcomeScreenShown() {
        sharedPreferences.edit().putBoolean(KEY_WELCOME_SHOWN, true).apply()
    }

    open fun isWelcomeScreenShown(): Boolean {
        return sharedPreferences.getBoolean(KEY_WELCOME_SHOWN, false)
    }
}
