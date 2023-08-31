package com.example.proyecto_poi

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_poi.modelos.usuarios
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaginaInicioActivity :AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private lateinit var carrera:String
    private lateinit var correo:String

    private val database = FirebaseDatabase.getInstance()
    private val UsuaRef = database.getReference("Usuarios")

    private lateinit var encriptacion:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagina_inicio)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        carrera = intent.getStringExtra("carrera") ?: "sin_nombre"
        correo = intent.getStringExtra("correo") ?: "sin_nombre"

        Logros()

        findViewById<Button>(R.id.btnGrupo).setOnClickListener {

            val intent = Intent(this, Listachats::class.java)
            intent.putExtra("nombreUsuario", nombreUsuario)
            intent.putExtra("TipoChat","Grupos") //Tipo de chat grupal
            intent.putExtra("Padrechat","sin_Padre")
            intent.putExtra("carrera",carrera)
            intent.putExtra("Encriptacion",encriptacion)

            startActivity(intent)

        }


        findViewById<Button>(R.id.btnIndividual).setOnClickListener {

            val intentChat = Intent(this, Listachats::class.java)
            intentChat.putExtra("nombreUsuario", nombreUsuario)
            intentChat.putExtra("TipoChat","Individual") //Aqui tengo que ver despues el nombre del usuario al que se le da click
            intentChat.putExtra("Padrechat","sin_Padre")
            intentChat.putExtra("Encriptacion",encriptacion)

            startActivity(intentChat)

        }

        findViewById<Button>(R.id.btnTareas).setOnClickListener {

            if(nombreUsuario == "admin"){

                val intentTa = Intent(this, Asignartarea::class.java)
                intentTa.putExtra("nombreUsuario", nombreUsuario)
                intentTa.putExtra("Encriptacion",encriptacion)
                intentTa.putExtra("carrera",carrera)
                intentTa.putExtra("correo",correo)

                startActivity(intentTa)

            }
            else{
                val intentT = Intent(this, Listatareas::class.java)
                intentT.putExtra("nombreUsuario", nombreUsuario)
                intentT.putExtra("carrera",carrera)

                startActivity(intentT)

            }

        }

        findViewById<Button>(R.id.btn_opciones).setOnClickListener{

            val intentOP = Intent(this, Configuraciones::class.java)
            intentOP.putExtra("nombreUsuario", nombreUsuario)
            intentOP.putExtra("carrera",carrera)
            intentOP.putExtra("correo",correo)

            startActivity(intentOP)

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

    private fun Logros(){

        UsuaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario: usuarios = snap.getValue(usuarios::class.java) as usuarios

                    if(usuario.Correo == correo){

                        if(usuario.Usuario == nombreUsuario && usuario.carrera == carrera){

                            if(usuario.Usuario == "admin"){

                                findViewById<TextView>(R.id.TV_LOGROS).text = ""
                                findViewById<TextView>(R.id.TV_enmedio).text = "Bienvenido administrador!"
                                findViewById<TextView>(R.id.TV_ESLOGAN).text = ""

                            }
                            else{


                                findViewById<TextView>(R.id.TV_LOGROS).text = usuario.tareas.toString()

                                if(usuario.tareas == 0){

                                    findViewById<TextView>(R.id.TV_ESLOGAN).text = "Sube tu primer tarea!"

                                }

                                if(usuario.tareas == 1){

                                    findViewById<TextView>(R.id.TV_ESLOGAN).text = "Felicidades por tu primer tarea!"

                                }
                                if(usuario.tareas > 1 && usuario.tareas!=5){

                                    findViewById<TextView>(R.id.TV_ESLOGAN).text = "Sigue asi!"

                                }
                                if(usuario.tareas == 5){

                                    findViewById<TextView>(R.id.TV_ESLOGAN).text = "Felicidades por tu quinta tarea!"

                                }

                            }

                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@PaginaInicioActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })



    }

}