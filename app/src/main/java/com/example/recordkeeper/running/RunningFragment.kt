package com.example.recordkeeper.running

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.recordkeeper.BaseFragment
import com.example.recordkeeper.FileService
import com.example.recordkeeper.databinding.FragmentRunningBinding


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

    override fun getFragmentName(): String {
        return RUNNING
    }

    override fun getFile(distance: String): SharedPreferences {
        return FileService.getRunningFile(requireContext(), distance)!!
    }
}