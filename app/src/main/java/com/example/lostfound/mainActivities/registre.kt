package com.example.lostfound.mainActivities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityRegistreBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class registre : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityRegistreBinding
    private val db = Firebase.firestore


    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        var usuari = ""
        var telefon = ""
        var mEmail = ""
        var mPassword = ""
        var mRepeatPassword = ""

        auth = Firebase.auth
        storageReference = FirebaseStorage.getInstance().reference

        binding.addPhoto.setOnClickListener{
            launchGallery()

        }

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
                uploadImage(usuari, telefon, mEmail, mPassword)
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

    fun crearUsuari(nom : String, telefon : String, email : String, password : String, imatge : String){
        val document = db.collection("Usuaris").document()
        val documentId = document.id

        val user = hashMapOf(
            "nom" to nom,
            "telefon" to telefon,
            "correu" to email,
            "contrasenya" to password,
            "id" to documentId,
            "imatge" to imatge

        )

        document.set(user)

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
                    crearUsuari(nom, telefon, email, password, downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

}