package com.sdalmau.lostfound

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sdalmau.lostfound.databinding.FragmentFormularioPeridosBinding
import com.sdalmau.lostfound.sharedPreferences.SharedApp
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*


class formularioPeridos : Fragment() {
    private val db = Firebase.firestore
    private lateinit var aux : auxGeneral


    //valors imatge
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null


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

        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)
        storageReference = FirebaseStorage.getInstance().reference


        binding.afegirGaleria.setOnClickListener{
            launchGallery()

        }
        binding.guardarInformacio.setOnClickListener{


            nom = binding.nomAnimal.text.toString()
            color = binding.colorAnimal.text.toString()
            visualitzacio =  binding.ultimaVisualitzacio.text.toString()
            descripcio = binding.DESCRIPCIO.text.toString()
            tipus = aux.getAnimal()
            amigable = binding.amigable.isChecked

            if(nom.isNotBlank() && color.isNotBlank() && visualitzacio.isNotBlank() && descripcio.isNotBlank()){
                uploadImage(tipus , nom, color, visualitzacio, descripcio, amigable)
                view?.findNavController()?.navigate(R.id.action_formularioPeridos_to_animalsPerduts2)
            }

            else{
                Toast.makeText(context, "No s'han introduit les dades obligatories", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    fun crearAnimalPerdut(tipus : String, nom : String, color : String, visualitzacio : String, detalls : String, amigable : Boolean, imatge : String){

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
            "id" to documentId,
            "imatge" to imatge
            )

       document.set(animal)

    }

    fun launchGallery(){val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data

        }
    }

    private fun uploadImage(tipus : String, nom : String, color : String, visualitzacio : String, detalls : String, amigable : Boolean){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    crearAnimalPerdut(tipus , nom, color, visualitzacio, detalls, amigable, downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }




}