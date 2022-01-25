package com.example.lostfound.mainActivities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityMainBinding
import com.example.lostfound.databinding.ActivityRegistreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class registre : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityRegistreBinding
    private val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        var usuari = ""
        var telefon = ""
        var mEmail = ""
        var mPassword = ""
        var mRepeatPassword = ""

        auth = Firebase.auth

        binding.singIn.setOnClickListener {
            usuari = binding.registreNomUsuari.text.toString()
            telefon = binding.registreTelefon.text.toString()
            mEmail = binding.registreCorreu.text.toString()
            mPassword = binding.registreContra.text.toString()
            mRepeatPassword = binding.registreContraVerificar.text.toString()


            if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(this, "El email no es valid.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty()) {
                Toast.makeText(this, "La contrasenya no es valida.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword) {
                Toast.makeText(this, "La confirmacio de la contrasenya no es correcte.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.length<8) {
                Toast.makeText(this, "La contrasenya ha de tindre com a minim 8 caracters.",
                    Toast.LENGTH_SHORT).show()
            }else  {
                crearUsuari(usuari, telefon, mEmail, mPassword)
                createAccount(mEmail, mPassword)

            }
        }


    }

    private fun createAccount(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val intent = Intent(applicationContext, iniciSesio::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Autentificacio fallida.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun crearUsuari(nom : String, telefon : String, email : String, password : String){

        val user = hashMapOf(
            "nom" to nom,
            "telefon" to telefon,
            "correu" to email,
            "contrasenya" to password,

        )

        db.collection("Usuaris")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

}