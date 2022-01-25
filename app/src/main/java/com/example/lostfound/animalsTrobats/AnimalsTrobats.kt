package com.example.lostfound.animalsTrobats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.FragmentAnimalsPerdutsBinding
import com.example.lostfound.databinding.FragmentAnimalsTrobatsBinding
import com.example.lostfound.seleccioAnimal.seleccioAnimalAux


class animalsTrobats : Fragment() {


    private lateinit var aux : seleccioAnimalAux

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAnimalsTrobatsBinding>(inflater,
            R.layout.fragment_animals_trobats,container,false)

        aux = ViewModelProvider(requireActivity()).get(seleccioAnimalAux::class.java)

        binding.afegirAnimalTrobat.setOnClickListener{
            aux.setTipus(2)
            view?.findNavController()?.navigate(R.id.action_animalsTrobats_to_selecionaAnimal)

        }
        return binding.root
    }

    companion object {
        fun newInstance(): animalsTrobats = animalsTrobats()
    }
}