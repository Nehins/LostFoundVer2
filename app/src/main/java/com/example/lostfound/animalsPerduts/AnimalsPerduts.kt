package com.example.lostfound.animalsPerduts

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
import com.example.lostfound.seleccioAnimal.seleccioAnimalAux


class animalsPerduts : Fragment() {

    private lateinit var aux : seleccioAnimalAux

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAnimalsPerdutsBinding>(inflater,
            R.layout.fragment_animals_perduts,container,false)

        aux = ViewModelProvider(requireActivity()).get(seleccioAnimalAux::class.java)

        binding.afegirAnimalPerdut.setOnClickListener{
            aux.setTipus(1)
            view?.findNavController()?.navigate(R.id.action_animalsPerduts2_to_selecionaAnimal)

        }
        return binding.root
    }

    companion object {
        fun newInstance(): animalsPerduts = animalsPerduts()
    }

}