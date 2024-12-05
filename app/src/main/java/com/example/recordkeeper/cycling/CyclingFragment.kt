package com.example.recordkeeper.cycling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import com.example.recordkeeper.BaseFragment
import com.example.recordkeeper.FileService
import com.example.recordkeeper.R
import com.example.recordkeeper.databinding.EventEntryBinding
import com.example.recordkeeper.databinding.FragmentCyclingBinding
import com.example.recordkeeper.editrecord.EditRecordActivity
import com.example.recordkeeper.running.DATE
import com.example.recordkeeper.running.RECORD

class CyclingFragment : BaseFragment<FragmentCyclingBinding>() {
    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentCyclingBinding {
        return FragmentCyclingBinding.inflate(inflater, container, false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setupUi(view, container, inflater, "cycling")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentLayout = view.findViewById<LinearLayout>(R.id.fragment_layout)
        val actType = "cycling"
        setupClickListener(view, fragmentLayout, actType)
    }

    override fun onResume() {
        super.onResume()
        displayRecords(getBinding().fragmentLayout)
    }

    override fun displayRecords(distance: String, record: TextView, date: TextView) {
        val runningPreferences = FileService.getCyclingFile(requireContext(), distance)
        record.text = runningPreferences?.getString(RECORD, null)
        date.text = runningPreferences?.getString(DATE, null)
    }
}