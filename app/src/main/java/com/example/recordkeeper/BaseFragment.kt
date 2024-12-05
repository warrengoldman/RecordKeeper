package com.example.recordkeeper

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.recordkeeper.databinding.EventEntryBinding
import com.example.recordkeeper.editrecord.EditRecordActivity
import com.example.recordkeeper.running.DATE
import com.example.recordkeeper.running.RECORD

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var binding: T? = null
    abstract fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    abstract fun getFragmentName(): String

    abstract fun getFile(distance: String): SharedPreferences

    open fun getBinding(): T {
        if (binding == null) {
            throw Exception("init binding has not been call or is returning null")
        }
        return this.binding!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = getBinding(inflater, container, savedInstanceState)
        var viewGroup = container
        if (viewGroup == null) viewGroup = binding!!.root as ViewGroup
        setupUi(binding!!.root, viewGroup, inflater, getFragmentName())
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        val fragmentLayout = getBinding().root.findViewById<LinearLayout>(R.id.fragment_layout)
        for (i in 0 until fragmentLayout.childCount) {
            val constraintLayout: ConstraintLayout =
                fragmentLayout.getChildAt(i) as ConstraintLayout
            val distance =
                constraintLayout.findViewById<TextView>(R.id.text_view_heading).text.toString()
            val preferences = getFile(distance)
            val record = constraintLayout.findViewById<TextView>(R.id.text_view_value)
            val date = constraintLayout.findViewById<TextView>(R.id.text_view_date)
            record.text = preferences?.getString(RECORD, null)
            date.text = preferences?.getString(DATE, null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener(view)
    }

    private fun setupClickListener(
        view: View
    ) {
        val fragmentLayout = view.findViewById<LinearLayout>(R.id.fragment_layout)
        val actType = getFragmentName()
        val eventFile = FileService.getFile(view.context, "events")
        for (i in 0 until fragmentLayout.childCount) {
            val constraintLayout: ConstraintLayout =
                fragmentLayout.getChildAt(i) as ConstraintLayout
            val eventType =
                constraintLayout.findViewById<TextView>(R.id.text_view_heading).text.toString()
            val hintKey = "$actType-$eventType-hint"
            var hint = eventFile?.getString(hintKey, null)
            if (hint == null) {
                hint = "$actType time"
                eventFile?.edit {
                    putString(hintKey, hint)
                }
            }
            setOnClickListener(
                constraintLayout, constraintLayout.getChildAt(0).id, actType,
                hint, EditRecordActivity::class.java
            )
        }
    }

    private fun setOnClickListener(
        constraintLayout: ConstraintLayout,
        extraId: Int,
        activityType: String,
        hint: String,
        javaClass: Class<out AppCompatActivity>
    ) {
        constraintLayout.setOnClickListener {
            val intent = Intent(context, javaClass)
            val distanceStr = constraintLayout.findViewById<TextView>(extraId).text.toString()
            val filename = "$activityType $distanceStr"
            intent.putExtra(
                "screen_data",
                EditRecordActivity.ScreenData(distanceStr, filename, hint)
            )
            startActivity(intent)
        }
    }

    private fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T {
        if (binding == null) {
            this.binding = initBinding(inflater, container, savedInstanceState = null)
        }
        return this.binding!!
    }

    private fun setupUi(
        view: View,
        container: ViewGroup?,
        inflater: LayoutInflater,
        actType: String
    ) {
        val fragmentLayout = view.findViewById<LinearLayout>(R.id.fragment_layout)
        val eventFile = FileService.getFile(container?.context!!, "events")
        val toTypedArray = eventFile?.getString(actType, null)?.split(",")?.toTypedArray()
        if (toTypedArray != null) {
            for (activityType in toTypedArray) {
                val eeView = EventEntryBinding.inflate(inflater).root
                val textView = eeView.findViewById<TextView>(R.id.text_view_heading)
                textView.text = activityType
                fragmentLayout.addView(eeView)
            }
        }
    }
}