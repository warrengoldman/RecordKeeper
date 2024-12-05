package com.example.recordkeeper.cycling

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.recordkeeper.BaseFragment
import com.example.recordkeeper.FileService
import com.example.recordkeeper.databinding.FragmentCyclingBinding
import com.example.recordkeeper.running.CYCLING

class CyclingFragment : BaseFragment<FragmentCyclingBinding>() {
    override fun initBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentCyclingBinding {
        return FragmentCyclingBinding.inflate(inflater, container, false)
    }

    override fun getFragmentName(): String {
        return CYCLING
    }

    override fun getFile(distance: String): SharedPreferences {
        return FileService.getCyclingFile(requireContext(), distance)!!
    }
}