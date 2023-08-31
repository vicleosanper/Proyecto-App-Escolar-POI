package com.example.proyecto_poi

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.adaptadores.TareasAdapter
import com.example.proyecto_poi.modelos.Tarea
import com.example.proyecto_poi.modelos.usuarios
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class Tareaactivity:AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private lateinit var carrera: String
    private lateinit var materia: String
    private lateinit var nombretarea: String


    var vuelta:Int = 0
    var vueltaU:Int = 0

    var tareasEntregadas:Int = 0

    //private var lista = mutableListOf<Tarea>()

    private val database = FirebaseDatabase.getInstance()
    private val TareaRef = database.getReference("Tareas")
    private val UsuaRef = database.getReference("Usuarios")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entregar_tarea)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        carrera = intent.getStringExtra("carrera") ?: "sin_carrera"

        materia = intent.getStringExtra("Materia") ?: "sin_materia"
        nombretarea = intent.getStringExtra("nombreTarea") ?: "sin_tarea"

        findViewById<TextView>(R.id.TV_NomTarea).text = nombretarea


        findViewById<Button>(R.id.btn_Entregar).setOnClickListener{

            entregarTarea(Tarea(nombretarea,materia,carrera,nombreUsuario,false,""))

            if(vuelta == 1){

                Toast.makeText(this, "Ya haz entregado tu tarea", Toast.LENGTH_SHORT).show()

            }

            if(vueltaU == 0){

                MarcarTarea(usuarios(nombreUsuario,"","",carrera,true,"",0))

            }

            if(tareasEntregadas == 1){

                Toast.makeText(this, "Gracias por entregar tu primer tarea", Toast.LENGTH_SHORT).show()
                vuelta = 0

            }

        }

    }

    private fun entregarTarea(tareaentregada:Tarea){

        TareaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                for (snap in snapshot.children) {

                    val tarea: Tarea = snap.getValue(Tarea::class.java) as Tarea

                    if(tarea.Carrera == carrera && tarea.Asignado == nombreUsuario
                        && tarea.Materia == materia && tarea.Nombre == nombretarea){


                        if(!tarea.Entregado){

                            tareaentregada.Entregado = true
                            tareaentregada.id = tarea.id

                            TareaRef.child(tarea.id).setValue(tareaentregada)
                            // TareaRef.setValue(tareaentregada)

                        }
                        if(tarea.Entregado){

                            vuelta = 1

                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Tareaactivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun MarcarTarea(Usuario:usuarios){

        UsuaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario: usuarios = snap.getValue(usuarios::class.java) as usuarios

                    if(usuario.Usuario == nombreUsuario && usuario.carrera == carrera){

                        if(vueltaU == 0){

                            Usuario.tareas = (usuario.tareas + 1)
                            Usuario.Usuario = usuario.Usuario
                            Usuario.Contraseña = usuario.Contraseña
                            Usuario.carrera = usuario.carrera
                            Usuario.Correo = usuario.Correo
                            Usuario.activo = usuario.activo
                            Usuario.id_Usuario = usuario.id_Usuario
                            Usuario.ImagenURL = usuario.ImagenURL


                            UsuaRef.child(usuario.id_Usuario).setValue(Usuario)

                            tareasEntregadas = Usuario.tareas

                            vueltaU = 1

                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Tareaactivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })


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