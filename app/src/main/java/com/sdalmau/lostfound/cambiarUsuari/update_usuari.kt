package com.sdalmau.lostfound.cambiarUsuari

import android.app.Activity
import android.content.ContentValues.TAG
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
import androidx.navigation.findNavController
import com.sdalmau.lostfound.R
import com.sdalmau.lostfound.databinding.FragmentUpdateUsuariBinding
import com.sdalmau.lostfound.sharedPreferences.SharedApp
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class update_usuari : Fragment() {

    private val user = Firebase.auth.currentUser
    private lateinit var db : FirebaseFirestore
    private var telefonSenseCambiar = ""

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    private var foto = "";

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentUpdateUsuariBinding>(inflater,
            R.layout.fragment_update_usuari,container,false)

        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        db.collection("Usuaris").whereEqualTo("id", SharedApp.prefs.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    telefonSenseCambiar = document["telefon"].toString()

                    binding.nomUsuari.setText(document["nom"].toString())
                    binding.telefon.setText(document["telefon"].toString())
                    binding.correu.setText(document["correu"].toString())
                    binding.contra.setText(document["contrasenya"].toString())
                    binding.verContra.setText(document["contrasenya"].toString())
                    foto = document["imatge"].toString()

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        binding.addPhotog.setOnClickListener{
            launchGallery()

        }


        binding.button.setOnClickListener {
            if(binding.contra.text.toString().equals(binding.verContra.text.toString())){

                var i = 0

                if(filePath==null){
                    cambiarUsuari(binding.nomUsuari.text.toString(), binding.telefon.text.toString(), binding.correu.text.toString(), binding.contra.text.toString(), foto)
                }

                else{
                    uploadImage(binding.nomUsuari.text.toString(), binding.telefon.text.toString(), binding.correu.text.toString(), binding.contra.text.toString())
                }

                nouCorreu(binding.correu.text.toString())
                novaContrasenya(binding.contra.text.toString())
                cambiarTelefon(binding.telefon.text.toString())

                Thread.sleep(2000)

                val snack = Snackbar.make(it,"Usuari modificat",Snackbar.LENGTH_LONG)
                snack.show()

                view?.findNavController()?.navigate(R.id.action_update_usuari_to_animalsPerduts22)

            }

        }

        return binding.root
    }


    fun nouCorreu(correuNou : String){
        user!!.updateEmail(correuNou)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }
    }

    fun novaContrasenya(contraNou : String){
        user!!.updatePassword(contraNou)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }
    }

    fun cambiarUsuari(nom : String, telefon : String, correu : String, contra : String, imatge : String){

        val doc =  db.collection("Usuaris").document(SharedApp.prefs.id)

        val user = mapOf(
            "id" to SharedApp.prefs.id,
            "nom" to nom,
            "telefon" to telefon,
            "correu" to correu,
            "contrasenya" to contra,
            "imatge" to imatge
        )

        doc.set(user).addOnSuccessListener {
            //Toast.makeText(context, "Usuari modificat amb exit!!" , Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
                Toast.makeText(context, "Falla al hacer update", Toast.LENGTH_SHORT).show()
            }

    }

    fun launchGallery(){
        val intent = Intent()
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

    private fun uploadImage(nom : String, telefon : String, email : String, password : String){
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
                   cambiarUsuari(nom, telefon, email, password, downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    fun cambiarTelefon(telefon : String){
        db = FirebaseFirestore.getInstance()

        db.collection("AnimalsTrobats").whereEqualTo("telefon", telefonSenseCambiar)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("AnimalsTrobats").document(document.id)
                        .update("telefon", telefon)
                        .addOnCompleteListener{
                            Log.d(TAG, "Error getting documents: ")
                        }
                        .addOnFailureListener{
                            Log.d(TAG, "Error getting documents: ")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


        db.collection("AnimalsPerduts").whereEqualTo("telefon", telefonSenseCambiar)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("AnimalsPerduts").document(document.id)
                        .update("telefon", telefon)
                        .addOnCompleteListener{

                        }
                        .addOnFailureListener{
                            Log.d(TAG, "Error getting documents: ")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }



    }

}



