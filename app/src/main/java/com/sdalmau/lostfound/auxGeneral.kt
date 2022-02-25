package com.sdalmau.lostfound

import androidx.lifecycle.ViewModel

class auxGeneral: ViewModel() {


    //Quan hem seleicionat el animal, si el tipus de formulari es 1, anira al formulari d'animals perduts, si es 2, anira al formulari d'animals trobats
    var tipusformulari = 0
    fun getTipus(): Int { return tipusformulari }
    fun setTipus(tipus : Int){ tipusformulari=tipus}

    var tipusAnimal = ""
    fun getAnimal(): String { return tipusAnimal }
    fun setAnimal(tipus : String){ tipusAnimal = tipus}


    var idPerdut = ""
    fun getidPerdut() : String {return idPerdut}
    fun setidPerdut(posicio : String) {idPerdut = posicio}

    var idTrobat = ""
    fun getidTrobat () : String {return idTrobat }
    fun setidTrobat (posicio : String) {idTrobat  = posicio}

}
