package com.example.proyecto_poi

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.modelos.Tarea
import com.example.proyecto_poi.modelos.chat
import com.example.proyecto_poi.modelos.usuarios
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Asignartarea:AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val tarearef = database.getReference("Tareas")
    private val UsuariosRef = database.getReference("Usuarios")
    private val chatsRef = database.getReference("chats")

    private lateinit var vuelta : String

    private lateinit var encriptacion:String
    private lateinit var nombreUsuario: String
    private lateinit var carrera:String
    private lateinit var correo:String


    private lateinit var NombreTarea : String
    private lateinit var Materia : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.asignar_tarea)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Validar()

        vuelta = "Falso"
        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        carrera = intent.getStringExtra("carrera") ?: "sin_nombre"
        correo = intent.getStringExtra("correo") ?: "sin_nombre"


        findViewById<Button>(R.id.btn_asignarTarea).setOnClickListener{

            carrera = findViewById<TextView>(R.id.id_Carrera).text.toString()
            NombreTarea = findViewById<TextView>(R.id.id_NombreTarea).text.toString()
            Materia = findViewById<TextView>(R.id.id_Materia).text.toString()

            if(carrera.isEmpty() || NombreTarea.isEmpty() || Materia.isEmpty()){

                Toast.makeText(this@Asignartarea, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()

            }
            else{

                AsignarTarea(Tarea(NombreTarea,Materia,carrera,"",false,""))

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tarea asignada")
                builder.setMessage("Haz asignado una tarea nueva")
                builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

                    val intent = Intent(this, PaginaInicioActivity::class.java)
                    intent.putExtra("nombreUsuario", nombreUsuario)
                    intent.putExtra("carrera",carrera)
                    intent.putExtra("correo",correo)
                    intent.putExtra("Encriptacion",encriptacion)
                    startActivity(intent)
                    finish()

                })

                builder.show()


            }

        }



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


    private fun AsignarTarea(tarea:Tarea) {

        UsuariosRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario: usuarios  = snap.getValue(usuarios::class.java) as usuarios

                    if(usuario.carrera == tarea.Carrera){

                        val mensajeFirebase = tarearef.push()
                        tarea.Nombre = NombreTarea ?: ""
                        tarea.Materia = Materia ?: ""
                        tarea.Carrera = carrera ?: ""
                        tarea.Entregado = false
                        tarea.Asignado = usuario.Usuario
                        tarea.id = mensajeFirebase.key ?: ""

                        mensajeFirebase.setValue(tarea)

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Asignartarea, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })

    }

    /*
    private fun Validar(){

        chatsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val chatt: chat  = snap.getValue(chat::class.java) as chat

                    if(chatt.Padre == carrera && chatt.nombre == Materia){

                        validado = "Verdadero"

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Asignartarea, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })


    }

     */

}