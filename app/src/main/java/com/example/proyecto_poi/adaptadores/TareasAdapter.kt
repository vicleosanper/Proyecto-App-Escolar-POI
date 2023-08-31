package com.example.proyecto_poi.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.R
import com.example.proyecto_poi.modelos.Tarea


class TareasAdapter (private val listaTareas: MutableList<Tarea>,private var carrera : String
,private var nombreusuario : String,private val itemClickListener:OnChatClickListener)
    : RecyclerView.Adapter<TareasAdapter.TareasViewHolder>(){

    interface OnChatClickListener{
        fun onChatClick(nombre: String, materia: String) // Todos los datos de la tarea
    }

    class TareasViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun asignarinformacion(tarea: Tarea,carrera: String,
                               nombreusuario: String,itemClickListener:OnChatClickListener){

            itemView.setOnClickListener { itemClickListener.onChatClick(tarea.Nombre,tarea.Materia) }

            if(tarea.Carrera == carrera && tarea.Asignado == nombreusuario){

                itemView.findViewById<TextView>(R.id.TVNombre_tarea).text = tarea.Nombre
                itemView.findViewById<TextView>(R.id.TV_Materia).text = tarea.Materia

                val params = itemView.findViewById<LinearLayout>(R.id.contenedortareas).layoutParams

                val newParams = FrameLayout.LayoutParams(

                    params.width,
                    params.height
                )
                itemView.findViewById<LinearLayout>(R.id.contenedortareas).layoutParams = newParams
/*
                val dateFormater = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
                val fecha = dateFormater.format(Date(mensaje.timeStamp as Long))

                itemView.tvFecha.text = fecha

 */
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TareasAdapter.TareasViewHolder {

        return TareasViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.diseno_lista_tareas, parent, false)
        )

    }

    override fun onBindViewHolder(holder: TareasAdapter.TareasViewHolder, position: Int) {

        holder.asignarinformacion(listaTareas[position],carrera,nombreusuario,itemClickListener)

    }

    override fun getItemCount(): Int = listaTareas.size


}