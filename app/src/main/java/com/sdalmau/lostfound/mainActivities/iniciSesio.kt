package com.sdalmau.lostfound.mainActivities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sdalmau.lostfound.R
import com.sdalmau.lostfound.databinding.ActivityIniciSesioBinding
import com.sdalmau.lostfound.sharedPreferences.SharedApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class iniciSesio : AppCompatActivity() {

    private lateinit var binding: ActivityIniciSesioBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharedApp.prefs.telefonUsuari = ""
        SharedApp.prefs.nomUsuari = ""
        SharedApp.prefs.id = ""

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inici_sesio)

        var correu = ""
        var contra = ""

        auth = Firebase.auth

        binding.logInButton.setOnClickListener { view : View ->
            correu = binding.iniciCorreu.text.toString()
            contra = binding.iniciPassword.text.toString()

            if(correu.isNotBlank()|| contra.isNotBlank()) {
                LogIn(correu,contra)


            }
            else {
                Toast.makeText(this, "Correu o contrasenya incorrectes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LogIn(email : String, password : String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("TAG", "signInWithEmail:success")
                    obtenerInfo()
                    val intent = Intent(this, lligamentFragments::class.java)
                    startActivity(intent)
                }
                else {
                    Log.e("TAG", "signInWithEmail:failures", task.exception)
                    Toast.makeText(this, "Autentificacio fallida", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun obtenerInfo(){
        db = FirebaseFirestore.getInstance()
        db.collection("Usuaris")
            .whereEqualTo("correu", binding.iniciCorreu.text.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    SharedApp.prefs.telefonUsuari = document["telefon"].toString()
                    SharedApp.prefs.nomUsuari = document["nom"].toString()
                    SharedApp.prefs.id = document["id"].toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }
    @Override
    override fun onBackPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


}
