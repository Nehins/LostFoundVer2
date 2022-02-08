package com.example.lostfound.cambiarUsuari

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.auxGeneral
import com.example.lostfound.databinding.FragmentFormularioEncontradosBinding
import com.example.lostfound.databinding.FragmentUpdateUsuariBinding
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class update_usuari : Fragment() {

    private val user = Firebase.auth.currentUser
    private lateinit var db : FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentUpdateUsuariBinding>(inflater,
            R.layout.fragment_update_usuari,container,false)

        db = FirebaseFirestore.getInstance()
        db.collection("Usuaris").whereEqualTo("id", SharedApp.prefs.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    binding.nomUsuari.setText(document["nom"].toString())
                    binding.telefon.setText(document["telefon"].toString())
                    binding.correu.setText(document["correu"].toString())
                    binding.contra.setText(document["contrasenya"].toString())
                    binding.verContra.setText(document["contrasenya"].toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


        binding.button.setOnClickListener {
            if(binding.contra.text.toString().equals(binding.verContra.text.toString())){
                nouCorreu(binding.correu.text.toString())
                novaContrasenya(binding.contra.text.toString())
                cambiarUsuari(binding.nomUsuari.text.toString(),
                                binding.telefon.text.toString(),
                                binding.correu.text.toString(),
                                binding.contra.text.toString())

            }

        }


        return binding.root
    }


    fun nouCorreu(correuNou : String){
        user!!.updateEmail(correuNou)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Correu modificat", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun novaContrasenya(contraNou : String){
        user!!.updatePassword(contraNou)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Contrasenya modificada", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun cambiarUsuari(nom : String, telefon : String, correu : String, contra : String){

        val doc =  db.collection("Usuaris").document(SharedApp.prefs.id)

        val user = mapOf(
            "nom" to nom,
            "telefon" to telefon,
            "correu" to correu,
            "contrasenya" to contra
        )

        doc.update(user)

    }

}

