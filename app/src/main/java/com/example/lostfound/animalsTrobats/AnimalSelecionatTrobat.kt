package com.example.lostfound.animalsTrobats

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
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
import com.example.lostfound.databinding.FragmentAnimalSelecionatPerdutsBinding
import com.example.lostfound.databinding.FragmentAnimalSelecionatTrobatBinding
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.app.ActivityCompat.startActivityForResult





class AnimalSelecionatTrobat : Fragment() {


    private lateinit var db : FirebaseFirestore
    private lateinit var aux : auxGeneral

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAnimalSelecionatTrobatBinding>(inflater,
            R.layout.fragment_animal_selecionat_trobat,container,false)



        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)
        var identificarUsuari = ""

        db = FirebaseFirestore.getInstance()
        db.collection("AnimalsTrobats").whereEqualTo("id", aux.getidTrobat())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {


                    var nomUsuari = "Nom del cuidador : "+ document["omCuidador"].toString() +" \n"

                    binding.nom2.setText(document["nom"].toString())
                    binding.color.text = document["color"].toString()
                    binding.localidadTrobat.setText(document["llocTrobat"].toString())
                    binding.detalls.setText(nomUsuari + document["detalls"].toString())
                    binding.telefon.setText(document["telefon"].toString())

                    identificarUsuari = document["telefon"].toString()

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
            if(identificarUsuari.equals(SharedApp.prefs.telefonUsuari)){
                db.collection("AnimalsTrobats").document(aux.getidTrobat())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "L'animal s'ha qualificat com a trobat!", Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_animalSelecionatTrobat_to_animalsTrobats)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "No s'ha pogut calificar l'animal com a trobar", Toast.LENGTH_SHORT).show()
                    }

            }

            else{
                Toast.makeText(context, "Nomes es pot donar per trobat l'animal quan l'usuari que a introduit la mascota el dona de baixa", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

}