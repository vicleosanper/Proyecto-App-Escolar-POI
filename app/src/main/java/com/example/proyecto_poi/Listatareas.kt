package com.example.proyecto_poi

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.adaptadores.TareasAdapter
import com.example.proyecto_poi.modelos.Tarea
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Listatareas:AppCompatActivity(),TareasAdapter.OnChatClickListener {

    private lateinit var nombreUsuario: String
    private lateinit var carrera: String
    private var lista = mutableListOf<Tarea>()

    private val database = FirebaseDatabase.getInstance()
    private val TareaRef = database.getReference("Tareas")
    public lateinit var adaptador : TareasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_tareas)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        carrera = intent.getStringExtra("carrera") ?: "sin_nombre"

        adaptador = TareasAdapter(lista,carrera,nombreUsuario,this)
        findViewById<RecyclerView>(R.id.lista_tareas).adapter = adaptador

        imprimirtareas()

    }

    private fun imprimirtareas(){

        TareaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                lista.clear()

                for (snap in snapshot.children) {

                    val tarea: Tarea  = snap.getValue(Tarea::class.java) as Tarea

                    if(tarea.Carrera == carrera && tarea.Asignado == nombreUsuario){

                        lista.add(tarea)

                    }

                }

                if (lista.size > 0) {

                    adaptador.notifyDataSetChanged()
                    findViewById<RecyclerView>(R.id.lista_tareas).smoothScrollToPosition(lista.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Listatareas, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })



    }

    override fun onChatClick(nombre: String, materia: String){

        val intentT = Intent(this, Tareaactivity::class.java)

        intentT.putExtra("nombreUsuario", nombreUsuario)
        intentT.putExtra("carrera",carrera)
        intentT.putExtra("nombreTarea",nombre)
        intentT.putExtra("Materia",materia)

        startActivity(intentT)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            android.R.id.home->{
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

}