package com.example.lostfound.seleccioAnimal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.lostfound.R
import com.example.lostfound.auxGeneral
import com.example.lostfound.databinding.FragmentSelecionaAnimalBinding


class SelecionaAnimal : Fragment() {

    private lateinit var aux : auxGeneral

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSelecionaAnimalBinding>(inflater,
            R.layout.fragment_seleciona_animal,container,false)


        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)

        binding.perroButton.setOnClickListener{
            aux.setAnimal("Gos")
            obrirFormulari()
        }

        binding.gatoButton.setOnClickListener {
            aux.setAnimal("Gat")
            obrirFormulari()
        }

        binding.pajaroButton.setOnClickListener{
            aux.setAnimal("Ocell")
            obrirFormulari()
        }

        binding.reptilButton.setOnClickListener {
            aux.setAnimal("Reptils")
            obrirFormulari()
        }

        binding.roedorButton.setOnClickListener {
            aux.setAnimal("Rosegadors")
            obrirFormulari()
        }

        binding.animalesGranjaButton.setOnClickListener {
            aux.setAnimal("Animals de granja")
            obrirFormulari()
        }

        binding.otrosButton.setOnClickListener {
            aux.setAnimal("Altres")
            obrirFormulari()
        }


        return binding.root
    }

    fun obrirFormulari(){

        aux = ViewModelProvider(requireActivity()).get(auxGeneral::class.java)

        if(aux.getTipus()==1){
            view?.findNavController()?.navigate(R.id.action_selecionaAnimal_to_formularioPeridos)

        }
        else if(aux.getTipus()==2){
            view?.findNavController()?.navigate(R.id.action_selecionaAnimal_to_formularioEncontrados)
        }

    }

}