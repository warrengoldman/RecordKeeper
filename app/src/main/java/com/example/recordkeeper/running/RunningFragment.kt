package com.example.recordkeeper.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.recordkeeper.BaseFragment
import com.example.recordkeeper.FileService
import com.example.recordkeeper.R
import com.example.recordkeeper.databinding.FragmentRunningBinding
import com.example.recordkeeper.editrecord.EditRecordActivity


const val RUNNING = "running"
const val RECORD = "record"
const val DATE = "date"
const val CYCLING = "cycling"
const val CYCLING_EVENTS = "5 Kilometers,15 Kilometers"
const val RUNNING_EVENTS = "5 Kilometers,Half Marathon,Full Marathon"

class RunningFragment : BaseFragment<FragmentRunningBinding>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRunningBinding {
        return FragmentRunningBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setupUi(view, container, inflater, RUNNING)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentLayout = view.findViewById<LinearLayout>(R.id.fragment_layout)
        val actType = RUNNING
        setupClickListener(view, fragmentLayout, actType)
    }
    override fun onResume() {
        super.onResume()
        displayRecords(getBinding().fragmentLayout)
    }

    override fun displayRecords(distance: String, record: TextView, date: TextView) {
        val runningPreferences = FileService.getRunningFile(requireContext(), distance)
        record.text = runningPreferences?.getString(RECORD, null)
        date.text = runningPreferences?.getString(DATE, null)
    }
}