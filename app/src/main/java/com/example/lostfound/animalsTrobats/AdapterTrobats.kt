package com.example.lostfound.animalsTrobats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.squareup.picasso.Picasso

class AdapterTrobats (private val trobatslist : ArrayList<trobats>, private val listener : (trobats) -> Unit): RecyclerView.Adapter<AdapterTrobats.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTrobats.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.perduts_view,parent,false)
        return MyViewHolder(itemView)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val animal: trobats = trobatslist[position]
        holder.nom.text = animal.nom
        holder.telefon.text = animal.telefon
        holder.tipus.text = animal.tipus
        Picasso.get()
            .load(animal.imatge)
            .into(holder.imatge)

        holder.itemView.setOnClickListener(){
            listener(animal)

        }

    }

    override fun getItemCount(): Int {
        return trobatslist.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imatge: ImageView = itemView.findViewById(R.id.imatge)
        val nom : TextView = itemView.findViewById(R.id.nomPerdutView)
        val tipus : TextView = itemView.findViewById(R.id.tipudPerdutViewTipus)
        val telefon : TextView = itemView.findViewById(R.id.telefonPerdutView)
    }
}