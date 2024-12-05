package com.example.recordkeeper

import android.content.Context
import android.content.Intent
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

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var binding: T? = null
    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : T
    private fun getBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : T {
        if (binding == null) {
            this.binding = initBinding(inflater, container, savedInstanceState = null)
        }
        return this.binding!!
    }
    open fun getBinding() : T {
        if (binding == null) {
            throw Exception("init binding has not been call or is returning null")
        }
        return this.binding!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = getBinding(inflater, container, savedInstanceState)
        return binding!!.root
    }

    private fun setOnClickListener(constraintLayout: ConstraintLayout, extraId: Int, activityType: String, hint: String, javaClass: Class<out AppCompatActivity>) {
        constraintLayout.setOnClickListener {
            val intent = Intent(context, javaClass)
            val distanceStr = constraintLayout.findViewById<TextView>(extraId).text.toString()
            val filename = "$activityType $distanceStr"
            intent.putExtra("screen_data", EditRecordActivity.ScreenData(distanceStr, filename, hint))
            startActivity(intent)
        }
    }

    fun setupUi(
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

    fun displayRecords(fragmentLayout: LinearLayout) {
        for (i in 0 until fragmentLayout.childCount) {
            val constraintLayout: ConstraintLayout =
                fragmentLayout.getChildAt(i) as ConstraintLayout
            val heading =
                constraintLayout.findViewById<TextView>(R.id.text_view_heading).text.toString()
            val valueView = constraintLayout.findViewById<TextView>(R.id.text_view_value)
            val dateView = constraintLayout.findViewById<TextView>(R.id.text_view_date)
            displayRecords(heading, valueView, dateView)
        }
    }
    abstract fun displayRecords(distance: String, record: TextView, date: TextView)

    fun setupClickListener(
        view: View,
        fragmentLayout: LinearLayout,
        actType: String
    ) {
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
}