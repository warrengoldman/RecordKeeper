package com.example.recordkeeper.editrecord

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.recordkeeper.FileService
import com.example.recordkeeper.databinding.ActivityEditRecordBinding
import com.example.recordkeeper.running.DATE
import com.example.recordkeeper.running.RECORD
import java.io.Serializable

class EditRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRecordBinding
    private val screenData: ScreenData by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("screen_data", ScreenData::class.java) as ScreenData
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("screen_data") as ScreenData
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        displayRecord()
    }

    private fun setupUi() {
        title = "${screenData.record} $RECORD"
        binding.buttonSave.setOnClickListener {
            saveRecord()
            finish()
        }
        binding.buttonDelete.setOnClickListener {
            clearRecord()
            finish()
        }

        binding.textInputRecord.hint = screenData.recordFieldHint
    }

    private fun displayRecord() {
        binding.editTextRecord.setText(getFile()?.getString(RECORD, null))
        binding.editTextDate.setText(getFile()?.getString(DATE, null))
    }

    private fun getFile(): SharedPreferences? {
        return FileService.getFile(this, screenData.sharedPreferencesName)
    }

    private fun saveRecord() {
        getFile()?.edit {
            putString(RECORD, binding.editTextRecord.text.toString())
            putString(DATE, binding.editTextDate.text.toString())
        }
    }

    private fun clearRecord() {
        getFile()?.edit {
            remove(RECORD)
            remove(DATE)
        }
    }
    data class ScreenData(
        val record: String,
        val sharedPreferencesName: String,
        val recordFieldHint: String
    ) : Serializable
}