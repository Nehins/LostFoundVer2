package com.example.lostfound.animalsPerduts

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.auxGeneral
import com.example.lostfound.databinding.FragmentAnimalSelecionatPerdutsBinding
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class animalSelecionatPerduts : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var aux : auxGeneral

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAnimalSelecionatPerdutsBinding>(inflater,
            R.layout.fragment_animal_selecionat_perduts,container,false)

        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)


        db = FirebaseFirestore.getInstance()
        db.collection("AnimalsPerduts").whereEqualTo("id", aux.getidPerdut())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var amigable = ""
                    var nomUsuari = "Nom del amo : "+ document["nomAmo"].toString() +" \n"
                   if(document["amigable"]==true){
                       amigable = ". Es amigable"
                   }
                    else{
                       amigable = ". No es amigable"
                   }
                    binding.nom2.setText(document["nom"].toString())
                    binding.color.text = document["color"].toString()
                    binding.localidadPerdut.setText(document["ultimaVisualitzacio"].toString())
                    binding.detalls.setText(nomUsuari + document["detalls"].toString()+amigable)
                    binding.telefon.setText(document["telefon"].toString())
                    Picasso.get()
                        .load(document["imatge"].toString())
                        .into(binding.imageView3)


                    if(SharedApp.prefs.telefonUsuari.equals(binding.telefon.text.toString())){
                        binding.trobatButton.setVisibility(View.VISIBLE)
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


        binding.telefon.setOnClickListener{
            val telefonPredeterminat = "+34"+binding.telefon.text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+telefonPredeterminat))
            startActivity(intent)
        }

        binding.trobatButton.setOnClickListener{
                db.collection("AnimalsPerduts").document(aux.getidPerdut())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "L'animal s'ha qualificat com a trobat!", Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_animalSelecionat_to_animalsPerduts2)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "No s'ha pogut qualificar l'animal com a trobar", Toast.LENGTH_SHORT).show()
                    }

        }

        return binding.root
    }

}