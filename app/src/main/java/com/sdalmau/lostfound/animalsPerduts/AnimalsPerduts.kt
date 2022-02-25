package com.sdalmau.lostfound.animalsPerduts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdalmau.lostfound.R
import com.sdalmau.lostfound.auxGeneral
import com.sdalmau.lostfound.databinding.FragmentAnimalsPerdutsBinding
import com.google.firebase.firestore.*


class animalsPerduts : Fragment() {

    private lateinit var aux : auxGeneral
    private lateinit var recycleView : RecyclerView
    private lateinit var perdutsArraylist : ArrayList<perduts>
    private lateinit var myAdapter : AdapterPerduts
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Animals perduts"
        val binding = DataBindingUtil.inflate<FragmentAnimalsPerdutsBinding>(inflater,
            R.layout.fragment_animals_perduts,container,false)

        recycleView = binding.perdusView
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)

        perdutsArraylist = arrayListOf()

        myAdapter = AdapterPerduts(perdutsArraylist){ animal ->
            aux.setidPerdut(animal.id.toString())
            view?.findNavController()?.navigate(R.id.action_animalsPerduts2_to_animalSelecionat)

        }

        recycleView.adapter = myAdapter

        EventChangeListener()


        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)

        binding.afegirAnimalPerdut.setOnClickListener{
            aux.setTipus(1)
            view?.findNavController()?.navigate(R.id.action_animalsPerduts2_to_selecionaAnimal)

        }
        return binding.root
    }


    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("AnimalsPerduts")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore error",error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            perdutsArraylist.add(dc.document.toObject(perduts::class.java))
                        }
                    }

                    myAdapter.notifyDataSetChanged()

                }
            })

    }

}