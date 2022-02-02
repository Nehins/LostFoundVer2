package com.example.lostfound

import android.app.ProgressDialog.show
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.lostfound.databinding.FragmentAnimalsTrobatsBinding
import com.example.lostfound.databinding.FragmentFormularioPeridosBinding
import com.example.lostfound.mainActivities.iniciSesio
import com.example.lostfound.seleccioAnimal.seleccioAnimalAux
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class formularioPeridos : Fragment() {
    private val db = Firebase.firestore
    private lateinit var aux : seleccioAnimalAux

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFormularioPeridosBinding>(inflater,
            R.layout.fragment_formulario_peridos,container,false)


        var amigable : Boolean
        var nom = ""
        var color = ""
        var visualitzacio = ""
        var descripcio = ""
        var tipus = ""

        aux = ViewModelProvider(requireActivity()).get(seleccioAnimalAux::class.java)

        binding.guardarInformacio.setOnClickListener{
            nom = binding.nomAnimal.text.toString()
            color = binding.colorAnimal.text.toString()
            visualitzacio =  binding.ultimaVisualitzacio.text.toString()
            descripcio = binding.DESCRIPCIO.text.toString()
            tipus = aux.getAnimal()
            amigable = binding.amigable.isChecked

            if(nom.isNotBlank() && color.isNotBlank() && visualitzacio.isNotBlank() && descripcio.isNotBlank()){
                crearAnimalPerdut(tipus , nom, color, visualitzacio, descripcio, amigable)
                view?.findNavController()?.navigate(R.id.action_formularioPeridos_to_animalsPerduts2)


            }

            else{
                Toast.makeText(context, "No s'han introduit les dades obligatories", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }




    fun crearAnimalPerdut(tipus : String, nom : String, color : String, visualitzacio : String, detalls : String, amigable : Boolean){

        val document = db.collection("AnimalsPerduts").document()
        val documentId = document.id

        val animal = hashMapOf(
            "tipus" to tipus,
            "nom" to nom,
            "color" to color,
            "detalls" to detalls,
            "ultimaVisualitzacio" to visualitzacio,
            "amigable" to amigable,
            "telefon" to SharedApp.prefs.telefonUsuari,
            "nomAmo" to SharedApp.prefs.nomUsuari,
            "id" to documentId

            )

       document.set(animal)


    }





}