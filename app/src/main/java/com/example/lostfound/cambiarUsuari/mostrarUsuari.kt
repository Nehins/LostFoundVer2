package com.example.lostfound.cambiarUsuari

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentMostrarUsuariBinding
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class mostrarUsuari : Fragment() {

    private lateinit var db : FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMostrarUsuariBinding>(inflater,
            R.layout.fragment_mostrar_usuari,container,false)

        db = FirebaseFirestore.getInstance()
        db.collection("Usuaris").whereEqualTo("id", SharedApp.prefs.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    binding.nomPerfil.setText(document["nom"].toString())
                    binding.telefonPerfil.setText(document["telefon"].toString())
                    binding.correuPerfil.setText(document["correu"].toString())
                    Picasso.get()
                        .load(document["imatge"].toString())
                        .into(binding.imageView2)

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


        binding.modificarPerfil.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mostrarUsuari_to_update_usuari)

        }


        return binding.root
    }


}