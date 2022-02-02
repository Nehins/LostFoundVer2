package com.example.lostfound.animalsTrobats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.auxGeneral
import com.example.lostfound.databinding.FragmentAnimalsTrobatsBinding
import com.google.firebase.firestore.*


class animalsTrobats : Fragment() {


    private lateinit var aux : auxGeneral
    private lateinit var recycleView : RecyclerView
    private lateinit var trobatsArraylist : ArrayList<trobats>
    private lateinit var myAdapter : AdapterTrobats
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAnimalsTrobatsBinding>(inflater,
            R.layout.fragment_animals_trobats,container,false)

        recycleView = binding.trobatsView
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)

        trobatsArraylist = arrayListOf()

        myAdapter = AdapterTrobats(trobatsArraylist){ animal ->
            aux.setidTrobat(animal.id.toString())
            view?.findNavController()?.navigate(R.id.action_animalsTrobats_to_animalSelecionatTrobat)

        }

        recycleView.adapter = myAdapter

        EventChangeListener()


        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)

        binding.afegirAnimalTrobat.setOnClickListener{
            aux.setTipus(2)
            view?.findNavController()?.navigate(R.id.action_animalsTrobats_to_selecionaAnimal)

        }

        return binding.root
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("AnimalsTrobats")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore error",error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            trobatsArraylist.add(dc.document.toObject(trobats::class.java))
                        }
                    }

                    myAdapter.notifyDataSetChanged()

                }
            })

    }
}