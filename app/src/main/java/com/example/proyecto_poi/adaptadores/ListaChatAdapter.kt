package com.fcfm.poi.chat.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.R
import com.example.proyecto_poi.modelos.chat
import com.example.proyecto_poi.modelos.usuarios


class ListaChatAdapter(private val listaChats: MutableList<chat>,
                       private var tipo_chat : String,
                       private val itemClickListener:OnChatClickListener)
        :RecyclerView.Adapter<ListaChatAdapter.ListaViewHolder>() {

    interface OnChatClickListener{
        fun onChatClick(nombre: String)
    }

    class ListaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun asignarInformacion(Chat: chat,tipo_chat: String,itemClickListener:OnChatClickListener) {

            itemView.setOnClickListener { itemClickListener.onChatClick(Chat.nombre) }

            if (tipo_chat == Chat.tipo_chat){ //Si el mensaje tiene el identificador que deseamos

                itemView.findViewById<TextView>(R.id.id_Nombre).text = Chat.nombre

                itemView.findViewById<TextView>(R.id.id_Mensajes).text = Chat.tipo_chat





                //NOTA: Pasar el mensaje del que envio el mensaje para mostrarlo

                val params = itemView.findViewById<LinearLayout>(R.id.contenedorchats).layoutParams

                val newParams = FrameLayout.LayoutParams(

                    params.width,
                    params.height
                )
                itemView.findViewById<LinearLayout>(R.id.contenedorchats).layoutParams = newParams

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {

        return ListaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.diseno_lista_chats, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {

        holder.asignarInformacion(listaChats[position],tipo_chat,itemClickListener)

    }

    override fun getItemCount(): Int = listaChats.size




}