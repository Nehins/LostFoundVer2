package com.example.lostfound.animalsPerduts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R

class AdapterPerduts(private val perdutslist : ArrayList<perduts>, private val listener : (perduts) -> Unit): RecyclerView.Adapter<AdapterPerduts.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPerduts.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.perduts_view,parent,false)
        return MyViewHolder(itemView)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val animal: perduts = perdutslist[position]
        holder.nom.text = animal.nom
        holder.telefon.text = animal.telefon
        holder.tipus.text = animal.tipus
//        Picasso.get()
//            .load(animal.imatge)
//            .into(holder.imatge)

        holder.itemView.setOnClickListener(){
            listener(animal)

        }

    }

    override fun getItemCount(): Int {
       return perdutslist.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nom : TextView = itemView.findViewById(R.id.nomPerdutView)
        val tipus : TextView = itemView.findViewById(R.id.tipudPerdutViewTipus)
        val telefon : TextView = itemView.findViewById(R.id.telefonPerdutView)
    }
}