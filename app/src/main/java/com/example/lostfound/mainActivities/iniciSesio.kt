package com.example.lostfound.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityIniciSesioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class iniciSesio : AppCompatActivity() {

    private lateinit var binding: ActivityIniciSesioBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inici_sesio)

        var correu = ""
        var contra = ""

        auth = Firebase.auth

        binding.logInButton.setOnClickListener { view : View ->
            correu = binding.iniciCorreu.text.toString()
            contra = binding.iniciPassword.text.toString()

//            if(correu.isNotBlank()|| contra.isNotBlank()) {
//                LogIn(correu,contra)
//
//            }
//            else {
//                Toast.makeText(this, "Correu o contrasenya incorrectes", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun LogIn(email : String, password : String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("TAG", "signInWithEmail:success")
                    val intent = Intent(this, lligamentFragments::class.java)
                    startActivity(intent)
                } else {
                    Log.e("TAG", "signInWithEmail:failures", task.exception)
                    Toast.makeText(this, "Autentificacio fallida", Toast.LENGTH_SHORT).show()

                }

            }
    }
}
