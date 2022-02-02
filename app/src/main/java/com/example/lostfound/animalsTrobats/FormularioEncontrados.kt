package com.example.lostfound.animalsTrobats

import android.os.Bundle
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
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class formularioEncontrados : Fragment() {
    private val db = Firebase.firestore
    private lateinit var aux : auxGeneral

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFormularioEncontradosBinding>(inflater,
            R.layout.fragment_formulario_encontrados,container,false)


        var nom = ""
        var color = ""
        var llocTrobat = ""
        var descripcio = ""
        var tipus = ""

        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)

        binding.guardarInformacio.setOnClickListener{
            nom = binding.nomAnimal.text.toString()
            color = binding.colorAnimal.text.toString()
            llocTrobat =  binding.llocTrobat.text.toString()
            descripcio = binding.DESCRIPCIO.text.toString()
            tipus = aux.getAnimal()

            if(color.isNotBlank() && llocTrobat.isNotBlank() && descripcio.isNotBlank()){
                crearAnimalPerdut(tipus , nom, color, llocTrobat, descripcio)
                view?.findNavController()?.navigate(R.id.action_formularioEncontrados_to_animalsTrobats)


            }

            else{
                Toast.makeText(context, "No s'han introduit les dades obligatories", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }




    fun crearAnimalPerdut(tipus : String, nom : String, color : String, lloc : String, detalls : String){

        val document = db.collection("AnimalsTrobats").document()
        val documentId = document.id

        val animal = hashMapOf(
            "tipus" to tipus,
            "nom" to nom,
            "color" to color,
            "detalls" to detalls,
            "llocTrobat" to lloc,
            "telefon" to SharedApp.prefs.telefonUsuari,
            "nomAmo" to SharedApp.prefs.nomUsuari,
            "id" to documentId

        )

        document.set(animal)

    }





}