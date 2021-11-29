package com.example.lostfound

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.lostfound.databinding.FragmentRegistreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.concurrent.timerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Registre.newInstance] factory method to
 * create an instance of this fragment.
 */
class Registre : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //creem variables per a assignarlis el seu valor
    private lateinit var regNom : EditText
    private lateinit var regTelefon : EditText
    private lateinit var regCorreu : EditText
    private lateinit var regContrasenya : EditText
    private lateinit var regVerConstrasenya : EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentRegistreBinding>(inflater,
            R.layout.fragment_registre, container, false)

        regNom = binding.registreNomUsuari
        regTelefon = binding.registreTelefon
        regCorreu = binding.registreCorreu
        regContrasenya = binding.registreContra
        regVerConstrasenya = binding.registreContraVerificar
        auth = FirebaseAuth.getInstance()

        binding.singIn.setOnClickListener {view : View ->
            if(regContrasenya.text==regVerConstrasenya.text){
                if(regCorreu.text.isNotEmpty() && regContrasenya.text.isNotEmpty()){


                }else{

                }
            }

        }


        return binding.root

    }

    private fun signUpUser() {

        auth.createUserWithEmailAndPassword(regCorreu.text.toString(), regContrasenya.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registre complet",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Registre.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Registre().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}