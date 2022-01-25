package com.example.lostfound.mainActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lostfound.R
import com.example.lostfound.animalsPerduts.animalsPerduts
import com.example.lostfound.animalsTrobats.animalsTrobats
import com.example.lostfound.databinding.ActivityLligamentFragmentsBinding

class lligamentFragments : AppCompatActivity() {

    private lateinit var binding: ActivityLligamentFragmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lligament_fragments)

//        var navView = binding.bottomNavigationView
//        navView.setOnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.animalsPerduts -> {
//                    val fragment = animalsPerduts.newInstance()
//                    openFragment(fragment)
//                    true
//                }
//                R.id.animalsTrobats -> {
//                    val fragment = animalsTrobats.newInstance()
//                    openFragment(fragment)
//                    true
//                }
//
//                else -> false
//            }
//        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.holaNavHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}