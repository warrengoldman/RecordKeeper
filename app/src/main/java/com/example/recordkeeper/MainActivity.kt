package com.example.recordkeeper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recordkeeper.cycling.CyclingFragment
import com.example.recordkeeper.databinding.ActivityMainBinding
import com.example.recordkeeper.running.CYCLING
import com.example.recordkeeper.running.CYCLING_EVENTS
import com.example.recordkeeper.running.RUNNING
import com.example.recordkeeper.running.RUNNING_EVENTS
import com.example.recordkeeper.running.RunningFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.bottomNav.setOnItemSelectedListener(this)
        val file = FileService.getFile(this, "events")
        file?.edit {
            putString(CYCLING, CYCLING_EVENTS)
            putString(RUNNING, RUNNING_EVENTS)
        }
        binding.viewPager2.adapter = PagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when(position) {
                0 -> tab.text = RUNNING.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                1 -> tab.text = CYCLING.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val result = when (item.itemId) {
            R.id.reset_running -> resetRunning()
            R.id.reset_cycling -> resetCycling()
            R.id.reset_all -> resetAll()
            else -> super.onOptionsItemSelected(item)
        }
//        onNavigationItemSelected(binding.bottomNav.selectedItemId)
        return result
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return onNavigationItemSelected(item.itemId)
    }

    private fun onNavigationItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.nav_bikling -> setFragment(CyclingFragment())
            R.id.nav_running -> setFragment(RunningFragment())
            else -> false
        }
    }

    private fun setFragment(fragment: Fragment): Boolean {
        supportFragmentManager.commit {
//            replace(R.id.frame_content, fragment)
        }
        return true
    }

    private fun resetRunning(): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Reset running records")
            .setMessage("Are you sure you want to clear the records?")
            .setPositiveButton("Yes") { _, _ ->
                clearRunningFile()
                postClear()
            }
            .setNegativeButton("No", null)
            .show()
        return true
    }

    private fun resetCycling(): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Reset cycling records")
            .setMessage("Are you sure you want to clear the records?")
            .setPositiveButton("Yes") { _, _ ->
                clearCyclingFile()
                postClear()
            }
            .setNegativeButton("No", null)
            .show()
        return true
    }

    private fun resetAll(): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Reset all records")
            .setMessage("Are you sure you want to clear all the records?")
            .setPositiveButton("Yes") { _, _ ->
                clearRunningFile()
                clearCyclingFile()
                postClear()
            }
            .setNegativeButton("No", null)
            .show()
        return true
    }
    private fun postClear() {
//        val snackbar = Snackbar.make(binding.frameContent, "Records cleared successfully!", Snackbar.LENGTH_LONG)
//        snackbar.anchorView = binding.bottomNav
//        snackbar.show()
//        onNavigationItemSelected(binding.bottomNav.selectedItemId)
    }
    private fun clearCyclingFile() {
        val distances = FileService.getFile(this, "events")?.getString(CYCLING, null)?.split(",")?.toTypedArray()
        if (distances != null) {
            for (distance in distances) {
                FileService.getCyclingFile(this, distance)?.edit { clear() }
            }
        }
    }
    private fun clearRunningFile() {
        val distances = FileService.getFile(this, "events")?.getString(RUNNING, null)?.split(",")?.toTypedArray()
        if (distances != null) {
            for (distance in distances) {
                FileService.getRunningFile(this, distance)?.edit { clear() }
            }
        }
    }
    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> RunningFragment()
            else -> CyclingFragment()
        }
    }
}