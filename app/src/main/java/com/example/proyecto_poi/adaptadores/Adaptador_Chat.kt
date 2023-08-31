package com.example.proyecto_poi.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.R

data class Mensaje(

    val de: String,
    val  contenido: String,
    val fecha: String

)

class Adaptador_Chat(val ListaMensajes: List<Mensaje>): RecyclerView.Adapter<Adaptador_Chat.ChatViewHolder>() {

    class ChatViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

       val miView = LayoutInflater.from(parent.context)
           .inflate(R.layout.mensajes_chat, parent, false)
        return ChatViewHolder(miView)

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

       val tvNombre = holder.itemView.findViewById<TextView>(R.id.tvNombre)
        val tvMensaje = holder.itemView.findViewById<TextView>(R.id.tvMensaje)
        val tvHora = holder.itemView.findViewById<TextView>(R.id.tvHora)

        tvNombre.text = ListaMensajes[position].de
        tvMensaje.text = ListaMensajes[position].contenido
        tvHora.text = ListaMensajes[position].fecha

    }

    override fun getItemCount(): Int {
        return ListaMensajes.size
    }


}