package com.example.ejemplosharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.example.ejemplosharedpreferences.PreferenceHelper.defaultPreference
import com.example.ejemplosharedpreferences.PreferenceHelper.password
import com.example.ejemplosharedpreferences.PreferenceHelper.userId
import com.example.ejemplosharedpreferences.PreferenceHelper.clearValues
import com.example.ejemplosharedpreferences.PreferenceHelper.customPreference
import com.example.ejemplosharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMainBinding

    val CUSTOM_PREF_NAME = "User_data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener(this)
        binding.btnClear.setOnClickListener(this)
        binding.btnShow.setOnClickListener(this)
        binding.btnShowDefault.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val prefs = customPreference(this, CUSTOM_PREF_NAME)
        when (v?.id) {
            R.id.btnSave -> {
                prefs.password = binding.inPassword.text.toString()
                prefs.userId = binding.inUserId.text.toString().toInt()
            }
            R.id.btnClear -> {
                prefs.clearValues()
            }
            R.id.btnShow -> {
                binding.inUserId.setText(prefs.userId.toString())
                binding.inPassword.setText(prefs.password)
            }
            R.id.btnShowDefault -> {
                val defaultPrefs = defaultPreference(this)
                binding.inUserId.setText(defaultPrefs.userId.toString())
                binding.inPassword.setText(defaultPrefs.password)
            }
        }
    }
}
object PreferenceHelper {

    val USER_ID = "USER_ID"
    val USER_PASSWORD = "PASSWORD"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userId
        get() = getInt(USER_ID, 0)
        set(value) {
            editMe {
                it.putInt(USER_ID, value)
            }
        }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                it.putString(USER_PASSWORD, value)
            }
        }

    fun SharedPreferences.clearValues() {
        editMe {
            it.clear()
        }
    }

    fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }
}