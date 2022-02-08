package com.example.lostfound.animalsTrobats

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
import com.example.lostfound.R
import com.example.lostfound.auxGeneral
import com.example.lostfound.databinding.FragmentFormularioEncontradosBinding
import com.example.lostfound.sharedPreferences.SharedApp
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*


class formularioEncontrados : Fragment() {
    private val db = Firebase.firestore
    private lateinit var aux : auxGeneral

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null

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
        storageReference = FirebaseStorage.getInstance().reference

        binding.aAdirGaleria.setOnClickListener{
            launchGallery()

        }

        binding.guardarInformacio.setOnClickListener{
            nom = binding.nomAnimal.text.toString()
            color = binding.colorAnimal.text.toString()
            llocTrobat =  binding.llocTrobat.text.toString()
            descripcio = binding.DESCRIPCIO.text.toString()
            tipus = aux.getAnimal()

            if(color.isNotBlank() && llocTrobat.isNotBlank() && descripcio.isNotBlank()){
                uploadImage(tipus , nom, color, llocTrobat, descripcio)
                view?.findNavController()?.navigate(R.id.action_formularioEncontrados_to_animalsTrobats)


            }

            else{
                Toast.makeText(context, "No s'han introduit les dades obligatories", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }




    fun crearAnimalTrobat(tipus : String, nom : String, color : String, lloc : String, detalls : String, imatge : String){

        val document = db.collection("AnimalsTrobats").document()
        val documentId = document.id

        val animal = hashMapOf(
            "tipus" to tipus,
            "nom" to nom,
            "color" to color,
            "detalls" to detalls,
            "llocTrobat" to lloc,
            "telefon" to SharedApp.prefs.telefonUsuari,
            "nomCuidador" to SharedApp.prefs.nomUsuari,
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

    private fun uploadImage(tipus : String, nom : String, color : String, lloc : String, detalls : String){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + SharedApp.prefs.telefonUsuari+"/" + UUID.randomUUID().toString())
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
                    crearAnimalTrobat(tipus , nom, color, lloc, detalls, downloadUri.toString())
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