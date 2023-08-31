package com.fcfm.poi.chat.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.CifradoTools
import com.example.proyecto_poi.R
import com.fcfm.poi.chat.modelos.Mensaje
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(private val listaMensajes: MutableList<Mensaje>,private var id_chat : String):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun asignarInformacion(mensaje: Mensaje,id_chat: String) {

            if (id_chat == mensaje.id_chat){ //Si el mensaje tiene el identificador que deseamos

                itemView.findViewById<TextView>(R.id.TVNombre).text = mensaje.de

                if(mensaje.encriptado == "Activada"){

                    itemView.findViewById<TextView>(R.id.TVMensajes).text = CifradoTools.descifrar(mensaje.contenido,"POI03")

                }
                if(mensaje.encriptado == "Desactivada"){

                    itemView.findViewById<TextView>(R.id.TVMensajes).text = mensaje.contenido

                }


                val dateFormater = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
                val fecha = dateFormater.format(Date(mensaje.timeStamp as Long))

                itemView.findViewById<TextView>(R.id.tvHora).text = fecha

                val params = itemView.findViewById<LinearLayout>(R.id.contenedorchat).layoutParams

                if (mensaje.esMio) {

                    val newParams = FrameLayout.LayoutParams(
                        params.width,
                        params.height,
                        Gravity.END
                    )
                    itemView.findViewById<LinearLayout>(R.id.contenedorchat).layoutParams = newParams

                } else {

                    val newParams = FrameLayout.LayoutParams(
                        params.width,
                        params.height,
                        Gravity.START
                    )
                    itemView.findViewById<LinearLayout>(R.id.contenedorchat).layoutParams = newParams
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.mensajes_chat, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.asignarInformacion(listaMensajes[position],id_chat)
    }

    override fun getItemCount(): Int = listaMensajes.size


}