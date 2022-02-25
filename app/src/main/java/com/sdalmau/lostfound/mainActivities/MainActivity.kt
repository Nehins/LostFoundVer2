package com.sdalmau.lostfound.mainActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sdalmau.lostfound.R
import com.sdalmau.lostfound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.iniciSessiobutton.setOnClickListener { view : View ->
            val intent = Intent(this, iniciSesio::class.java)
            startActivity(intent)

        }

        binding.RegistreSessiobutton.setOnClickListener {view : View ->
            val intent = Intent(this, registre::class.java)
            startActivity(intent)
        }
    }
}