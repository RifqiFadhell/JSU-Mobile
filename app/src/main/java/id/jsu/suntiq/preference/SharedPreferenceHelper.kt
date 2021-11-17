package id.jsu.suntiq.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SharedPreferenceHelper {
    private const val PREF_FILE = "ULA_APP"
    private const val SUGGESTION_ITEMS = "SUGGESTION_ITEMS"
    private const val APPLIED_COUPON_CODE = "applied_coupon_code"

    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    fun setSharedPreferenceString(context: Context, key: String, value: String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Get a string shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    fun getSharedPreferenceString(
        context: Context,
        key: String,
        defValue: String
    ): String? {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        return settings.getString(key, defValue)
    }

    /**
     * Removes the specified key shared preferences file
     * @param key - Key to remove
     * */
    fun removeSharedPreference(context: Context, key: String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        settings.edit().remove(key).apply()
    }

    /**
     * Add to suggestions shared preference
     * @param value - Value for the key
     */
    fun addToSuggestionList(context: Context, value: String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val suggestionSet = settings.getStringSet(SUGGESTION_ITEMS, hashSetOf()) as HashSet
        if (!suggestionSet.contains(value)) {
            val newSuggestionSet = HashSet<String>()
            newSuggestionSet.addAll(suggestionSet.distinct().take(9))
            newSuggestionSet.add(value)

            val editor = settings.edit()
            editor.putStringSet(SUGGESTION_ITEMS, newSuggestionSet.toHashSet())
            editor.apply()
        }
    }

    /**
     * Remove from suggestions shared preference
     * @param value - Value for the key
     */
    fun removeFromSuggestionList(context: Context, value: String) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val suggestionSet = settings.getStringSet(SUGGESTION_ITEMS, hashSetOf()) as HashSet
        suggestionSet.remove(value)
        val newSuggestionSet = HashSet<String>()
        newSuggestionSet.addAll(suggestionSet.take(10).toHashSet())
        val editor = settings.edit()
        editor.remove(SUGGESTION_ITEMS)
        editor.apply()
        editor.putStringSet(SUGGESTION_ITEMS, newSuggestionSet)
        editor.apply()
    }

    /**
     * Get suggestions shared preference
     * @return value - Set of suggestions if found.
     */
    fun getSuggestionList(
        context: Context
    ): Set<String>? {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        return settings.getStringSet(SUGGESTION_ITEMS, emptySet())
    }

    /**
     * Clear shared preference
     * @return value - String containing value of the shared preference if found.
     */
    fun clearSharedPreferences(context: Context) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        settings.edit().clear().apply()
    }

    /**
     * Set a boolean / Int / Long / Float / String value to shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    fun <T> setSharedPreferenceValue(context: Context, key: String, value: T) {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is String -> editor.putString(key, value)
            else -> throw IllegalArgumentException("Unsupported value type")
        }
        editor.apply()
    }

    /**
     * Get a a boolean value from shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - Boolean value of the shared preference if found.
     */
    fun getSharedPreferenceBoolean(
        context: Context,
        key: String,
        defValue: Boolean
    ): Boolean {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        return settings.getBoolean(key, defValue)
    }

    /**
     * Get a a int value from shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - Int value of the shared preference if found.
     */
     fun getSharedPreferenceInt(
        context: Context,
        key: String,
        defValue: Int
    ): Int {
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        return settings.getInt(key, defValue)
    }

     fun addSharedPreferenceChangeListener(
        context: Context,
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        context.getSharedPreferences(PREF_FILE, 0)
            .apply {
                registerOnSharedPreferenceChangeListener(listener)
            }
    }
}
